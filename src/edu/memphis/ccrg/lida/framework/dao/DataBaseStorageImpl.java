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

    private static final String SQL_SELECT = "SELECT * FROM APP.tablename ORDER BY ID, LIDAID DESC";
    private static final String SQL_MAXID = "SELECT max(id) FROM APP.tablename";
    private static final String SQL_INSERT = "INSERT INTO APP.tablename VALUES(placeholders)";
    
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
        ArrayList<Object[]> data = getData(storageName, -1);
        return data.get(0);
    }

    public ArrayList<Object[]> getData(String storageName, int maxRows) {
        if (!connected) return null;
        ArrayList result = new ArrayList();
        try {
            Statement stmt = dbConnection.createStatement();
            if (maxRows > 0) stmt.setMaxRows(maxRows);
            ResultSet rs = stmt.executeQuery(SQL_SELECT.replaceFirst("tablename", storageName));

            ResultSetMetaData rsmd = rs.getMetaData();
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
            //id is inserted automatically
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT.replaceFirst("tablename", storageName));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            rs = stmt.executeQuery(SQL_MAXID.replaceFirst("tablename", storageName));
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            id++;

            String insertStatement = SQL_INSERT;
            insertStatement = insertStatement.replaceFirst("tablename", storageName);
            insertStatement = insertStatement.replaceFirst("placeholders", getPlaceholders(columnCount));
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
