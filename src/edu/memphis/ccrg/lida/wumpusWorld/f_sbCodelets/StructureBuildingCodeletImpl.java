package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.StructureBuildingCodelet;

public class StructureBuildingCodeletImpl implements Runnable, Stoppable, StructureBuildingCodelet{
	
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkTimer timer;
	//
	private Workspace workspace;
	//
	private double activation = 1.0;
	private CodeletsDesiredContent soughtContent = null;
	private CodeletAction action = new SpatialLinkCodeletAction();
	
	private List<CodeletReadable> buffersIuse = new ArrayList<CodeletReadable>();
			
	public StructureBuildingCodeletImpl(FrameworkTimer t, List<CodeletReadable> buffers, 
										double activation, CodeletsDesiredContent obj, CodeletAction a){
		timer = t;
		this.activation = activation;
		soughtContent = obj;
		action = a;
	}//constructor
	
	public void run(){
		while(keepRunning){
			try{Thread.sleep(100);}catch(Exception e){}
			timer.checkForStartPause();
			for(CodeletReadable buffer: buffersIuse)
				checkAndWorkOnBuffer(buffer);		
		}//while		
	}//run
	
	private void checkAndWorkOnBuffer(CodeletReadable buffer){		
		WorkspaceContent bufferContent = buffer.getCodeletsDesiredContent(soughtContent);
		
		if(bufferContent != null){
			WorkspaceContent updatedContent = action.getResultOfAction(bufferContent);			
			workspace.addContentToCSM(updatedContent);
		}else{
			System.out.println("codelet is getting null buffer content");
		}
	}//checkAndWorkOnBuffer


	public void setActivation(double a){
		activation = a;
	}
	public void setContext(CodeletsDesiredContent content){
		soughtContent = content;
	}
	public void setCodeletAction(CodeletAction a){
		action = a;
	}
	public double getActivation(){
		return activation;
	}
	public CodeletsDesiredContent getObjective(){
		return soughtContent;
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

	public void decay() {
		// TODO Auto-generated method stub
		
	}

	public void excite(double amount) {
		// TODO Auto-generated method stub
		
	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub
		
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub
		
	}

}//class SBCodelet
