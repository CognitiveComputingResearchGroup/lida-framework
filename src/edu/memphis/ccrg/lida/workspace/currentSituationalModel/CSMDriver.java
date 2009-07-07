package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.example.genericLIDA.main.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class CSMDriver implements FrameworkModuleDriver{
	
	private boolean keepRunning = true;
	private FrameworkTimer timer;
	private CurrentSituationalModelImpl csm;
	
	public CSMDriver(CurrentSituationalModelImpl csm, FrameworkTimer t){
		timer = t;
		this.csm = csm;
	}

	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}	
			timer.checkForStartPause();
			csm.sendCSMContent();
			csm.sendGuiContent();	        
		}//while
	}//method

	public void stopRunning(){
		keepRunning = false;		
	}//method
	
}//class
