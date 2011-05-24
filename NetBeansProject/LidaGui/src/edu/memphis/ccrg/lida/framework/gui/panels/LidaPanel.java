/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 *
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import javax.swing.JPanel;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiController;

/**
 *  Lida Gui Panel
 * @author Javier Snaider
 *
 */
public interface LidaPanel {
	public void registerGuiController(FrameworkGuiController lgc);
	public void registerLida(Lida lida);
	public void display (Object o);
	public void refresh();
	public JPanel getPanel();
    public Module getSupportedModule();
    public void setSupportedModule(Module module);
    public void setName(String name);
    public String getName();
    public void initPanel(String[] param);
}

