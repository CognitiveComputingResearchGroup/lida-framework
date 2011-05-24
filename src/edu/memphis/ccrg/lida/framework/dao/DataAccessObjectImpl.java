/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkModule;

/**
 *
 * @author Tom
 */
public class DataAccessObjectImpl implements DataAccessObject {
    public static int SDATA_INDEX = 2;

    protected FrameworkModule module;
    protected Storage storage = null;
    public String storageName = "unknown_module";
    protected int lidaId = 0;

    public DataAccessObjectImpl(FrameworkModule newmodule, Storage cStorage, String cStorageName, int cLidaId) {
        module = newmodule;
        storage = cStorage;
        lidaId = cLidaId;
        storageName = cStorageName;
    }

    @Override
	public boolean save() {
        //TODO proper update? (this save method deletes all old data for the current lidaId and then adds new data)
        storage.deleteData(
                storageName,
                new Object[] {"lidaid"},
                new Object[] {lidaId});
        
        List<Object> data = new ArrayList<Object>();
        data.add(lidaId);
        data.add(Serializer.getBytes(((Saveable)module).getState()));
        boolean success = storage.insertData(storageName, data);

        return success;
    }

    @Override
	public boolean load() {
        return loadFromRow(storage.getDataRow(storageName));
    }
    @Override
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

    @Override
	public boolean delete() {return false;}
}
