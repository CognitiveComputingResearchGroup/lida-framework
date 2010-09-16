/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.util.Serializer;
import edu.memphis.ccrg.lida.util.Deserializer;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Tom
 */
public class SensoryMemoryDAO implements DataAccessObject {
    public static final String STORAGE_NAME = "sensorymemory";
    public static int SDATA_INDEX = 2;

    private SensoryMemory sensoryMemory;
    private Storage storage = null;
    private int lidaId = 0;

    public SensoryMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        sensoryMemory = (SensoryMemory)module;
        storage = cStorage;
        lidaId = cLidaId;
    }
    
    public boolean save() {
        ArrayList data = new ArrayList();
        data.add(lidaId);
        data.add(Serializer.getBytes(sensoryMemory.getState()));
        boolean success = storage.insertData(STORAGE_NAME, data);

        return success;
    }
    public boolean load() {
        Object[] row = storage.getDataRow(STORAGE_NAME);
        
        byte[] sdata = (byte[])row[SDATA_INDEX];
        if (sdata != null && sdata.length > 0) {
            Object obj = Deserializer.getObject(sdata);
            sensoryMemory.setState(obj);
            return true;
        }

        return false;
    }
    public boolean delete() {return false;}
}
