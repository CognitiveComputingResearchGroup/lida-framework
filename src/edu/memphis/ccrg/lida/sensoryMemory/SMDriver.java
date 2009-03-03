package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.util.M;
import edu.memphis.ccrg.lida.util.Stoppable;

public class SMDriver implements Runnable, Stoppable{
	private SensoryMemory sm;
	private boolean keepRunning;	
	
	public SMDriver(SensoryMemory sm){
		this.sm = sm;
		keepRunning = true;		
	}//PAMDrive constructor
		
	public void run(){
		//boolean printSentContent = true;
		boolean printSentContent = false;
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(21);}catch(Exception e){}
			sm.processSimContent();
			sm.sendSensoryContent(printSentContent);					
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nSM: Ave. cycle time: " + 
							M.rnd((finishTime - startTime)/(double)counter));
		System.out.println("SM: Num. cycles: " + counter + "\n");			
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);} catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

}//class SMDriver