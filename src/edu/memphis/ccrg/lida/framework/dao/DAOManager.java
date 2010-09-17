package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.transientepisodicmemory.TemImpl;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 *
 * @author Tom
 */
public class DAOManager implements DataAccessObject {
    public static final String LIDA_STORAGE_NAME = "lida";
    public static int TEM_WORD_LENGTH = TemImpl.DEF_WORD_LENGTH;

    private static DAOManager instance = null;
    private ArrayList<DataAccessObject> daos;
    private boolean initialized = false;

    public Storage storage = null;

    private DAOManager() {
        daos = new ArrayList<DataAccessObject>();
    }
    public static DAOManager getInstance() {
        if (instance == null) instance = new DAOManager();
        return instance;
    }

    public boolean initDataAccessObjects(Lida lida) {
        boolean success = true;

        storage = DataBaseStorageImpl.getInstance();
        success = storage.open();

        // TODO ask user for lida name
        String lidaName = "LIDA";
        int cLidaId = 0;

        ArrayList<Object> data = new ArrayList<Object>();
        data.add(lidaName);
        storage.insertData(LIDA_STORAGE_NAME, data);
        Object[] row = storage.getDataRow(LIDA_STORAGE_NAME);
        cLidaId = (Integer)row[0];

        TEM_WORD_LENGTH = (Integer)lida.getParam("tem.wordLength",TemImpl.DEF_WORD_LENGTH);

        for (ModuleName name : ModuleName.values()) {
            LidaModule module = lida.getSubmodule(name);
            if (module != null && module.getModuleName() == name) {
                System.out.println(module.getModuleName());
                Class daoClass = null;
                try {
                    String className = "edu.memphis.ccrg.lida.framework.dao."+name.toString()+"DAO";
                    daoClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                }
                if (daoClass != null) {
                    try {
                        Constructor daoConstructor = daoClass.getConstructor(
                                new Class[] {LidaModule.class, Storage.class, int.class}
                        );
                        DataAccessObject dao = (DataAccessObject)daoConstructor.newInstance(
                                new Object[] {module, storage, cLidaId}
                        );
                        daos.add(dao);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        success = false;
                    }
                }
            }
        }
        if (success) initialized = true;
        return success;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean save() {
        boolean success = true;
        for (DataAccessObject dao : daos) {
            if (!dao.save()) success = false;
        }
        return success;
    }
    public boolean load() {
        //TODO ask or search for lida id
        boolean success = true;
        for (DataAccessObject dao : daos) {
            if (!dao.load()) success = false;
        }
        return success;
    }
    public boolean load(int lidaId) {
        boolean success = true;
        for (DataAccessObject dao : daos) {
            if (!dao.load(lidaId)) success = false;
        }
        return success;
    }
    public boolean delete() {
        boolean success = true;
        for (DataAccessObject dao : daos) {
            if (!dao.delete()) success = false;
        }
        return success;
    }
}
