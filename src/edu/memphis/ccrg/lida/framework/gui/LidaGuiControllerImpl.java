/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * @author Javier Snaider
 * 
 */
public class LidaGuiControllerImpl implements LidaGuiController {

	private static Logger logger = Logger.getLogger(LidaGuiControllerImpl.class.getCanonicalName());
	private Lida lida;
	Properties commands;

	/**
	 * @param lida Lida object
	 */
	public LidaGuiControllerImpl(Lida lida, Properties commands) {
		super();
		this.lida = lida;
 		this.commands = commands;
	}
	public LidaGuiControllerImpl(Lida lida) {
		this(lida,null);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.gui.LidaGuiController#executeCommand(java.lang.String, java.util.Map)
	 */
	@Override
	public Object executeCommand (String commandName, Map<String,Object> parameters){
		String commandClass = commands.getProperty(commandName);
		Command command=null;
		if(commandClass != null){
			try {
				command=(Command)(Class.forName(commandClass)).newInstance();
			} catch (Exception e) {
				logger.log(Level.WARNING,e.getMessage());
			}
		}
		if (command == null){
			return null;
		}
		if(parameters != null){
			command.setParameters(parameters);
		}
		command.execute(lida);
		logger.log(Level.FINE, "Command "+ commandName + " executed",LidaTaskManager.getActualTick());
		return command.getResult();
	}

	/**
	 * Executes a command sent by the GUI
	 * @param command the command to execute. 
	 * @return  The result of the command.
	 */
	@Override
	public Object executeCommand (Command command){
		command.execute(lida);
		logger.log(Level.FINE, "Command "+ command + " executed",LidaTaskManager.getActualTick());
		return command.getResult();
	}
	public boolean isSystemPaused() {
		return lida.getTaskManager().isTasksPaused();
	}

	@Override
	public void registrerLida(Lida lida) {
		this.lida = lida;
	}

}
