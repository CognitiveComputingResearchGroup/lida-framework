package edu.memphis.ccrg.lida.workspace.sbCodelets;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.broadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.lida.workspace.csm.CSM;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;

public class SBCodelet implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkTimer timer;
	//
	private PerceptualBuffer pBuffer = null;
	private EpisodicBuffer eBuffer = null;
	private PreviousBroadcasts pBroads = null;
	private CSM csm;
	//
	private double activation = 1.0;
	private CodeletObjective objective = null;
	private CodeletAction action = new CodeletAction();
			
	public SBCodelet(FrameworkTimer t, PerceptualBuffer buffer, EpisodicBuffer eBuffer, 
					PreviousBroadcasts pBroads, CSM csm, double activation, CodeletObjective obj, CodeletAction a){
		timer = t;
		pBuffer = buffer;
		this.eBuffer = eBuffer;
		this.pBroads = pBroads;
		this.csm = csm;
		this.activation = activation;
		objective = obj;
		action = a;
	}
	
	public void run() {
		while(keepRunning){
			timer.checkForStartPause();
			if(pBuffer != null){
				
			}
			
		}		
	}
	
	public void setActivation(double a){
		activation = a;
	}
	public void setContext(CodeletObjective obj){
		objective = obj;
	}
	public void setCodeletAction(CodeletAction a){
		action = a;
	}
	public double getActivation(){
		return activation;
	}
	public CodeletObjective getObjective(){
		return objective;
	}
	public CodeletAction getCodeletAction(){
		return action;
	}

	public long getThreadID() {
		return threadID;
	}

	public void setThreadID(long id) {
		threadID = id;		
	}

	public void stopRunning() {
		keepRunning = false;		
	}

}//class SBCodelet
