package edu.memphis.ccrg.lida.framework.gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;

/**
 * @author Javier Snaider
 * 
 */
public class LidaGuiControllerImpl implements LidaGuiController {

	private static Logger logger = Logger.getLogger("lida.framework.gui.Controller");
	private Lida lida;
	Properties commands;

	/**
	 * @param lida
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
	public Object executeCommand (Command command){
		command.execute(lida);
		logger.log(Level.FINE, "Command "+ command + " executed",LidaTaskManager.getActualTick());
		return command.getResult();
	}
	public boolean isSystemPaused() {
		return lida.getTaskManager().isTasksPaused();
	}

	public void registrerLida(Lida lida) {
		this.lida = lida;
	}

}
