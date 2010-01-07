package edu.memphis.ccrg.lida.example.genericlida.main;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaXmlFactory;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiFactory;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;

public class GenericLida{
	
	private static Logger logger = Logger.getLogger("lida.example.genericlida.main.GenericLida");
	
	private static String DEFAULT_LIDAPROPERTIES_FILENAME="configs/lidaConfig.properties";
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		//Specify a configuration file path
        String configFilePath = DEFAULT_LIDAPROPERTIES_FILENAME;
		if(args.length!=0){
			configFilePath=args[0];
		}			
        Properties lidaProperties = ConfigUtils.loadProperties(configFilePath);
        
        //Create Model: LIDA
//    	Lida lida = new Lida1(lidaProperties);
    	Lida lida =new LidaXmlFactory().getLida(lidaProperties);
		logger.log(Level.CONFIG,"LIDA created",0L);

       //Use the LidaGuiFactory to start the agent
       LidaGuiFactory.start(lida,lidaProperties);
	}//method
	
}//class