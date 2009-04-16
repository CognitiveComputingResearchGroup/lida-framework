package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
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
	private CodeletsDesiredContent soughtContent = null;
	private CodeletAction action = new SpatialLinkCodeletAction();
	
	private Map<Integer, Boolean> buffersIuse = new HashMap<Integer, Boolean>();
			
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
			soughtContent = obj;
			action = a;
			
			if(usesPBuffer){
				buffersIuse.put(Workspace.PBUFFER, true);
			}else if(usesEBuffer){
				buffersIuse.put(Workspace.EBUFFER, true);
			}else if(usesPBroads){
				buffersIuse.put(Workspace.PBROADS, true);			
			}			
		}//else
			
	}//constructor
	
	public void run(){
		while(keepRunning){
			try{Thread.sleep(100);}catch(Exception e){}
			timer.checkForStartPause();
			for(Integer i: buffersIuse.keySet())
				if(buffersIuse.get(i))
					checkAndWorkOnBuffer(i);		
		}//while		
	}//run
	
	private void checkAndWorkOnBuffer(int i){		
		WorkspaceContent bufferContent = workspace.getCodeletDesiredContent(i, soughtContent);
		
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

}//class SBCodelet
