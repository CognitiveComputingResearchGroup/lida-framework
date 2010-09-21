/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.gui.commands;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 *
 * @author Javier Snaider
 */
public interface Command {
    public void setParameter(String name, Object value);
    public Object getParameter(String name);
    public Object getResult();
    public void execute(Lida lida);
    public void setParameters(Map<String, Object> parameters);
}
