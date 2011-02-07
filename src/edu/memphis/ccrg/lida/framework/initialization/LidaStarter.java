/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.dao.DAOManager;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiFactory;

/**
 * This class allows a LIDA agent to be started in these ways using the static methods start()
 * 		1. Using the default lida properties path (a String)
 * 		2. Using a lida properties path (a String)
 * 		3. Using a Properties object created in another class (and/or project).
 * 
 * Or using the main method with a lida properties path (a String)
 * specified in the command line arguments.
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public class LidaStarter {

	private static final Logger logger = Logger.getLogger(LidaStarter.class.getCanonicalName());

	/**
	 * Default lida configuation
	 */
	public static final String DEFAULT_LIDA_PROPERTIES_PATH = "configs/lidaConfig.properties";

	/**
	 * The lida configuration to be used for this agent
	 */
	private static Properties lidaProperties;

	/**
	 * Starts a LIDA agent using the default LIDA properties path or the one
	 * that is specified in the command line arguments
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		// Specify the LIDA configuration file path
		String propertiesPath = DEFAULT_LIDA_PROPERTIES_PATH;
		if (args.length != 0)
			propertiesPath = args[0];

		Properties properties = ConfigUtils.loadProperties(propertiesPath);
		start(properties);
	}

	/**
	 * Load supplied properties
	 * @param p - Properties used for the agent
	 */
	public static void start(Properties p) {
		lidaProperties = p;
		if (lidaProperties == null) {
			start();
		} else {
			run();
		}
	}
	/**
	 * Load the default properties
	 */
	public static void start() {
		String propertiesPath = DEFAULT_LIDA_PROPERTIES_PATH;
		lidaProperties = ConfigUtils.loadProperties(propertiesPath);
		run();
	}

	/**
	 * @param propertiesPath - Properties file used for the agent
	 */
	public static void start(String propertiesPath) {
		if (propertiesPath == null) 
			propertiesPath = DEFAULT_LIDA_PROPERTIES_PATH;
	
		lidaProperties = ConfigUtils.loadProperties(propertiesPath);
		if (lidaProperties == null) {
			start();
		} else {
			run();
		}
	}

	/**
	 * Create and run a LIDA and a LidaGuiFactory using the properties object
	 */
	private static void run() {
		//Load factories data
		LidaFactoriesXMLLoader lidaFactoriesLoader=new LidaFactoriesXMLLoader();
		lidaFactoriesLoader.loadFactoriesData(lidaProperties);
		
		// Create model, LIDA
		Lida lida = new LidaXmlFactory().getLida(lidaProperties);
		logger.log(Level.CONFIG, "LIDA created", 0L);

        // Initialize Data Access Objects
		DAOManager manager = DAOManager.getInstance();
        if (!manager.isInitialized())
//        	manager.initDataAccessObjects(lida);
        //TODO Uncomment when DAO is operational

		// Use the LidaGuiFactory to start the agent
		LidaGuiFactory.start(lida, lidaProperties);
	}

}