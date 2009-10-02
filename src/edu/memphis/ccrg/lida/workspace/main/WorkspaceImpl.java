package edu.memphis.ccrg.lida.workspace.main;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.actionselection.ActionContent;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBuffer;

/**
 * 
 * TODO: Not yet tested.
 * 
 * @author Ryan J. McCall
 *
 */
public class WorkspaceImpl implements Workspace, PamListener, 
									  LocalAssociationListener,
									  BroadcastListener, 
									  ActionSelectionListener, 
									  WorkspaceBufferListener{
	
	//Workspace contains these components
	private PerceptualBuffer perceptualBuffer;
	private EpisodicBuffer episodicBuffer;
	private BroadcastQueue broadcastQueue;
	private CurrentSituationalModel csm;
	
	//These listeners listen to the Workspace
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private WorkspaceListener pamWorkspaceListener;
	
	public WorkspaceImpl(PerceptualBuffer pb, EpisodicBuffer eb, BroadcastQueue bq, 
					     CurrentSituationalModel csm){
		perceptualBuffer = pb;
		episodicBuffer = eb;
		broadcastQueue = bq;
		this.csm = csm;	
		
		perceptualBuffer.addBufferListener(this);
		episodicBuffer.addBufferListener(this);
		this.csm.addBufferListener(this);
	}//

	
	//****Output from the Workspace to other modules
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}
	
	//check
	public void addPamWorkspaceListener(WorkspaceListener listener){
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
	public void receiveBufferContent(Module originatingBuffer, NodeStructure content) {
		if(originatingBuffer == Module.episodicBuffer)
			pamWorkspaceListener.receiveWorkspaceContent(Module.episodicBuffer, content);
		cue(content);
	}//method

	public void cue(NodeStructure content){
		for(CueListener c: cueListeners){
			c.receiveCue(content);
		}
	}
	
	//****Input into the Workspace from other Modules is sent to the appropriate
	//submodules
	public void receiveNodeStructure(NodeStructure ns) {
		((PamListener) perceptualBuffer).receiveNodeStructure(ns);		
	}
	public void receiveLink(Link l) {
		((PamListener) perceptualBuffer).receiveLink(l);
	}
	public void receiveNode(PamNode node) {
		((PamListener) perceptualBuffer).receiveNode(node);
	}
	
	public void receiveLocalAssociation(NodeStructure association) {
		((LocalAssociationListener) episodicBuffer).receiveLocalAssociation(association);		
	}	

	public void receiveBroadcast(BroadcastContent bc) {
		((BroadcastListener) broadcastQueue).receiveBroadcast(bc);		
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
	public BroadcastQueue getBroadcastQueue(){
		return broadcastQueue;
	}

	public void learn() {
		//Not applicable for WorkspaceImpl
	}
	
}//class