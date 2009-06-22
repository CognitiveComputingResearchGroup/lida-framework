package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemoryImpl pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	private FrameworkGui nodeLinkFlowGui;
	
	public PAMDriver(PerceptualAssociativeMemoryImpl pam, FrameworkTimer timer, FrameworkGui flowGui){
		this.pam = pam;
		this.timer = timer;
		nodeLinkFlowGui = flowGui;
	}//PAMDrive constructor
		
	public void run(){
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}						
			timer.checkForStartPause();//won't return if paused until started again					
					
			pam.detectSensoryMemoryContent();				
			pam.propogateActivation();//Pass activation	
			pam.sendOutPercept(); //Send the percept to p-Workspace
			pam.decayPAM();  //Decay the activations	

		    nodeLinkFlowGui.receiveGuiContent(FrameworkGui.FROM_PAM, pam.getGuiContent());
		}//while	
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

}//class PAMDriver