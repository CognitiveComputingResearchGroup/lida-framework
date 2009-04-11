package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
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
	private CodeletsDesiredContent objective = null;
	private CodeletAction action = new SpatialLinkCodeletAction();
	
	private List<CodeletReadable> buffers = new ArrayList<CodeletReadable>();
			
	public StructureBuildingCodeletImpl(FrameworkTimer t, Workspace workspace, 
										boolean usesPBuffer, boolean usesEBuffer, boolean usesPBroads, 
										double activation, CodeletsDesiredContent obj, CodeletAction a){
		
		if(!usesPBuffer && !usesEBuffer && !usesPBroads){
			try{
				throw new Exception();
			}catch (Exception e){
				System.out.println("Codelet needs at least one source buffer");
				e.printStackTrace();
			}
		}else{
			timer = t;
			this.workspace = workspace;
			this.activation = activation;
			objective = obj;
			action = a;
			
			if(usesPBuffer){
				CodeletReadable cr = workspace.getModuleReference(Workspace.PBUFFER);
				if(cr != null)
					buffers.add(cr);
			}else if(usesEBuffer){
				CodeletReadable cr = workspace.getModuleReference(Workspace.EBUFFER);
				if(cr != null)
					buffers.add(cr);
			}else if(usesPBroads){
				CodeletReadable cr = workspace.getModuleReference(Workspace.PBROADS);
				if(cr != null)
					buffers.add(cr);				
			}			
		}//else
			
	}//constructor
	
	public void run(){
		while(keepRunning){
			try{Thread.sleep(100);}catch(Exception e){}
			timer.checkForStartPause();
			
			for(CodeletReadable buffer: buffers)
				checkAndWorkOnBuffer(buffer);		
		}//while		
	}//run
	
	private void checkAndWorkOnBuffer(CodeletReadable buffer) {
		WorkspaceContent bufferContent = buffer.getCodeletsObjective(objective);
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
