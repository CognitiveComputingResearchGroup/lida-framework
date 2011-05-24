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

/**
 *
 * @author Tom
 */
public interface Storage {
    public boolean open();
    public boolean close();
    public boolean insertData(String storageName, List<Object> data);
    public boolean batchInsertData(String storageName, ArrayList<Object[]> data);
    public Object[][] getData(String storageName, int maxRows);
    public Object[][] getData(String storageName, Object[] propertyNames, Object[] propertyValues);
    public Object[][] getData(String storageName, Object[] propertyNames, Object[] propertyValues, int maxRows);
    public Object[] getDataRow(String storageName);
    public Object[] getDataRow(String storageName, Object[] propertyNames, Object[] propertyValues);
    public boolean deleteData(String storageName, Object[] propertyNames, Object[] propertyValues);
    //public boolean batchDeleteData(String storageName, ArrayList<Object[]> propertyNames, ArrayList<Object[]> propertyValues);
    //reason: performance problem (see Impl)
    public boolean batchDeleteData(String storageName, String propertyName, ArrayList<Object> propertyValues);
}
