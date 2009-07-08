package edu.memphis.ccrg.lida.workspace.main;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientEpisodicMemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastBuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;

/**
 * 
 * TODO: Not yet tested.
 * 
 * @author Ryan J. McCall
 *
 */
public class WorkspaceImpl implements Workspace, PAMListener, 
									  LocalAssociationListener,
									  BroadcastListener, 
									  ActionSelectionListener, 
									  WorkspaceBufferListener{
	
	//Workspace contains these components
	private PerceptualBuffer perceptualBuffer;
	private EpisodicBuffer episodicBuffer;
	private BroadcastQueue broadcastBuffer;
	private CurrentSituationalModel csm;
	
	//These listeners listen to the Workspace
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private WorkspaceListener pamWorkspaceListener;
	private WorkspaceListener sbCodeletWorkspaceListener;
	
	public WorkspaceImpl(PerceptualBuffer pb, EpisodicBuffer eb, BroadcastQueue pbroads, 
							CurrentSituationalModel csm){
		perceptualBuffer = pb;
		episodicBuffer = eb;
		broadcastBuffer = pbroads;
		this.csm = csm;	
	}//
	
	//****Output from the Workspace to other modules
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}
	
	public void add_PAM_WorkspaceListener(WorkspaceListener listener){
		pamWorkspaceListener = listener;
	}
	
	public void add_SBCodelet_WorkspaceListener(WorkspaceListener listener){
		sbCodeletWorkspaceListener = listener;
	}
	
	/**
	 * WorkspaceImpl listens to its submodules and forwards the content
	 * that they send to the appropriate modules outside the workspace.
	
	1. Content sent to the sbCodeletDriver for context-sensitive codelet activation. 
	2. Contents from perceptual & episodic buffers as well as the csm cues the episodic memories
	3. Episodic memories are sent to PAM for top-down PAM activation
		TODO May want to also activate PAM based on new representations created by codelets
		that get produced and put in the CSM
	 */ 	 
	public void receiveBufferContent(int buffer, NodeStructure content) {
		if(buffer == WorkspaceBufferListener.PBUFFER){
			sbCodeletWorkspaceListener.receiveWorkspaceContent(content, WorkspaceListener.FROM_PBUFFER);	
			cue(content);
		}else if(buffer == WorkspaceBufferListener.EBUFFER){
			pamWorkspaceListener.receiveWorkspaceContent(content, WorkspaceListener.FROM_EBUFFER);
			sbCodeletWorkspaceListener.receiveWorkspaceContent(content, WorkspaceListener.FROM_EBUFFER);		
			cue(content);
		}else if(buffer == WorkspaceBufferListener.CSM){
			cue(content);
		}else if(buffer == WorkspaceBufferListener.BQUEUE){
			sbCodeletWorkspaceListener.receiveWorkspaceContent(content, WorkspaceListener.FROM_BQUEUE);
		}
	}//method

	public void cue(NodeStructure content){
		for(CueListener c: cueListeners)
			c.receiveCue(content);
	}
	
	//****Input into the Workspace from other Modules is sent to the appropriate
	//submodules
	public void receivePAMContent(NodeStructure ns) {
		perceptualBuffer.receivePAMContent(ns);		
	}
	public void receiveLocalAssociation(NodeStructure association) {
		episodicBuffer.receiveLocalAssociation(association);		
	}	

	public void receiveBroadcast(BroadcastContent bc) {
		broadcastBuffer.receiveBroadcast(bc);		
	}
	public void receiveBehaviorContent(ActionContent c) {
		// TODO: Implementing this is a long way off as of (3.30.09)		
	}

	/**
	 * Codelets use this
	 */
	public void addContentToCSM(NodeStructure updatedContent) {
		csm.addWorkspaceContent(updatedContent);		
	}

	/**
	 * For Codelets to access the buffers
	 */
	public CurrentSituationalModel getCSM() {
		return csm;
	}
	public PerceptualBuffer getPerceptualBuffer(){
		return perceptualBuffer;
	}
	public EpisodicBuffer getEpisodicBuffer(){
		return episodicBuffer;
	}
	public BroadcastQueue getBroadcastBuffer(){
		return broadcastBuffer;
	}
	
}//class