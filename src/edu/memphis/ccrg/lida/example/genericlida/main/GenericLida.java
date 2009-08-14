package edu.memphis.ccrg.lida.example.genericlida.main;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.example.genericlida.environ_sm.VisionEnvironment;
import edu.memphis.ccrg.lida.example.genericlida.environ_sm.VisionSensoryMemory;
import edu.memphis.ccrg.lida.framework.LidaStarter;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

public class GenericLida{
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new GenericLida().setup();
	}//main
	
	public void setup(){
		//Declare an EnvironmentImpl
		int height = 10, width = 10;
		EnvironmentImpl environment = new VisionEnvironment(height, width);
		
		//Declare a SensoryMemoryImpl
		SensoryMemoryImpl sensoryMemory = new VisionSensoryMemory();
		
		//Specify a configuration file path
        String configFilePath = "inputFiles/lidaConfig.properties";
		
        //Use the LidaStarter to start the agent
       LidaStarter.start(environment, sensoryMemory, configFilePath);
	}//method
	
}//class