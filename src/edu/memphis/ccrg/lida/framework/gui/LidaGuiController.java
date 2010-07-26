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
public interface LidaGuiController {

	/**
	 * Executes a command specified by the name. This name corresponds to a property in 
	 * guiCommands.properties file.
	 * 
	 * @param commandName the name of the command.
	 * @param parameters a Map of parameters for the command.
	 * @return the result of the command.
	 */
	public abstract Object executeCommand (String commandName, Map<String,Object> parameters);
	/**
	 * Executes a command sent by the GUI
	 * @param command the command to execute. 
	 * @return  The result of the command.
	 */
	public abstract Object executeCommand (Command command);
	
	public abstract void registrerLida(Lida lida);
	
}
