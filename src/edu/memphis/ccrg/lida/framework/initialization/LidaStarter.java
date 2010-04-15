package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
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

	private static Logger logger = Logger.getLogger("lida.example.genericlida.main.LidaStarter");

	/**
	 * Default lida configuation
	 */
	private static final String DEFAULT_LIDA_PROPERTIES_PATH = "configs/lidaConfig.properties";

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
	}// method

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
	 * @param path
	 *            - Properties file used for the agent
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
		lidaFactoriesLoader.loadData(lidaProperties);
		
		// Create model, LIDA
		Lida lida = new LidaXmlFactory().getLida(lidaProperties);
		logger.log(Level.CONFIG, "LIDA created", 0L);

		// Use the LidaGuiFactory to start the agent
		LidaGuiFactory.start(lida, lidaProperties);
	}

}// class