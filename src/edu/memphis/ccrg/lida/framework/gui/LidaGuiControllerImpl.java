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
import edu.memphis.ccrg.lida.framework.gui.commands.Command;

/**
 * @author Javier Snaider
 * 
 */
public class LidaGuiControllerImpl implements LidaGuiController {

	private Logger logger = Logger.getLogger("lida.framework.gui.Controller");
	private Lida lida;
	Properties commands;

	/**
	 * @param lida
	 */
	public LidaGuiControllerImpl(Lida lida, String commandsFile) {
		super();
		this.lida = lida;
		commands = new Properties();
		if(commandsFile!=null){
		try {
			commands.load(new BufferedReader(new FileReader(commandsFile)));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading Commands List {0}", e.getMessage());
		}
		}else{
			logger.log(Level.WARNING, "Commands File no especified");			
		}
	}
	public LidaGuiControllerImpl(Lida lida) {
		this(lida,null);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.gui.LidaGuiController#executeCommand(java.lang.String, java.util.Map)
	 */
	public Object executeCommand (String commandName,Map<String,Object> parameters){
		String commandClass = commands.getProperty(commandName);
		Command command=null;
		if(commandClass != null){
			try {
				command=(Command)(Class.forName(commandClass)).newInstance();
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		if (command == null){
			return null;
		}
		if(parameters != null){
			command.setParameters(parameters);
		}
		command.execute(lida);
		logger.info("Command "+ commandName + " executed");
		return command.getResult();
	}

	/**
	 * Executes a command sent by the GUI
	 * @param command the command to execute. 
	 * @return  The result of the command.
	 */
	public Object executeCommand (Command command){
		command.execute(lida);
		logger.info("Command "+ command + " executed");
		return command.getResult();
	}
	public boolean isSystemPaused() {
		return lida.getTaskManager().isTasksPaused();
	}

	public void registrerLida(Lida lida) {
		this.lida = lida;
	}

}
