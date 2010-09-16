/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import java.util.ArrayList;

/**
 *
 * @author Tom
 */
public interface Storage {
    public boolean open();
    public boolean close();
    public boolean insertData(String storageName, ArrayList<Object> data);
    public ArrayList<Object[]> getData(String storageName, int maxRows);
    public Object[] getDataRow(String storageName);
}
