/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.util.Serializer;
import edu.memphis.ccrg.lida.util.Deserializer;
import java.util.ArrayList;
import java.util.Arrays;

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
        //TODO proper update? (this save method deletes all old data for the current lidaId and then adds new data)
        storage.deleteData(
                storageName,
                new Object[] {"lidaid"},
                new Object[] {lidaId});
        
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
        return loadFromRow(
                storage.getDataRow(
                    storageName,
                    new Object[] {"lidaid"},
                    new Object[] {lidaId}
                ));
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
