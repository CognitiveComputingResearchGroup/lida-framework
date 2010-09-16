package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 *
 * @author Tom
 */
public class DAOManager implements DataAccessObject {
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

        // TODO change to LIDA ID (e.g. autoincremented or ask user) if multiple LIDA instances should be saved/loaded
        int cLidaId = 0;

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
        boolean success = true;
        for (DataAccessObject dao : daos) {
            if (!dao.load()) success = false;
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
