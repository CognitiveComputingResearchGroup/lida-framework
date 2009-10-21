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

/**
 * 
 * This class implements the Facade pattern.  The Workspace contains the Perceptual and 
 * Episodic Buffers as well as the Broadcast Queue and Current Situational Model.
 * Any outside module that wishes to access and/or modify these Workspace components must do 
 * so through this class.  Thus this class defines the methods to access the 
 * data of these submodules.
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
	//private PerceptualBuffer perceptualBuffer;
	private EpisodicBuffer episodicBuffer;
	private BroadcastQueue broadcastQueue;
	private CurrentSituationalModel csm;
	
	//These listeners listen buffers of the Workspace
	/**
	 * TEM and DM are cue listeners
	 */
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	/**
	 * PAM also listens.
	 */
	private WorkspaceListener pamWorkspaceListener;
	
	public WorkspaceImpl(EpisodicBuffer eb, BroadcastQueue bq, 
					     CurrentSituationalModel csm){
		episodicBuffer = eb;
		broadcastQueue = bq;
		this.csm = csm;	
		
		//Register the Workspace as a listener of these buffers:
		episodicBuffer.addBufferListener(this);
		this.csm.addBufferListener(this);
	}//

	
	//Methods to add listeners..
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
	
	1. Contents from perceptual & episodic buffers as well as the csm cues the episodic memories
	2. Additionally episodic buffer content is sent to PAM for grounded episodic recall
		TODO: May want to also activate PAM based on new representations created by codelets
		that get produced and put in the CSM
	 */ 	 
	public void receiveBufferContent(Module originatingBuffer, NodeStructure content) {
		if(originatingBuffer == Module.EpisodicBuffer)
			pamWorkspaceListener.receiveWorkspaceContent(Module.EpisodicBuffer, content);
		cue(content);
	}//method
	public void cue(NodeStructure content){
		for(CueListener c: cueListeners){
			c.receiveCue(content);
		}
	}
	
	/**
	 * Implementation of the PamListener interface.  Send received Node Structure to the
	 * the csm.
	 */
	public void receiveNodeStructure(NodeStructure ns) {
		((PamListener) csm).receiveNodeStructure(ns);		
	}
	/**
	 * Implementation of the PamListener interface.  Send received link to the
	 * the csm.
	 */
	public void receiveLink(Link l) {
		((PamListener) csm).receiveLink(l);
	}
	
	/**
	 * Implementation of the PamListener interface.  Send received node to the
	 * the csm.
	 */
	public void receiveNode(PamNode node) {
		((PamListener) csm).receiveNode(node);
	}
	
	/**
	 * Received local associations are sent to the episodic buffer
	 */
	public void receiveLocalAssociation(NodeStructure association) {
		((LocalAssociationListener) episodicBuffer).receiveLocalAssociation(association);		
	}	

	/**
	 * Received broadcasts are sent to the broadcast queue.
	 */
	public void receiveBroadcast(BroadcastContent bc) {
		((BroadcastListener) broadcastQueue).receiveBroadcast(bc);		
	}
	/**
	 * TODO: Implementing this is a long way off as of (3.30.09)	
	 */
	public void receiveBehaviorContent(ActionContent c) {
	}

	/**
	 * Codelets use this to access CSM
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
	public EpisodicBuffer getEpisodicBuffer(){
		return episodicBuffer;
	}
	public BroadcastQueue getBroadcastQueue(){
		return broadcastQueue;
	}

	/**
	 * Not applicable for WorkspaceImpl
	 */
	public void learn() {}
	
}//class