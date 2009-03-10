package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;

public class SMDriver implements Runnable, Stoppable{
	private SensoryMemory sm;
	private boolean keepRunning;	
	private FrameworkTimer timer;
	
	public SMDriver(SensoryMemory sm, FrameworkTimer timer){
		this.sm = sm;
		keepRunning = true;		
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		//boolean printSentContent = true;
		boolean printSentContent = false;
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(21);}catch(Exception e){}
			timer.checkForClick();
			
			sm.processSimContent();
			sm.sendSensoryContent(printSentContent);					
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nSM: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("SM: Num. cycles: " + counter + "\n");			
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);} catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

}//class SMDriver