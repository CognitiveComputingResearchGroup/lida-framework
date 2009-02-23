package edu.memphis.ccrg.main;

import edu.memphis.ccrg.sensoryMemory.SensoryMemory;

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
			try{Thread.sleep(24);}catch(Exception e){}
			sm.processSimContent();
			sm.sendSensoryContent(printSentContent);					
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nSM: Ave. cycle time: " + rnd((finishTime - startTime)/(double)counter));
		System.out.println("SM: Num. cycles: " + counter);			
	}//method run
	
	public void stopRunning(){
		keepRunning = false;		
	}//method stopRunning
	
	public double rnd(double d){    //rounds a double to the nearest 10000th
    	return Math.round(d*10000.0)/10000.0;
    }

}//class SMDriver