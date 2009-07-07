package edu.memphis.ccrg.lida.proceduralMemory;

import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver implements FrameworkModuleDriver{

	private boolean keepRunning = true;
	private ProceduralMemoryImpl procMem;
	private FrameworkTimer timer;
		
	public ProceduralMemoryDriver(ProceduralMemoryImpl pm, FrameworkTimer timer){
		procMem = pm;
		this.timer = timer;	
	}//constructor

	/**
	 * This loop drives the procedural memory
	 */
	public void run() {
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}	
			timer.checkForStartPause();
			
			procMem.sendGuiContent();
		}//while	
	}//method

	public void stopRunning() {
		keepRunning = false;		
	}

}//class