/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class AddPanelCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		java.awt.Container parent = (java.awt.Container)getParameter("parent");
		javax.swing.JPanel panel = (javax.swing.JPanel)getParameter("panel");
		
		if (parent != null) {
			boolean panelFound = false;
			for (java.awt.Component c : parent.getComponents()) {
				if (c.equals(panel)) {
					panelFound = true;
					//panel exists, remove it
					parent.remove(panel);
					break;
				}
			}
			if (!panelFound) {
				//panel doesn't exist, add it
				parent.add(panel);
			}
			parent.repaint();
		}
	}

}
