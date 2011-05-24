/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom
 */
public class DataBaseStorageImpl implements Storage {
    private static final Logger logger = Logger.getLogger(DataBaseStorageImpl.class.getCanonicalName());
    
    private static final String DBURL = "jdbc:derby://localhost:1527/lidadb";
    private static final String USERNAME = "lida";
    private static final String PASSWORD = "lida";

    private static final String VALUES_PLACEHOLDER = "values_placeholder";
    private static final String IDS_PLACEHOLDER = "ids_placeholder";
    private static final String TABLENAME_PLACEHOLDER = "tablename_placeholder";
    private static final String WHERE_PLACEHOLDER = "where_placeholder";

    private static final String SQL_SELECT_NOORDER = "SELECT * FROM APP."+TABLENAME_PLACEHOLDER;
    private static final String SQL_SELECT = "SELECT * FROM APP."+TABLENAME_PLACEHOLDER+" ORDER BY "+IDS_PLACEHOLDER+" DESC";
    private static final String SQL_SELECT_WHERE = "SELECT * FROM APP."+TABLENAME_PLACEHOLDER+" WHERE "+WHERE_PLACEHOLDER+" ORDER BY "+IDS_PLACEHOLDER+" DESC";
    private static final String SQL_MAXID = "SELECT max(id) FROM APP."+TABLENAME_PLACEHOLDER;
    private static final String SQL_DELETE = "DELETE FROM APP."+TABLENAME_PLACEHOLDER+" WHERE "+WHERE_PLACEHOLDER;
    private static final String SQL_INSERT = "INSERT INTO APP."+TABLENAME_PLACEHOLDER+" VALUES("+VALUES_PLACEHOLDER+")";
    
    private boolean connected = false;
    private Connection dbConnection = null;

    private static DataBaseStorageImpl instance = null;

    private DataBaseStorageImpl() {
    }

    public static Storage getInstance() {
        if (instance == null) instance = new DataBaseStorageImpl();
        return instance;
    }

    @Override
	public boolean open() {
        if (connected) return false;
        
        try {
            dbConnection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            if (!dbConnection.isClosed()) {
                connected = true;
                return true;
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to connect to the database.");
        }

        return false;
    }

    @Override
	public boolean close() {
        if (!connected) return false;

        try {
            dbConnection.close();
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    @Override
	public Object[] getDataRow(String storageName) {
        Object[][] data = getData(storageName, new Object[0], new Object[0], 1);
        return ((data != null && data.length > 0) ? data[0] : null);
    }

    @Override
	public Object[] getDataRow(String storageName, Object[] propertyNames, Object[] propertyValues) {
        Object[][] data = getData(storageName, propertyNames, propertyValues, 1);
        return ((data != null && data.length > 0) ? data[0] : null);
    }

    @Override
	public Object[][] getData(String storageName, int maxRows) {
        return getData(storageName, new Object[0], new Object[0], maxRows);
    }

    @Override
	public Object[][] getData(String storageName, Object[] propertyNames, Object[] propertyValues) {
        return getData(storageName, propertyNames, propertyValues, -1);
    }

    @Override
	public Object[][] getData(String storageName, Object[] propertyNames, Object[] propertyValues, int maxRows) {
        if (!connected) return null;
        Object[][] result = null;
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.setMaxRows(1);
            ResultSet rs = stmt.executeQuery(SQL_SELECT_NOORDER.replaceFirst(TABLENAME_PLACEHOLDER, storageName));
            ResultSetMetaData rsmd = rs.getMetaData();

            String ids = "";
            for (int i = 1; i < rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                if (columnName.equalsIgnoreCase("id") || columnName.substring(columnName.length() - 2, columnName.length()).equalsIgnoreCase("id")) {
                    ids += columnName + ",";
                }
            }
            ids = ids.substring(0, ids.length() - 1);
            rs.close();
            stmt.close();

            String query = "";
            if (propertyNames == null || propertyNames.length == 0) {
                //simple select, no where statement
                query = SQL_SELECT;
                query = query.replace(TABLENAME_PLACEHOLDER, storageName).replace(IDS_PLACEHOLDER, ids);
            }
            else {
                //select with where statement verifying property values
                String whereString = getWhereString(propertyNames, propertyValues);
                query = SQL_SELECT_WHERE;
                query = query.replace(TABLENAME_PLACEHOLDER, storageName).replace(IDS_PLACEHOLDER, ids);
                query = query.replace(WHERE_PLACEHOLDER, whereString);
            }


            int rows = 1;
            stmt = dbConnection.createStatement();
            if (maxRows > 0) {
                stmt.setMaxRows(maxRows);
                rows = maxRows;
            }
            else {
                String countQuery = query.substring(0, query.indexOf("*")) + "count(*)" + query.substring(query.indexOf("*")+1);
                countQuery = countQuery.substring(0, countQuery.indexOf("ORDER BY") - 1);
                rs = stmt.executeQuery(countQuery);
                rs.next();
                rows = rs.getInt(1);
                rs.close();
                stmt.close();
                stmt = dbConnection.createStatement();
            }
            result = new Object[rows][];

            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            int i = 0;
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    Object o = rs.getObject(j+1);
                    if (o instanceof java.sql.Blob) {
                        //row.add(((java.sql.Blob)o).getBytes(1, (int)((java.sql.Blob)o).length()));
                        row[j] = ((java.sql.Blob)o).getBytes(1, (int)((java.sql.Blob)o).length());
                    }
                    else {
                        row[j] = o;
                    }
                }
                result[i] = row;
                i++;
            }
            return result;
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Failed to get data from {0}", storageName);
        }
        return null;
    }

