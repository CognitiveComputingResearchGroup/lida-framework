package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class AttentionCodeletImpl implements AttentionCodelet{
	
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
			try{
				Thread.sleep(codeletSleepMillis, codeletSleepNanos);
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

	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}//class