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
 * A command is an encapsulation of an event from the Gui such as a button press or a 
 * slider state change.  
 * @author Javier Snaider
 */
public interface Command {

    /**
     * Executes this command performing the necessary steps in the model (Lida)
     * @param lida
     */
    public void execute(Lida lida);
    
    /**
     * Expected result of the command
     */
    public Object getResult();    

    /**
     * Set multiple parameters using a map
     * @param parameters
     */
    public void setParameters(Map<String, Object> parameters);
    
    /**
     * Set single parameter
     * @param name
     * @param value
     */
    public void setParameter(String name, Object value);
    
    /**
     * Get a parameter
     * @param name
     */
    public Object getParameter(String name);
    
    
}
