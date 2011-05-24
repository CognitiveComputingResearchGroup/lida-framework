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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

//TODO move to DAO?
/**
 *
 * @author Tom
 */
public class Deserializer {
    public static Object getObject(byte[] byteArray) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            Object obj = in.readObject();
            in.close();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
