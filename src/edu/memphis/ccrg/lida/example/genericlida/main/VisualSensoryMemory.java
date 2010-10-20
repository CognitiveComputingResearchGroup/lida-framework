package edu.memphis.ccrg.lida.example.genericlida.main;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

/**
 * 
 * @author Ryan McCall
 *
 */
public class VisualSensoryMemory extends SensoryMemoryImpl {

	/**
	 * A matrix representation representing the beginning of 
	 * the agent's understanding.
	 * This variable or whatever variables you declare should be 
	 * modified using 'synchronized(this)' as feature detectors
	 * running in separate threads will access this variable(s) as well
	 */
	private double[][] sensoryScene = new double[5][5];
	
	/**
	 * This method is called repeatedly by SensoryMemoryDriver.
	 * You can control this rate by changing the 'ticksperstep' 
	 * parameter for SensoryMemoryDriver in lida.xml
	 * 
	 * Notice environment is inherited from SensoryMemoryImpl.
	 * Module content depends on what environment you are using.
	 * 
	 */
	@Override
	public void runSensors() {
		//Environment is inherited from SensoryMemoryImpl
		//Module content depends on your environment
		double[][] environContent = (double[][]) environment.getModuleContent();
		//Add processing code
		
		//Store the results of processing
		synchronized(this){
			sensoryScene = environContent;
		}
	}

	public Object getModuleContent(Object... parameters) {
		if("sensoryScene".equalsIgnoreCase((String) parameters[0]))
			return sensoryScene;
		return null;
	}

	@Override
	public void receiveAction(LidaAction a) {
		
	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// TODO Auto-generated method stub
		return false;
	}

}//class
