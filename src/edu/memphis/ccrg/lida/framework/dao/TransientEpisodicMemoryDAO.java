/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.util.VectorConverter;
import edu.memphis.ccrg.lida.util.VectorConverter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Tom
 */

public class TransientEpisodicMemoryDAO extends DataAccessObjectImpl {
    public static final String STORAGE_NAME = "transientepisodicmemory";
    public static final String ADDRESSSTORAGE_NAME = "tem_addresses";
    public static final String COUNTERSTORAGE_NAME = "tem_counters";

    public static final int DB_ADDRESS_INDEX = 2;
    public static final int DB_COUNTER_INDEX = 1;

    public TransientEpisodicMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        super(module, cStorage, STORAGE_NAME, cLidaId);
    }

    //@override
    public boolean save() {
        boolean success = true;

        //TODO delete all old data for the current lidaId
        try {
            /*
            ArrayList<Object[]> oldAddresses = storage.getData(
                    ADDRESSSTORAGE_NAME,
                    new ArrayList(Arrays.asList("lidaid")),
                    new ArrayList(Arrays.asList(lidaId)));
            storage.deleteData(
                    ADDRESSSTORAGE_NAME,
                    new ArrayList(Arrays.asList("lidaid")),
                    new ArrayList(Arrays.asList(lidaId)));
            for (int i = 0; i < oldAddresses.size(); i++) {
                int addressId = (Integer)oldAddresses.get(i)[0];
                storage.deleteData(
                        COUNTERSTORAGE_NAME,
                        new ArrayList(Arrays.asList("addressid")),
                        new ArrayList(Arrays.asList(addressId)));
            }*/
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        Object content = ((Saveable)module).getState();
        if (content instanceof Object[]) {
            try {
                Object[] state = (Object[])content;
                BitVector[] addresses = (BitVector[])state[0];
                byte[][] counters = (byte[][])state[1];

                Object[] lastRow = storage.getDataRow(ADDRESSSTORAGE_NAME);
                int lastAddressId = 0;
                if (lastRow != null && lastRow.length > 0)
                    lastAddressId = ((Integer)lastRow[0]).intValue();
                lastRow = storage.getDataRow(COUNTERSTORAGE_NAME);
                int lastCounterId = 0;
                if (lastRow != null && lastRow.length > 0)
                    lastCounterId = ((Integer)lastRow[0]).intValue();

                ArrayList<Object[]> data = new ArrayList<Object[]>();
                int batchSize = 1000;
                for (int i = 0; i < addresses.length; i++) {
                    Object[] row = new Object[3];
                    row[0] = lastAddressId + i + 1;
                    row[1] = lidaId;
                    row[2] = VectorConverter.toByteArray(addresses[i]);
                    data.add(row);
                    if (i > 0 && i % batchSize == 0) {
                        success = success && storage.batchInsertData(ADDRESSSTORAGE_NAME, data);
                        data = new ArrayList<Object[]>();
                    }
                }
                if (data.size() > 0)
                    success = success && storage.batchInsertData(ADDRESSSTORAGE_NAME, data);
                data = new ArrayList<Object[]>();
                for (int i = 0; i < counters.length; i++) {
                    Object[] row = new Object[3];
                    row[0] = lastCounterId + i + 1;
                    row[1] = lastAddressId + i + 1;
                    row[2] = counters[i];
                    data.add(row);
                    if (i > 0 && i % batchSize == 0) {
                        success = success && storage.batchInsertData(COUNTERSTORAGE_NAME, data);
                        data = new ArrayList<Object[]>();
                    }
                }
                if (data.size() > 0)
                    success = success && storage.batchInsertData(COUNTERSTORAGE_NAME, data);
                data = new ArrayList<Object[]>();
                /* slow single insert code
                ArrayList data = new ArrayList();
                for (int i = 0; i < addresses.length; i++) {
                    data = new ArrayList();
                    data.add(lastAddressId + i + 1);
                    data.add(lidaId);
                    data.add(VectorConverter.toByteArray(addresses[i]));
                    success = success && storage.insertData(ADDRESSSTORAGE_NAME, data);

                    data = new ArrayList();
                    data.add(lastAddressId + i + 1);
                    data.add(counters[i]);
                    success = success && storage.insertData(COUNTERSTORAGE_NAME, data);
                }*/
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    //@override
    public boolean load(int lidaId) {
        BitVector[] bAddresses;
        byte[][] bCounters;
        byte[] cAddress;

        ArrayList<Object[]> addresses = storage.getData(
                ADDRESSSTORAGE_NAME,
                new ArrayList(Arrays.asList("lidaid")),
                new ArrayList(Arrays.asList(lidaId)));

        bAddresses = new BitVector[addresses.size()];
        bCounters = new byte[addresses.size()][];

        for (int i = 0; i < addresses.size(); i++) {
            cAddress = (byte[])addresses.get(i)[DB_ADDRESS_INDEX];
            bAddresses[i] = VectorConverter.fromByteArray(cAddress);

            int addressId = (Integer)addresses.get(i)[0];
            Object[] counters = storage.getDataRow(
                COUNTERSTORAGE_NAME,
                new ArrayList(Arrays.asList("addressid")),
                new ArrayList(Arrays.asList(addressId)));

            bCounters[i] = (byte[])counters[DB_COUNTER_INDEX];
        }

        Object[] state = new Object[2];
        state[0] = bAddresses;
        state[1] = bCounters;

        return ((Saveable)module).setState(state);
    }
    //@override
    public boolean load() {
        return load(lidaId);
    }
}
