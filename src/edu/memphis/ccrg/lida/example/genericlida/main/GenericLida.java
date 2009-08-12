package edu.memphis.ccrg.lida.example.genericlida.main;

import java.util.HashMap;
import java.util.Map;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.example.genericlida.environ_sm.VisionEnvironment;
import edu.memphis.ccrg.lida.example.genericlida.environ_sm.VisionSensoryMemory;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.Module;
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
	        	boolean tasksStartOutRunning = false;
	    		int timeScale = 150;//TODO: Figure out what this rep's.
	    		LidaTaskManager taskManager = new LidaTaskManager(tasksStartOutRunning, timeScale);
	    		//
	    		int height = 10, width = 10;
	    		int ticksPerStep = 10;
	    		EnvironmentImpl environ = new VisionEnvironment(taskManager, ticksPerStep, height, width);
	    		SensoryMemory sm = new VisionSensoryMemory(environ);
	    		//
	        	Map<Module, String> configFilesMap = new HashMap<Module, String>();
	        	configFilesMap.put(Module.perceptualAssociativeMemory, "pam.txt");
	        	configFilesMap.put(Module.transientEpisodicMemory, "tem.txt"); 
	        	configFilesMap.put(Module.declarativeMemory , "dm.txt");
	        	configFilesMap.put(Module.globalWorkspace , "global.txt");
	        	configFilesMap.put(Module.proceduralMemory , "procMem.txt");
	            configFilesMap.put(Module.actionSelection , "as.txt");
	        	//
	        	Lida lida = new Lida(taskManager, environ, sm, configFilesMap);
	        	LidaGuiController lgc = new LidaGuiControllerImpl(lida);	        	
	            new LidaGui(lida, lgc).setVisible(true);
	        }//run
	        
	    });//invokeLater
	}//main
	
}//class
