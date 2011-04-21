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

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;

/**
 * For specific agent implementations, create a main class simply 
 * need call this class's start method with particular parameters for that agent:
 * {@link Agent} instance and a Properties.
 * Creates a MVC for the system
 * 
 * @author Ryan J. McCall
 *
 */
public class FrameworkGuiFactory {
	
	private static final Logger logger = Logger.getLogger(FrameworkGuiFactory.class.getCanonicalName());
	private static String DEFAULT_COMMANDS_FILENAME = "configs/guiCommands.properties";
	private static final String DEFAULT_PANELS_FILENAME = "configs/guiPanels.properties";

	public static void start(final Agent agent, final Properties agentProperties) {
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){					
	        	//Create the controller
				String filename=agentProperties.getProperty("lida.gui.commands",DEFAULT_COMMANDS_FILENAME);
				Properties properties =ConfigUtils.loadProperties(filename);
				if(properties == null){
					logger.log(Level.SEVERE, "unable to load gui commands");
				}
				
	        	FrameworkGuiController controller = new FrameworkGuiControllerImpl(agent, properties);
				logger.log(Level.INFO,"GUI Controller created",0L);
				
				filename=agentProperties.getProperty("lida.gui.panels",DEFAULT_PANELS_FILENAME);
				properties = ConfigUtils.loadProperties(filename);
				if(properties == null){
					logger.log(Level.SEVERE, "unable to load guiPanels.properties");
				}
				
	        	FrameworkGui FrameworkGui = new FrameworkGui(agent, controller, properties);
	            FrameworkGui.setVisible(true);
	            
	            logger.log(Level.INFO,"FrameworkGui started\n",0L);
	        }//run
		});//invokeLater
		
	}

}