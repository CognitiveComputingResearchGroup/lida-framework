/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.util.Serializer;
import edu.memphis.ccrg.lida.util.Deserializer;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.pam.PamNodeStructure;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import java.util.ArrayList;

/**
 *
 * @author Tom
 */
public class ProceduralMemoryDAO implements DataAccessObject {
    public static final String STORAGE_NAME = "proceduralmemory";
    public static int SDATA_INDEX = 2;

    private ProceduralMemory proceduralMemory;
    private Storage storage = null;
    private int lidaId = 0;

    public ProceduralMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        proceduralMemory = (ProceduralMemory)module;
        storage = cStorage;
        lidaId = cLidaId;
    }
    
    public boolean save() {
        ArrayList data = new ArrayList();
        data.add(lidaId);
        data.add(Serializer.getBytes(proceduralMemory.getState()));
        boolean success = storage.insertData(STORAGE_NAME, data);

        return success;
    }
    public boolean load() {
        Object[] row = storage.getDataRow(STORAGE_NAME);
        
        byte[] sdata = (byte[])row[SDATA_INDEX];
        if (sdata != null && sdata.length > 0) {
            Object obj = Deserializer.getObject(sdata);
            proceduralMemory.setState(obj);
            return true;
        }

        return false;
    }
    public boolean delete() {return false;}
}
