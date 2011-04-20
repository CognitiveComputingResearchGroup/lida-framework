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
package edu.memphis.ccrg.lida.framework.gui;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;

/**
 * @author Javier Snaider
 *
 */
public interface FrameworkGuiController {

	/**
	 * Executes a command specified by the name. This name corresponds to a property in 
	 * guiCommands.properties file.
	 * 
	 * @param commandName the name of the command.
	 * @param parameters a Map of parameters for the command.
	 * @return the result of the command.
	 */
	public Object executeCommand (String commandName, Map<String,Object> parameters);
	/**
	 * Executes a command sent by the GUI
	 * @param command the command to execute. 
	 * @return  The result of the command.
	 */
	public Object executeCommand (Command command);
	
	void registrerLida(Lida lida);
	
}
