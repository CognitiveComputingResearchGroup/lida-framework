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
	        	boolean startPaused = false;
	    		int threadSleepTime = 150;
	    		LidaTaskManager timer = new LidaTaskManager(startPaused, threadSleepTime);
	    		//
	    		int height = 10, width = 10;
	    		EnvironmentImpl environ = new VisionEnvironment(timer, height, width);
	    		SensoryMemory sm = new VisionSensoryMemory(environ);
	    		//
	        	Lida lida = new Lida(timer, environ, sm);
	        	LidaGuiController lgc = new LidaGuiControllerImpl(lida);
	            new LidaGui(lida, lgc).setVisible(true);
	        }//run
	        
	    });//invokeLater
	}//main
	
}//class