    @Override
	public boolean insertData(String storageName, List<Object> data) {
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT_NOORDER.replaceFirst(TABLENAME_PLACEHOLDER, storageName));
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            
            //id is inserted automatically if it is not in "data"
            int id = -1;
            if (columnCount > data.size()) {
                rs = stmt.executeQuery(SQL_MAXID.replaceFirst(TABLENAME_PLACEHOLDER, storageName));
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                id++;
                data.add(0, id);
            }
            else {
                id = ((Integer)data.get(0)).intValue();
            }

            String insertString = SQL_INSERT;
            insertString = insertString.replaceFirst(TABLENAME_PLACEHOLDER, storageName);
            insertString = insertString.replaceFirst(VALUES_PLACEHOLDER, getPlaceholders(columnCount));
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertString);

            //preparedStatement.setInt(1, id);
            for (int i = 0; i < data.size(); i++) {
                preparedStatement.setObject(i+1, data.get(i));
            }
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Failed to insert data into {0}", storageName);
        }

        return false;
    }

    @Override
	public boolean batchInsertData(String storageName, ArrayList<Object[]> data) {
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT_NOORDER.replaceFirst(TABLENAME_PLACEHOLDER, storageName));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            dbConnection.setAutoCommit(false);

            String insertString = SQL_INSERT;
            insertString = insertString.replaceFirst(TABLENAME_PLACEHOLDER, storageName);
            insertString = insertString.replaceFirst(VALUES_PLACEHOLDER, getPlaceholders(columnCount));
            PreparedStatement prepStmt = dbConnection.prepareStatement(insertString);

            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).length; j++) {
                    prepStmt.setObject(j+1, data.get(i)[j]);
                }
                prepStmt.addBatch();
            }

            int[] result = prepStmt.executeBatch();
            dbConnection.commit();
            dbConnection.setAutoCommit(true);
            boolean error = false;
            for (int i = 0; i < result.length; i++) {
                if (result[i] == Statement.EXECUTE_FAILED) error = true;
            }
            
            return !error;
        }
        catch (Exception e) {
            /*
            e.printStackTrace();
            while ((e = e.getNextException()) != null)
                e.printStackTrace();
             */
            logger.log(Level.WARNING, "Batch insert into {0} failed", storageName);
        }

        return false;
    }

    @Override
	public boolean deleteData(String storageName, Object[] propertyNames, Object[] propertyValues) {
        try {
            String query = SQL_DELETE;
            String whereString = getWhereString(propertyNames, propertyValues);
            query = query.replace(TABLENAME_PLACEHOLDER, storageName);
            query = query.replace(WHERE_PLACEHOLDER, whereString);

            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate(query);
            return true;
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Delete from {0} failed", storageName);
        }
        return false;
    }

    @Override
	public boolean batchDeleteData(String storageName, String propertyName, ArrayList<Object> propertyValues) {
        //new batch delete implementation checking only a single property against a list of values
        //-> single delete statement (much faster than multiple deletes due to java derby db performance bug when deleting rows containing BLOB)
        if (propertyValues.isEmpty()) return true;
        try {
            String deleteString = SQL_DELETE;
            deleteString = deleteString.replace(TABLENAME_PLACEHOLDER, storageName);
            String whereString = propertyName + " IN (" + getPlaceholders(propertyValues.size())+")";
            deleteString = deleteString.replace(WHERE_PLACEHOLDER, whereString);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteString);

            //preparedStatement.setInt(1, id);
            for (int i = 0; i < propertyValues.size(); i++) {
                preparedStatement.setObject(i+1, propertyValues.get(i));
            }
            preparedStatement.execute();
            preparedStatement.close();
            
            return true;
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Batch delete from {0} failed", storageName);
        }
        return false;
    }
