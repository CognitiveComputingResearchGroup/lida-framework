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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Tom
 */
public class DataBaseStorageImpl implements Storage {
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
    private static final String SQL_INSERT = "INSERT INTO APP."+TABLENAME_PLACEHOLDER+" VALUES("+VALUES_PLACEHOLDER+")";
    private static final String SQL_DELETE = "DELETE FROM APP."+TABLENAME_PLACEHOLDER+" WHERE "+WHERE_PLACEHOLDER;
    
    private boolean connected = false;
    private Connection dbConnection = null;

    private static DataBaseStorageImpl instance = null;

    private DataBaseStorageImpl() {
    }

    public static Storage getInstance() {
        if (instance == null) instance = new DataBaseStorageImpl();
        return instance;
    }

    public boolean open() {
        if (connected) return false;
        
        try {
            dbConnection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            if (!dbConnection.isClosed()) {
                connected = true;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean close() {
        if (!connected) return false;

        try {
            dbConnection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Object[] getDataRow(String storageName) {
        ArrayList<Object[]> data = getData(storageName, new ArrayList(), new ArrayList(), 1);
        return (data.size() > 0 ? data.get(0) : null);
    }

    public Object[] getDataRow(String storageName, ArrayList propertyNames, ArrayList propertyValues) {
        ArrayList<Object[]> data = getData(storageName, propertyNames, propertyValues, 1);
        return (data.size() > 0 ? data.get(0) : null);
    }

    public ArrayList<Object[]> getData(String storageName, int maxRows) {
        return getData(storageName, new ArrayList(), new ArrayList(), maxRows);
    }

    public ArrayList<Object[]> getData(String storageName, ArrayList propertyNames, ArrayList propertyValues) {
        return getData(storageName, propertyNames, propertyValues, -1);
    }

    public ArrayList<Object[]> getData(String storageName, ArrayList propertyNames, ArrayList propertyValues, int maxRows) {
        if (!connected) return null;
        ArrayList result = new ArrayList();
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

            String query = "";
            if (propertyNames == null || propertyNames.isEmpty()) {
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


            stmt = dbConnection.createStatement();
            if (maxRows > 0) stmt.setMaxRows(maxRows);
            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            ArrayList row = new ArrayList();
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    Object o = rs.getObject(i+1);
                    if (o instanceof java.sql.Blob) {
                        row.add(((java.sql.Blob)o).getBytes(1, (int)((java.sql.Blob)o).length()));
                    }
                    else {
                        row.add(o);
                    }
                }
                result.add(row.toArray());
            }
            return result;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertData(String storageName, ArrayList<Object> data) {
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

            String insertStatement = SQL_INSERT;
            insertStatement = insertStatement.replaceFirst(TABLENAME_PLACEHOLDER, storageName);
            insertStatement = insertStatement.replaceFirst(VALUES_PLACEHOLDER, getPlaceholders(columnCount));
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertStatement);

            //preparedStatement.setInt(1, id);
            for (int i = 0; i < data.size(); i++) {
                preparedStatement.setObject(i+1, data.get(i));
            }
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteData(String storageName, ArrayList propertyNames, ArrayList propertyValues) {
        try {
            String query = SQL_DELETE;
            String whereString = getWhereString(propertyNames, propertyValues);
            query = query.replace(TABLENAME_PLACEHOLDER, storageName);
            query = query.replace(WHERE_PLACEHOLDER, whereString);

            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate(query);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getWhereString(ArrayList propertyNames, ArrayList propertyValues) {
            String whereString = "";
            for (int i = 0; i < propertyNames.size(); i++) {
                String apostrophe = "'";
                if (propertyValues.get(i) instanceof Integer || propertyValues.get(i) instanceof Double
                        && !Double.isNaN((Double)propertyValues.get(i))) apostrophe = "";
                if (i < propertyNames.size() - 1)
                    whereString += propertyNames.get(i)+"="+apostrophe+propertyValues.get(i)+apostrophe+" AND";
                else
                    whereString += propertyNames.get(i)+"="+apostrophe+propertyValues.get(i)+apostrophe;
            }
            return whereString;
    }

    private String getPlaceholders(int count) {
        String result = "";
        for (int i = 0; i < count; i++) result += "?,";
        return result.substring(0, result.length() - 1);
    }
}
