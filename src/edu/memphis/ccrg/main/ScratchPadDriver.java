package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.scratchpad.ScratchPad;

public class ScratchPadDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private ScratchPad pad;

	public ScratchPadDriver(ScratchPad p){
		pad = p;
	}

	public void run(){
		while(keepRunning){
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
	
	
}//class ScratchPadDriver