/*
    public boolean batchDeleteData(String storageName, ArrayList<Object[]> propertyNames, ArrayList<Object[]> propertyValues) {
        try {
            String query = "";
            String whereString = "";

            Statement stmt = dbConnection.createStatement();

            for (int i = 0; i < propertyNames.size(); i++) {
                query = SQL_DELETE;
                whereString = getWhereString(propertyNames.get(i), propertyValues.get(i));
                query = query.replaceFirst(TABLENAME_PLACEHOLDER, storageName);
                query = query.replaceFirst(WHERE_PLACEHOLDER, whereString);
                stmt.addBatch(query);
            }

            int[] result = stmt.executeBatch();
            dbConnection.commit();
            dbConnection.setAutoCommit(true);
            boolean error = false;
            for (int i = 0; i < result.length; i++) {
                if (result[i] == Statement.EXECUTE_FAILED) error = true;
            }

            return !error;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
*/
    private String getWhereString(Object[] propertyNames, Object[] propertyValues) {
            String whereString = "";
            for (int i = 0; i < propertyNames.length; i++) {
                if (!(propertyValues[i] instanceof int[])) {
                    String apostrophe = "'";
                    if (propertyValues[i] instanceof Integer || propertyValues[i] instanceof Double
                            && !Double.isNaN((Double)propertyValues[i])) apostrophe = "";
                    whereString += propertyNames[i]+"="+apostrophe+propertyValues[i]+apostrophe;
                }
                else if (((int[])propertyValues[i]).length == 2) {
                    int[] boundaries = (int[])propertyValues[i];
                    whereString += propertyNames[i] + ">=" + boundaries[0] + " AND " + propertyNames[i] + "<=" + boundaries[1];
                }
                if (i < propertyNames.length - 1)
                    whereString += " AND ";
            }
            return whereString;
    }

    public String getValuesString(Object[] values) {
        String valuesString = "";
        for (int i = 0; i < values.length; i++) {
            String apostrophe = "'";
            if (values[i] instanceof Integer || values[i] instanceof Double
                    && !Double.isNaN((Double)values[i])) apostrophe = "";
            valuesString += apostrophe+values[i].toString()+apostrophe;
        }
        return valuesString;
    }

    private String getPlaceholders(int count) {
        String result = "";
        for (int i = 0; i < count; i++) result += "?,";
        if (result.length() > 0) return result.substring(0, result.length() - 1);
        else return "";
    }
}
