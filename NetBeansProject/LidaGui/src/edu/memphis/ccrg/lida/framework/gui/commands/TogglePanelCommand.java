/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import java.util.Map;

public class TogglePanelCommand implements Command {

	public void execute(Lida lida) {

	}


    public void setParameter(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getParameter(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setParameters(Map<String, Object> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
