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
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

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
//	private PerceptualBuffer perceptualBuffer;
//	private EpisodicBuffer episodicBuffer;
//	private BroadcastQueue broadcastBuffer;
	private WorkspaceBuffersImpl workspaceBuffers;
	private CurrentSituationalModel csm;
	
	//These listeners listen to the Workspace
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private WorkspaceListener pamWorkspaceListener;
	
//	public WorkspaceImpl(PerceptualBuffer pb, EpisodicBuffer eb, BroadcastQueue pbroads, 
//							CurrentSituationalModel csm){
//		perceptualBuffer = pb;
//		episodicBuffer = eb;
//		broadcastBuffer = pbroads;
//		this.csm = csm;	
//	}//
	
	public WorkspaceImpl(WorkspaceBuffersImpl buffers, CurrentSituationalModel csm){
		workspaceBuffers = buffers;
		this.csm = csm;
	}
	
	//****Output from the Workspace to other modules
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}
	
	public void add_PAM_WorkspaceListener(WorkspaceListener listener){
		pamWorkspaceListener = listener;
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
		cue(content);
		if(buffer == WorkspaceBufferListener.EBUFFER)
			pamWorkspaceListener.receiveWorkspaceContent(WorkspaceListener.FROM_EBUFFER, content);
	
	}//method

	public void cue(NodeStructure content){
		for(CueListener c: cueListeners)
			c.receiveCue(content);
	}
	
	//****Input into the Workspace from other Modules is sent to the appropriate
	//submodules
	public void receivePAMContent(NodeStructure ns) {
		workspaceBuffers.receivePAMContent(ns);		
	}
	public void receiveLocalAssociation(NodeStructure association) {
		workspaceBuffers.receiveLocalAssociation(association);		
	}	

	public void receiveBroadcast(BroadcastContent bc) {
		workspaceBuffers.receiveBroadcast(bc);		
	}
	public void receiveBehaviorContent(ActionContent c) {
		// TODO: Implementing this is a long way off as of (3.30.09)		
	}

	public List<NodeStructure> getBroadcastQueue() {
		return workspaceBuffers.getBroadcastQueue();
	}

	public CurrentSituationalModel getCSM() {
		return  csm;
	}

	public List<NodeStructure> getEpisodicBuffer() {
		return workspaceBuffers.getEpisodicBuffer();
	}

	public List<NodeStructure> getPerceptualBuffer() {
		return workspaceBuffers.getPerceptualBuffer();
	}
	
}//class