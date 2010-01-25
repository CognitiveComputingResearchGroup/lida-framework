package edu.memphis.ccrg.lida.example.genericlida.main;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaXmlFactory;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiFactory;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;

/**
 * This class allows a LIDA agent to be started in 3 ways:
 * 1. Using the default lida properties path (a String)
 * 2. Using a lida properties path (a String) specified in the command line arguments
 * 3. Using a Properties object created in another class (and/or project).
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public class LidaStarter{
	
	private static Logger logger = Logger.getLogger("lida.example.genericlida.main.GenericLida");
	
	/**
	 * Default lida configuation
	 */
	private static String DEFAULT_LIDA_PROPERTIES_PATH = "configs/lidaConfig.properties";
	
	/**
	 * The lida configuration to be used for this agent
	 */
	private static Properties lidaProperties;
	
	/**
	 * Starts a LIDA agent using the default LIDA properties path or 
	 * the one that is specified in the command line arguments
	 * @param args the command line arguments
	 */
	public static void main(String args[]){
		//Specify the LIDA configuration file path
        String propertiesPath = DEFAULT_LIDA_PROPERTIES_PATH;
		if(args.length != 0)
			propertiesPath = args[0];
		
		Properties properties = ConfigUtils.loadProperties(propertiesPath);
		new LidaStarter(properties).run();
    }//method
	
	/**
	 * @param path - Properties used for the agent
	 */
	public LidaStarter(Properties p){
		lidaProperties = p;
	}
	
	public void run(){
		//Create model, LIDA
		Lida lida = new LidaXmlFactory().getLida(lidaProperties);
		logger.log(Level.CONFIG, "LIDA created", 0L);
		
		//Use the LidaGuiFactory to start the agent
		LidaGuiFactory.start(lida, lidaProperties);
	}
	
}//class