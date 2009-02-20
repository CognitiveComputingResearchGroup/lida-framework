package edu.memphis.ccrg.main;

import edu.memphis.ccrg.perception.PerceptualAssociativeMemory;

public class PAMDriver implements Runnable{

	private PerceptualAssociativeMemory pam;
	private boolean keepRunning;	
	public static boolean SHOW_STARTING_ACTIVATION = false; //TODO: move to config file/class
	
	public PAMDriver(PerceptualAssociativeMemory pam){
		this.pam = pam;
		pam.initPAM();
		keepRunning = true;		
	}//PAMDrive constructor
		
	public void run(){
		
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(24);}catch(Exception e){}
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations		
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("P: ave pam cycle time " + (finishTime - startTime)/(double)counter);
		System.out.println("P: times received: " + counter);			
		System.out.println("P: PAMDriver ending");
	}//method run
	
	public void stopRunning(){
		keepRunning = false;		
	}//method stopRunning
	
	public void print(int[] a){
		System.out.println("Sense received: " + a[0] + " " + a[1] + " " + a[2] + " " + a[3] + " " + a[4] + " ");
	}//print

}//class PAMDriver
