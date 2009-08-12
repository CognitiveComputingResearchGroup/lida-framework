package edu.memphis.ccrg.lida.example.genericlida.main;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.example.genericlida.environ_sm.VisionEnvironment;
import edu.memphis.ccrg.lida.example.genericlida.environ_sm.VisionSensoryMemory;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.gui.LidaGui;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiControllerImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public class GenericLida {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable(){
			
	        public void run() {	
	        	//Define task manager
	        	boolean tasksStartOutRunning = false;
	    		int tickDuration = 10;
	    		LidaTaskManager taskManager = new LidaTaskManager(tasksStartOutRunning, tickDuration);
	    		
	    		//Define environment
	    		int height = 10, width = 10;
	    		int ticksPerStep = 10;
	    		EnvironmentImpl environ = new VisionEnvironment(taskManager, ticksPerStep, height, width);
	    		
	    		//Define sensory memory
	    		SensoryMemory sm = new VisionSensoryMemory(environ);
	    		
	    		//Config path
	            String configFilePath = "lidaConfig.txt";
	            
	        	//Create the model and the controller
	        	Lida lida = new Lida(taskManager, environ, sm, configFilePath);
	        	LidaGuiController lgc = new LidaGuiControllerImpl(lida);	        	
	            new LidaGui(lida, lgc).setVisible(true);
	        }//run
	        
	    });//invokeLater
	}//main
	
}//class
