/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.util.VectorConverter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom
 */

public class TransientEpisodicMemoryDAO extends DataAccessObjectImpl {
    private static Logger logger = Logger.getLogger("edu.memphis.ccrg.lida.framework.dao.TransientEpisodicMemoryDAO");

    public static final String STORAGE_NAME = "transientepisodicmemory";
    public static final String ADDRESSSTORAGE_NAME = "tem_addresses";
    public static final String COUNTERSTORAGE_NAME = "tem_counters";

    public static final int DB_ADDRESS_INDEX = 2;
    public static final int DB_COUNTER_INDEX = 2;

    public TransientEpisodicMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        super(module, cStorage, STORAGE_NAME, cLidaId);
    }

    @Override
    public boolean save() {
        boolean success = true;

        //TODO proper update? (this save method deletes all old data for the current lidaId and then adds new data)
        try {
            Object[][] oldAddresses = storage.getData(
                    ADDRESSSTORAGE_NAME,
                    new Object[] {"lidaid"},
                    new Object[] {lidaId});
            success = success && storage.deleteData(
                    ADDRESSSTORAGE_NAME,
                    new Object[] {"lidaid"},
                    new Object[] {lidaId});

            ArrayList<Object> propertyValues = new ArrayList<Object>();
            for (int i = 0; i < oldAddresses.length; i++) {
                int addressId = (Integer)oldAddresses[i][0];
                propertyValues.add(addressId);
            }
            success = success && storage.batchDeleteData(COUNTERSTORAGE_NAME, "addressid", propertyValues);
        }
        catch (Exception ex) {
            success = false;
            logger.log(Level.WARNING, "Save: deleting old SDM entries failed");
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
            }
            catch (Exception ex) {
                logger.log(Level.WARNING, "Save: inserting new SDM entries failed");
            }
        }

        return success;
    }

    @Override
    public boolean load(int lidaId) {
        BitVector[] bAddresses;
        byte[][] bCounters;
        byte[] cAddress;

        try {
            Object[][] addresses = storage.getData(
                    ADDRESSSTORAGE_NAME,
                    new Object[] {"lidaid"},
                    new Object[] {lidaId});

            bAddresses = new BitVector[addresses.length];
            bCounters = new byte[addresses.length][];

            Object[][] counters = storage.getData(
                        COUNTERSTORAGE_NAME,
                        new Object[] {"addressid"},
                        new Object[] {new int[] {0, addresses.length}});

            for (int i = 0; i < addresses.length; i++) {
                if (addresses[i] != null) {
                    cAddress = (byte[])addresses[i][DB_ADDRESS_INDEX];
                    bAddresses[i] = VectorConverter.fromByteArray(cAddress);

                    Object[] counter = counters[i];
                    bCounters[i] = (byte[])counter[DB_COUNTER_INDEX];
                }
            }
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Load: loading new SDM entries failed");
            return false;
        }

        Object[] state = new Object[2];
        state[0] = bAddresses;
        state[1] = bCounters;

        return ((Saveable)module).setState(state);
    }
    @Override
    public boolean load() {
        return load(lidaId);
    }
}
