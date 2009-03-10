package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.util.M;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PamImpl pam;
	private boolean keepRunning;	
	public static boolean SHOW_STARTING_ACTIVATION = false; //TODO: move to config file/class
	
	public PAMDriver(PamImpl pam){
		this.pam = pam;
		keepRunning = true;		
	}//PAMDrive constructor
		
	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(22);}catch(Exception e){}
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			
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
