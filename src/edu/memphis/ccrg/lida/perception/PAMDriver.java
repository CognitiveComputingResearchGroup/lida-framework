package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.util.M;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemory pam;
	private boolean keepRunning;	
	private boolean printPercept;
	public static boolean SHOW_STARTING_ACTIVATION = false; //TODO: move to config file/class
	
	public PAMDriver(PerceptualAssociativeMemory pam, boolean printPercept){
		this.pam = pam;
		keepRunning = true;		
		this.printPercept = printPercept;
	}//PAMDrive constructor
		
	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(22);}catch(Exception e){}
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(printPercept); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			
			//pam.printNodeActivations();
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nPAM: Ave. cycle time: " + 
							M.rnd((finishTime - startTime)/(double)counter));
		System.out.println("PAM: Num. cycles: " + counter + "\n");			

	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning
	
}//class PAMDriver
