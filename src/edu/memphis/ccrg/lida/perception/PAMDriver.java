package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class PAMDriver implements FrameworkModuleDriver{

	private PerceptualAssociativeMemoryImpl pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	
	public PAMDriver(PerceptualAssociativeMemoryImpl pam, FrameworkTimer timer){
		this.pam = pam;
		this.timer = timer;
	}//constructor
		
	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();//won't return if paused until started again					
						
			pam.detectSensoryMemoryContent();				
			pam.propogateActivation();//Pass activation	
			pam.sendOutPercept(); //Send the percept to p-Workspace
			pam.sendGuiContent();
			pam.decayPAM();  //Decay the activations	
		}//while	
	}//method run
	
	public void stopRunning(){
		keepRunning = false;		
	}//method
	
}//class PAMDriver