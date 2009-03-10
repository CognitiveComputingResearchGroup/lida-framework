package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PamImpl pam;
	private boolean keepRunning;	
	public static boolean SHOW_STARTING_ACTIVATION = false; //TODO: move to config file/class
	private FrameworkTimer timer;
	
	public PAMDriver(PamImpl pam, FrameworkTimer timer){
		this.pam = pam;
		keepRunning = true;		
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(22  + timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForClick();
			
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nPAM: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("PAM: Num. cycles: " + counter + "\n");			

	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning
	
}//class PAMDriver
