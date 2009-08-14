package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.gui.LidaGui;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiControllerImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

/**
 * For specific lida agent implementations create a main class simply 
 * need call this class's start method with particular parameters for that agent:
 * Environment, SensoryMemory and config file path.
 * 
 * @author ryanjmccall
 *
 */
public class LidaStarter {
	
	public static void start(final EnvironmentImpl environment, 
							 final SensoryMemoryImpl sensoryMemory, 
				             final String configFilePath){
		
		java.awt.EventQueue.invokeLater(new Runnable(){
			
	        public void run(){	
	        	//Create the model and the controller
	        	Lida lida = new Lida(environment, sensoryMemory, configFilePath);
	        	LidaGuiController controller = new LidaGuiControllerImpl(lida);	        	
	            new LidaGui(lida, controller).setVisible(true);
	        }//run
	        
	    });//invokeLater
		
	}//method

}//class