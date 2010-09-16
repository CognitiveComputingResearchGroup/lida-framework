/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.util.Serializer;
import edu.memphis.ccrg.lida.util.Deserializer;
import java.util.ArrayList;

/**
 *
 * @author Tom
 */
public class DataAccessObjectImpl implements DataAccessObject {
    public static int SDATA_INDEX = 2;

    protected LidaModule module;
    protected Storage storage = null;
    public String storageName = "unknown_module";
    protected int lidaId = 0;

    public DataAccessObjectImpl(LidaModule newmodule, Storage cStorage, String cStorageName, int cLidaId) {
        module = newmodule;
        storage = cStorage;
        lidaId = cLidaId;
        storageName = cStorageName;
    }

    public boolean save() {
        ArrayList data = new ArrayList();
        data.add(lidaId);
        data.add(Serializer.getBytes(((Saveable)module).getState()));
        boolean success = storage.insertData(storageName, data);

        return success;
    }

    public boolean load() {
        return loadFromRow(storage.getDataRow(storageName));
    }
    public boolean load(int lidaId) {
        ArrayList propertyNames = new ArrayList();
        propertyNames.add("lidaid");
        ArrayList propertyValues = new ArrayList();
        propertyValues.add(lidaId);
        return loadFromRow(storage.getDataRow(storageName, propertyNames, propertyValues));
    }

    private boolean loadFromRow(Object[] row) {
        byte[] sdata = (byte[])row[SDATA_INDEX];
        if (sdata != null && sdata.length > 0) {
            Object obj = Deserializer.getObject(sdata);
            ((Saveable)module).setState(obj);
            return true;
        }

        return false;
    }

    public boolean delete() {return false;}
}
