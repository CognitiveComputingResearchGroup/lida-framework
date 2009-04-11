package edu.memphis.ccrg.lida.workspace.main;

import java.util.List;

import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemory;
import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemoryContent;
import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemoryListener;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.perception.PAMContent;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemory;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemoryContent;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemoryListener;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

/**
 * 
 * TODO: Not yet tested.
 * 
 * @author ryanjmccall
 *
 */
public class WorkspaceImpl implements Workspace, PAMListener, 
									  TransientEpisodicMemoryListener, 
									  DeclarativeMemoryListener,
									  BroadcastListener, 
									  ActionSelectionListener{
	
	//Workspace contains these components
	private PerceptualBuffer perceptualBuffer;
	private EpisodicBuffer episodicBuffer;
	private PreviousBroadcasts prevBroads;
	private CurrentSituationalModel csm;
	
	//These listeners listen to the Workspace
	private List<WorkspaceListener> listeners;
	private WorkspaceContent content;
	
	public WorkspaceImpl(PerceptualBuffer pb, EpisodicBuffer eb, PreviousBroadcasts pbroads, CurrentSituationalModel csm, 
						TransientEpisodicMemory tem, DeclarativeMemory dm, PerceptualAssociativeMemory pam){
		perceptualBuffer = pb;
		episodicBuffer = eb;
		prevBroads = pbroads;
		this.csm = csm;	
		
		//TODO: Either each component has a reference to the workspace and
		//      calls the workspace to do output _OR_
		// Each component sends the output itself (e.g. episodic buffer sends to PAM; CSM sends to TEM and DM)
		// 
		
//		pb.addWorkspaceObjectReference(this);
//		eb.addWorkspaceObjectReference(this);
//		pbroads.addWorkspaceObjectReference(this);
//		csm.addWorkspaceObjectReference(this);		
	}//constructor
	
	//****Output from the Workspace to other modules
	public void addWorkspaceListener(WorkspaceListener listener) {
		listeners.add(listener);		
	}
	
	public void sendWorkspaceContent(){
		for(WorkspaceListener l: listeners)
			l.receiveWorkspaceContent(content);
	}

	//****Input into the Workspace from other Modules
	public void receivePAMContent(PAMContent pc) {
		perceptualBuffer.receivePAMContent(pc);		
	}
	public void receiveTEMContent(TransientEpisodicMemoryContent association) {
		episodicBuffer.receiveTEMContent(association);		
	}	
	public void receivenDMContent(DeclarativeMemoryContent association) {
		episodicBuffer.receivenDMContent(association);		
	}
	public void receiveBroadcast(BroadcastContent bc) {
		prevBroads.receiveBroadcast(bc);		
	}
	public void receiveBehaviorContent(ActionContent c) {
		// TODO: Implementing this is a long way off as of (3.30.09)		
	}

	public void addContentToCSM(WorkspaceContent updatedContent) {
		csm.addWorkspaceContent(updatedContent);		
	}

	public CodeletReadable getModuleReference(int moduleID) {
		if(moduleID == Workspace.CSM){
			return null;
		}else if(moduleID == Workspace.PBUFFER){
			return perceptualBuffer;
		}else if(moduleID == Workspace.EBUFFER){
			return episodicBuffer;
		}else if(moduleID == Workspace.PBROADS){
			return prevBroads;
		}
		return null;
	}//method
	
}//class
