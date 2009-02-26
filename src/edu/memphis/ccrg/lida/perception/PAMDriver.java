package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.misc.Misc;
import edu.memphis.ccrg.lida.sensoryMemory.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemory pam;
	private boolean keepRunning;	
	public static boolean SHOW_STARTING_ACTIVATION = false; //TODO: move to config file/class
	
	public PAMDriver(PerceptualAssociativeMemory pam){
		this.pam = pam;
		pam.initPAM();
		keepRunning = true;		
	}//PAMDrive constructor
		
	public void run(){
		boolean printSentPercept = false;
		//boolean printSentPercept = true;
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(22);}catch(Exception e){}
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(printSentPercept); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			
			//pam.printNodeActivations();
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nPAM: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("PAM: Num. cycles: " + counter);			

	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning
	
}//class PAMDriver
