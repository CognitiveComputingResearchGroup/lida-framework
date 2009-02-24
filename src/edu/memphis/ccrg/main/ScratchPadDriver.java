package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.scratchpad.ScratchPad;

public class ScratchPadDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private ScratchPad pad;

	public ScratchPadDriver(ScratchPad p){
		pad = p;
	}

	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(24);}catch(Exception e){}
			
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();				
		System.out.println("\nSPAD: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("SPAD: Num. cycles: " + counter);		
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//public void stopRunning()
	
	
}//class ScratchPadDriver
