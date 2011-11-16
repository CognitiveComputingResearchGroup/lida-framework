/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiFactory;

/**
 * This class allows an {@link Agent} to be started in these ways using the static methods start()
 * 		1. Using the default agent properties path (a String)
 * 		2. Using a agent properties path (a String)
 * 		3. Using a Properties object created in another class (and/or project).
 * 
 * Or using the main method with a {@link Agent} properties path (a String)
 * specified in the command line arguments.
 * 
 * @author Ryan J. McCall
 * @author Javier Snaider
 */
public class AgentStarter {

	private static final Logger logger = Logger.getLogger(AgentStarter.class.getCanonicalName());

	/**
	 * Default properties configuation
	 */
	public static final String DEFAULT_PROPERTIES_PATH = "configs/lidaConfig.properties";

	/**
	 * The properties configuration to be used for this agent
	 */
	private static Properties agentProperties;

	/**
	 * Starts an {@link Agent} using the default properties path or the one
	 * that is specified in the command line arguments
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		// Specify the configuration file path
		String propertiesPath = DEFAULT_PROPERTIES_PATH;
		if (args.length != 0)
			propertiesPath = args[0];

		Properties properties = ConfigUtils.loadProperties(propertiesPath);
		if (properties != null){
			start(properties);
		}else{
			start();
		}
	}

	/**
	 * Load supplied properties
	 * @param p - Properties used for the agent
	 */
	public static void start(Properties p) {
		agentProperties = p;
		if (agentProperties == null) {
			start();
		} else {
			run();
		}
	}
	/**
	 * Load the default properties
	 */
	public static void start() {
		String propertiesPath = DEFAULT_PROPERTIES_PATH;
		agentProperties = ConfigUtils.loadProperties(propertiesPath);
		if (agentProperties != null){
			run();
		}else{
			logger.log(Level.SEVERE, "Could not load main configuration file from path: {0}. Application cannot start.", propertiesPath);
		}
	}

	/**
	 * @param propertiesPath - Properties file used for the agent
	 */
	public static void start(String propertiesPath) {
		agentProperties = ConfigUtils.loadProperties(propertiesPath);
		if (agentProperties != null) {
			run();
		} else {
			start();
		}
	}

	/**
	 * Create and run an {@link Agent} and a {@link FrameworkGuiFactory} using the properties object
	 */
	private static void run() {
		//Load factories data
		FactoriesDataXmlLoader factoriesDataLoader=new FactoriesDataXmlLoader();
		factoriesDataLoader.loadFactoriesData(agentProperties);
		
		// Create model: Agent
		Agent agent = new AgentXmlFactory().getAgent(agentProperties);
		if(agent == null){
			logger.log(Level.SEVERE, "Failed to create agent, application not started.");
			return;
		}
		GlobalInitializer.getInstance().clearAttributes();
		logger.log(Level.CONFIG, "Agent created", 0L);
		
		String loggingFile = agentProperties.getProperty("lida.logging.configuration");
		if(loggingFile!=null){
			ConfigUtils.configLoggers(loggingFile);
		}
		
		boolean enableGui = Boolean.parseBoolean(agentProperties.getProperty("lida.gui.enable", "true"));
		// Use the FrameworkGuiFactory to start the agent
		if(enableGui){
			FrameworkGuiFactory.start(agent, agentProperties);
		}else{	//no gui		
			agent.getTaskManager().resumeTasks();
		}
	}

}