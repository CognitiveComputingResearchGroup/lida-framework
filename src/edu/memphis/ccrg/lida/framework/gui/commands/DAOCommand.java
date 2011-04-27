/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.dao.DAOManager;

public class DAOCommand extends CommandImpl {
        public static String LOAD_ACTION = "load";
        public static String SAVE_ACTION = "save";

        public static String SUCCESS_STRING = "successful";
        public static String FAILED_STRING = "failed";

	@Override
	public void execute(Agent agent) {
		String action = getParameter("action").toString().toLowerCase();
                boolean success = false;
                String displayString;
		if (action.equals(SAVE_ACTION)) {
                    success = DAOManager.getInstance().save();
                }
                else if (action.equals(LOAD_ACTION)) {
                    success = DAOManager.getInstance().load();
                }
                if (success) displayString = action + " " + SUCCESS_STRING + ".";
                else displayString = action + " " + FAILED_STRING + ".";
                javax.swing.JOptionPane.showMessageDialog(null, displayString);
	}

}
