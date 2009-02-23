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
		
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(24);}catch(Exception e){}
			sm.processSimContent();
			sm.sendSensoryContent();					
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("SM: ave sm cycle time " + (finishTime - startTime)/(double)counter);
		System.out.println("SM: times received: " + counter);			
		System.out.println("SM: SMDriver ending");
	}//method run
	
	public void stopRunning(){
		keepRunning = false;		
	}//method stopRunning
	
	public void print(int[] a){
		System.out.println("Sense received: " + a[0] + " " + a[1] + " " + a[2] + " " + a[3] + " " + a[4] + " ");
	}//print

}//class SMDriver