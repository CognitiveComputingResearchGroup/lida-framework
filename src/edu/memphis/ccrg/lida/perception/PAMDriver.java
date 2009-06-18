package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemory pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	private FrameworkGui nodeLinkFlowGui;
	
	public PAMDriver(PerceptualAssociativeMemory pam, FrameworkTimer timer){
		this.pam = pam;
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}						
			timer.checkForStartPause();//won't return if paused until started again					
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			if(nodeLinkFlowGui != null)
		        nodeLinkFlowGui.receiveGuiContent(FrameworkGui.FROM_PAM, pam.getGuiContent());

		}//while	
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

	public void addFlowGui(FrameworkGui testGui) {
		this.nodeLinkFlowGui = testGui;		
	}

}//class PAMDriver