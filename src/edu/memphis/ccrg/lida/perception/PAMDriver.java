package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemory pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	private FrameworkGui testGui;
	
	public PAMDriver(PerceptualAssociativeMemory pam, FrameworkTimer timer){
		this.pam = pam;
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		//boolean runOneStep = false;
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}						
			timer.checkForStartPause();//won't return if paused until started again			
			//runOneStep = timer.checkForNextStep(runOneStep, threadID);		
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			if(testGui != null)
		        testGui.receiveGuiContent(FrameworkGui.FROM_PAM, pam.getGuiContent());

			//counter++;
		}//while keepRunning	
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}

}//class PAMDriver