package edu.memphis.ccrg.lida.framework.gui;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;

/**
 * For specific lida agent implementations create a main class simply 
 * need call this class's start method with particular parameters for that agent:
 * Environment, SensoryMemory and config file path.
 * Creates a MVC for LIDA
 * 
 * @author ryanjmccall
 *
 */
public class LidaGuiFactory {
	
	private static Logger logger = Logger.getLogger("lida.framework.LidaGuiFactory");
	private static String DEFAULT_COMMANDS_FILENAME = "configs/guiCommands.properties";
	private static final String DEFAULT_PANELS_FILENAME = "configs/guiPanels.properties";

	public static void start(final Lida lida, final Properties lidaProperties) {
		java.awt.EventQueue.invokeLater(new Runnable(){
			public void run(){					
	        	//Create the controller
				String filename=lidaProperties.getProperty("lida.gui.commands",DEFAULT_COMMANDS_FILENAME);
				Properties properties =ConfigUtils.loadProperties(filename);
	        	LidaGuiController controller = new LidaGuiControllerImpl(lida, properties);
				logger.log(Level.CONFIG,"GUI Controller created",0L);
				
				filename=lidaProperties.getProperty("lida.gui.panels",DEFAULT_PANELS_FILENAME);
				properties =ConfigUtils.loadProperties(filename);
	        	LidaGui lidaGui = new LidaGui(lida, controller, properties);
				logger.log(Level.CONFIG,"GUI created",0L);
	        	//Start the GUI
	            lidaGui.setVisible(true);
	        }//run
		});//invokeLater
		
	}//method

}//class