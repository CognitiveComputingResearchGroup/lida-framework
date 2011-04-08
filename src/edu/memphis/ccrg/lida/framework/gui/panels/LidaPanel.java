/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
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
import edu.memphis.ccrg.lida.framework.gui.LidaGui;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;

/**
 * A GUI Panel which can be displayed in the {@link LidaGui}
 * 
 * @author Javier Snaider
 */
public interface LidaPanel {
	
    /**
     * Initializes panel
     * @param param Parameters to initialize with.
     */
    public void initPanel(String[] param);
	
	/**
	 * Registers the {@link LidaGuiController} as the controller.
	 * @param lgc GuiController for this panel
	 */
	public void registrerLidaGuiController(LidaGuiController lgc);
	
	/**
	 * Sets {@link Lida} object as the model for this panel.
	 * @param lida Lida object
	 */
	public void registerLida(Lida lida);
	
	/**
	 * Update Panel to display supplied object
	 * @param o Object to display
	 */
	public void display (Object o);
	
	/**
	 * Refreshes the content this panel displays.
	 */
	public void refresh();
	
	/**
	 * Returns associated JPanel
	 * @return a JPanel
	 */
	public JPanel getPanel();
    
    /**
     * Sets name of panel
     * @param name label for panel
     */
    public void setName(String name);
    
    /**
     * Gets name of panels
     * @return name of panel
     */
    public String getName();
    
}

