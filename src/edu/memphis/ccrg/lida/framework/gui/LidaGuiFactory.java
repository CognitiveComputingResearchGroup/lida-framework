/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;

/**
 * For specific lida agent implementations, create a main class simply 
 * need call this class's start method with particular parameters for that agent:
 * Lida instance and a Properties.
 * Creates a MVC for LIDA
 * 
 * @author Ryan J. McCall
 *
 */
public class LidaGuiFactory {
	
	private static final Logger logger = Logger.getLogger(LidaGuiFactory.class.getCanonicalName());
	private static String DEFAULT_COMMANDS_FILENAME = "configs/guiCommands.properties";
	private static final String DEFAULT_PANELS_FILENAME = "configs/guiPanels.properties";

	public static void start(final Lida lida, final Properties lidaProperties) {
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){					
	        	//Create the controller
				String filename=lidaProperties.getProperty("lida.gui.commands",DEFAULT_COMMANDS_FILENAME);
				Properties properties =ConfigUtils.loadProperties(filename);
				if(properties == null){
					logger.log(Level.SEVERE, "unable to load gui commands");
				}
				
	        	LidaGuiController controller = new LidaGuiControllerImpl(lida, properties);
				logger.log(Level.INFO,"GUI Controller created",0L);
				
				filename=lidaProperties.getProperty("lida.gui.panels",DEFAULT_PANELS_FILENAME);
				properties = ConfigUtils.loadProperties(filename);
				if(properties == null){
					logger.log(Level.SEVERE, "unable to load gui panels properties");
				}
				
	        	LidaGui lidaGui = new LidaGui(lida, controller, properties);
				logger.log(Level.INFO,"GUI created",0L);
	        	//Start the GUI
	            lidaGui.setVisible(true);
	        }//run
		});//invokeLater
		
	}

}