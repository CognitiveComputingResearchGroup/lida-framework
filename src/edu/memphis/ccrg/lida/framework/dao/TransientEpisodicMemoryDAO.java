/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.util.Deserializer;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.util.VectorConverter;
import java.util.ArrayList;

/**
 *
 * @author Tom
 */

// TODO everything
public class TransientEpisodicMemoryDAO extends DataAccessObjectImpl {
    public static final String STORAGE_NAME = "transientepisodicmemory";
    public static final String ADDRESSSTORAGE_NAME = "tem_addresses";
    public static final String COUNTERSTORAGE_NAME = "tem_counters";

    public TransientEpisodicMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        super(module, cStorage, STORAGE_NAME, cLidaId);
    }

    //@override
    public boolean save() {
        boolean success = true;
/*
        ArrayList data = new ArrayList();
        Object content = ((Saveable)module).getState();
        if (content instanceof Object[]) {
            try {
                Object[] state = (Object[])content;
                BitVector[] addresses = (BitVector[])state[0];
                byte[][] counters = (byte[][])state[1];

                Object[] lastRow = storage.getDataRow(ADDRESSSTORAGE_NAME);
                int lastAddressId = (Integer)lastRow[0];
                        
                for (int i = 0; i < addresses.length; i++) {
                    data = new ArrayList();
                    data.add(lastAddressId + i + 1);
                    data.add(lidaId);
                    data.add(VectorConverter.toByteArray(addresses[i]));
                    success = success && storage.insertData(ADDRESSSTORAGE_NAME, data);
                    for (int j = 0; j < counters.length; j++) {
                        data = new ArrayList();
                        data.add(lastAddressId + i + 1);
                        data.add(counters[j]);
                        success = success && storage.insertData(COUNTERSTORAGE_NAME, data);
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
*/
        return success;
    }

    //@override
    public boolean load(int lidaId) {
        return false;
    }
    //@override
    public boolean load() {
        /*
        Object[] row = storage.getDataRow(STORAGE_NAME);
        
        byte[] sdata = (byte[])row[SDATA_INDEX];
        if (sdata != null && sdata.length > 0) {
            Object obj = Deserializer.getObject(sdata);
            ((Saveable)module).setState(obj);
            return true;
        }
*/
        return false;
    }
}
