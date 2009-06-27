package edu.memphis.ccrg.lida.proceduralMemory;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private ProceduralMemory procMem;
	private FrameworkTimer timer;
	private FrameworkGui flowGui;
		
	public ProceduralMemoryDriver(ProceduralMemory pm, FrameworkTimer timer, FrameworkGui gui) {
		procMem = pm;
		this.timer = timer;
		flowGui = gui;		
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
			
			flowGui.receiveGuiContent(FrameworkGui.FROM_PROCEDURAL_MEMORY, procMem.getGuiContent());
		}//while	
	}//method

	public void stopRunning() {
		keepRunning = false;		
	}

}//class