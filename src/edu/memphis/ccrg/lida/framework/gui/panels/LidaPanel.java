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
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;

/**
 * A GUI Panel for the LidaGui.
 * @author Javier Snaider
 */
public interface LidaPanel {
	
	/**
	 * Registers the LidaGuiController
	 * @param lgc GuiController for this panel
	 */
	public void registrerLidaGuiController(LidaGuiController lgc);
	
	/**
	 * Set Lida object for this panel.
	 * @param lida Lida object
	 */
	public void registerLida(Lida lida);
	
	/**
	 * Update Panel to display supplied object
	 * @param o Object to display
	 */
	public void display (Object o);
	
	/**
	 * Refresh the content this panel displays.
	 */
	public void refresh();
	
	/**
	 * Returns associated JPanel
	 * @return a JPanel
	 */
	public JPanel getPanel();
	
	/**
	 * Returns supported Module.
	 * @return ModuleName of Module supported by this LidaPanel
	 */
    public ModuleName getSupportedModule();
    
    /**
     * Sets supported module
     * @param module ModuleName of supported Module
     */
    public void setSupportedModule(ModuleName module);
    
    /**
     * Sets name of panel
     * @param name label for panel
     */
    public void setName(String name);
    
    /**
     * 
     * @return name of panel
     */
    public String getName();
    
    /**
     * initializes panel
     * @param param Paramters to initialize 
     */
    public void initPanel(String[] param);
}

