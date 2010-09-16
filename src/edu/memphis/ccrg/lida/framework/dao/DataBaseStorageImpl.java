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

    private static final String SQL_SELECT_NOORDER = "SELECT * FROM APP."+TABLENAME_PLACEHOLDER;
    private static final String SQL_SELECT = "SELECT * FROM APP."+TABLENAME_PLACEHOLDER+" ORDER BY "+IDS_PLACEHOLDER+" DESC";
    //private static final String SQL_LIDAIDSELECT = "SELECT * FROM APP."+TABLENAME_PLACEHOLDER+" WHERE  ORDER BY ID, LIDAID DESC";
    private static final String SQL_MAXID = "SELECT max(id) FROM APP."+TABLENAME_PLACEHOLDER;
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
        ArrayList<Object[]> data = getData(storageName, 1);
        return data.get(0);
    }

    public Object[] getDataRow(String storageName, ArrayList propertyNames, ArrayList propertyValues) {
        ArrayList<Object[]> data = getData(storageName, 1);
        return data.get(0);
    }

    private ArrayList<Object[]> getData(String storageName, int maxRows) {
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

            stmt = dbConnection.createStatement();
            if (maxRows > 0) stmt.setMaxRows(maxRows);
            String query = SQL_SELECT.replaceFirst(TABLENAME_PLACEHOLDER, storageName).replace(IDS_PLACEHOLDER, ids);
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
            }
            else {
                id = ((Integer)data.get(0)).intValue();
            }

            String insertStatement = SQL_INSERT;
            insertStatement = insertStatement.replaceFirst(TABLENAME_PLACEHOLDER, storageName);
            insertStatement = insertStatement.replaceFirst(VALUES_PLACEHOLDER, getPlaceholders(columnCount));
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertStatement);

            preparedStatement.setInt(1, id);
            for (int i = 0; i < data.size(); i++) {
                preparedStatement.setObject(i+2, data.get(i));
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

    private String getPlaceholders(int count) {
        String result = "";
        for (int i = 0; i < count; i++) result += "?,";
        return result.substring(0, result.length() - 1);
    }
}
