package edu.memphis.ccrg.lida.example.genericlida.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.LidaFactory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

public class Apple{
	
	private Logger logger = Logger.getLogger("lida.example.genericlida.main.GenericLida");
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new Apple().setup();
	}//main
	
	public void setup(){
		//Declare an EnvironmentImpl
		int height = 10, width = 10;
		EnvironmentImpl environment = new VisionEnvironment(height, width);
		
		//Declare a SensoryMemoryImpl
		SensoryMemoryImpl sensoryMemory = new VisionSensoryMemory();
		
		//Specify a configuration file path
        String lidaConfigPath = "configs/lidaConfig.properties";
        Properties lidaProperties = new Properties();
        try{
			lidaProperties.load(new BufferedReader(new FileReader(lidaConfigPath)));
		}catch(FileNotFoundException e){
			throw new IllegalArgumentException();
		}catch(IOException e){
			logger.log(Level.SEVERE, "Error reading lida properties file {1}",new Object[]{0L, e.getMessage()});
		}
		
		String guiPanelConfigPath = "configs/appleGuiPanels.properties";
		Properties guiPanelProperties = new Properties();
        try{
			guiPanelProperties.load(new BufferedReader(new FileReader(guiPanelConfigPath)));
		}catch(FileNotFoundException e){
			throw new IllegalArgumentException();
		}catch(IOException e){
			logger.log(Level.SEVERE, "Error reading GUI panel properties file {1}", new Object[]{0L, e.getMessage()});
		}
		
        //Use the LidaFactory to start the agent
       LidaFactory.start(environment, sensoryMemory, lidaProperties, guiPanelProperties);
	}//method
	
}//class