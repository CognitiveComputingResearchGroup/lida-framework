package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.impl;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsImpl;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.StructureBuildingCodelet;

public class StructureBuildingCodeletImpl implements Runnable, Stoppable, StructureBuildingCodelet{
	
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkTimer timer;
	//
	private CurrentSituationalModelImpl csm;
	private PerceptualBufferImpl pBuffer = null;
	private EpisodicBufferImpl eBuffer = null;
	private PreviousBroadcastsImpl pBroads = null;
	//
	private double activation = 1.0;
	private CodeletsDesiredContent objective = null;
	private CodeletAction action = new SpatialLinkCodeletAction();
	
	private List<CodeletAccessible> buffers = new ArrayList<CodeletAccessible>();
			
	public StructureBuildingCodeletImpl(FrameworkTimer t, PerceptualBufferImpl buffer, EpisodicBufferImpl eBuffer, 
					PreviousBroadcastsImpl pBroads, CurrentSituationalModelImpl csm, double activation, CodeletsDesiredContent obj, CodeletAction a){
		
		if(buffer == null && eBuffer == null && pBroads == null){
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("Codelet needs at least one source buffer");
				e.printStackTrace();
			}
		}else{
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
		}//else
			
	}//constructor
	
	public void run(){
		while(keepRunning){
			try{Thread.sleep(100);}catch(Exception e){}
			timer.checkForStartPause();
			
			for(CodeletAccessible buffer: buffers)
				checkAndWorkOnBuffer(buffer);		
		}//while		
	}//run
	
	private void checkAndWorkOnBuffer(CodeletAccessible buffer) {
		WorkspaceContent bufferContent = buffer.getCodeletsObjective(objective);
		if(bufferContent != null){
			WorkspaceContent updatedContent = action.getResultOfAction(bufferContent);
			csm.addWorkspaceContent(updatedContent);
		}else{
			System.out.println("codelet is getting null buffer content");
		}
	}//checkAndWorkOnBuffer

	public void setActivation(double a){
		activation = a;
	}
	public void setContext(CodeletsDesiredContent obj){
		objective = obj;
	}
	public void setCodeletAction(CodeletAction a){
		action = a;
	}
	public double getActivation(){
		return activation;
	}
	public CodeletsDesiredContent getObjective(){
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
