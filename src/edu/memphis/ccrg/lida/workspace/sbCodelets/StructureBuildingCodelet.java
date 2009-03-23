package edu.memphis.ccrg.lida.workspace.sbCodelets;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.broadcasts.PreviousBroadcastsImpl;
import edu.memphis.ccrg.lida.workspace.csm.CSM;
import edu.memphis.ccrg.lida.workspace.csm.CSMContent;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;

public class StructureBuildingCodelet implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkTimer timer;
	//
	private CSM csm;
	private PerceptualBuffer pBuffer = null;
	private EpisodicBufferImpl eBuffer = null;
	private PreviousBroadcastsImpl pBroads = null;
	//
	private double activation = 1.0;
	private CodeletObjective objective = null;
	private CodeletAction action = new CodeletAction();
	
	private List<CodeletAccessible> buffers = new ArrayList<CodeletAccessible>();
			
	public StructureBuildingCodelet(FrameworkTimer t, PerceptualBuffer buffer, EpisodicBufferImpl eBuffer, 
					PreviousBroadcastsImpl pBroads, CSM csm, double activation, CodeletObjective obj, CodeletAction a){
		timer = t;
		pBuffer = buffer;
		this.eBuffer = eBuffer;
		this.pBroads = pBroads;
		this.csm = csm;
		this.activation = activation;
		objective = obj;
		action = a;
		
		if(this.eBuffer != null){buffers.add(eBuffer);}
		if(this.pBuffer != null){buffers.add(pBuffer);}
		if(this.pBroads != null){buffers.add(pBroads);}
			
	}
	
	public void run(){
		while(keepRunning){
			timer.checkForStartPause();
			if(pBuffer != null){
				WorkspaceContent perceptualContent = pBuffer.getObjective(objective);
				if(perceptualContent != null){
					WorkspaceContent updatedContent = action.getResultOfAction(perceptualContent);
					csm.addWorkspaceContent(updatedContent);
				}
			}//pBuffer
			if(pBroads != null){
				
			}//pBroads
			if(eBuffer != null){
				
			}//eBuffer			
		}//while		
	}//run
	
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
