package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.LidaTaskBase;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class AttentionCodeletImpl extends LidaTaskBase implements AttentionCodelet{
	
	private boolean keepRunning = true;
	private int codeletSleepMillis = 3;
	private int codeletSleepNanos = 100000; //1 million nanoseconds = 1 millisecond
	private ContentDetectBehavior checkBehavior = new DefaultContentDetectBehavior();
	//
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	private double activation;
	    
    public AttentionCodeletImpl(CurrentSituationalModel csm, GlobalWorkspace g, 
    							double activation){
    	this.csm = csm;
    	global = g;
    	this.activation = activation; 	
    }

	public void run() {
		while(keepRunning){

//TODO: Fix this!!!!!			
//			timer.checkForStartPause();
			if (LidaTaskManager.isTicksMode()) {
				if(hasEnoughTicks()){
					consumeTicksForACycle();
				}else{
					continue;
				}
			} else {
				try {
					Thread.sleep(codeletSleepMillis, codeletSleepNanos);
				} catch (InterruptedException e) {
					stopRunning();
				}
			}
			//
			if(checkBehavior.hasSoughtContent(csm)){
				NodeStructure csmContent = checkBehavior.getSoughtContent(csm); 
				if(csmContent != null)
					global.addCoalition(new CoalitionImpl(csmContent, activation));
				
			}//if
		}//while
	}//method	

	public void stopRunning() {
		keepRunning = false;		
	}

}//class