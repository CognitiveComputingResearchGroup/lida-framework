package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionCodeletImpl implements AttentionCodelet, Runnable, Stoppable {
	
	private boolean keepRunning = true;
	private int codeletSleepTime = 4;
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
			try{
				Thread.sleep(codeletSleepTime);
			}catch(InterruptedException e){
				stopRunning();
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