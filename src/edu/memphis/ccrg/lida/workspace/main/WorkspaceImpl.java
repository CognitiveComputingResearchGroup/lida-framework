package edu.memphis.ccrg.lida.workspace.main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

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

public class WorkspaceImpl implements Workspace,  
									  PamListener, 
									  LocalAssociationListener,
									  BroadcastListener, 
									  ActionSelectionListener{
	
	private static Logger logger = Logger.getLogger("lida.workspace.main.WorkpaceImpl");

	/**
	 * TEM and DM are cue listeners
	 */
	private List<CueListener> cueListeners = new ArrayList<CueListener>();

	private static final int DEFAULT_DECAY_TIME=10; 

	//Workspace contains these components
	private WorkspaceBuffer episodicBuffer;
	private WorkspaceBuffer perceptualBuffer;
	private BroadcastQueue broadcastQueue;
	private WorkspaceBuffer csm;
	private int decayCounter = 0;
	private int decayTime=DEFAULT_DECAY_TIME;
	private double lowerActivationBound;
	
	/**
	 * PAM also listens.
	 */
	private WorkspaceListener pamWorkspaceListener;
		
	/**
	 * @param lowerActivationBound the lowerActivationBound to set
	 */
	public void setLowerActivationBound(double lowerActivationBound) {
		this.lowerActivationBound = lowerActivationBound;
	}

	/**
	 * @param decayTime
	 *            the decayTime to set
	 */
	public void setDecayTime(int decayTime) {
		this.decayTime = decayTime;
	}

	
	public WorkspaceImpl(WorkspaceBuffer episodicBuffer, WorkspaceBuffer perceptualBuffer,WorkspaceBuffer csm, BroadcastQueue broadcastQueue ){
		this.episodicBuffer = episodicBuffer;
		this.broadcastQueue = broadcastQueue;
		this.perceptualBuffer=perceptualBuffer;
		this.csm = csm;	
	}
	
	//Implementations for Workspace interface
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}

	public void addPamWorkspaceListener(WorkspaceListener listener){
		pamWorkspaceListener = listener;
	}
	public void cue(NodeStructure content){
		for(CueListener c: cueListeners){
			c.receiveCue(content);
		}
	}
	public BroadcastQueue getBroadcastQueue(){
		return broadcastQueue;
	}
	
	public WorkspaceBuffer getCSM() {
		return csm;
	}
	public WorkspaceBuffer getEpisodicBuffer(){
		return episodicBuffer;
	}

	
	/**
	 * @return the perceptualBuffer
	 */
	public WorkspaceBuffer getPerceptualBuffer() {
		return perceptualBuffer;
	}
	/**
	 * Not applicable for WorkspaceImpl
	 */
	public void learn() {}
	
	/**
	 * TODO: Implementing this is a long way off as of (3.30.09)	
	 */
	public void receiveAction(LidaAction c) {
	}
	/**
	 * Received broadcasts are sent to the broadcast queue.
	 */
	public void receiveBroadcast(BroadcastContent bc) {
		((BroadcastListener) broadcastQueue).receiveBroadcast(bc);		
	}
	
	/**
	 * Received local associations are merged into the episodic buffer.
	 * Then they are sent to PAM.
	 */
	public void receiveLocalAssociation(NodeStructure association) {
		WorkspaceContent ns = (WorkspaceContent) episodicBuffer.getModuleContent(); 
		ns.mergeWith(association);
		pamWorkspaceListener.receiveWorkspaceContent(ModuleType.EpisodicBuffer, ns);
	}
	/**
	 * Implementation of the PamListener interface. Send received node to the
	 * the perceptualBuffer.
	 */
	public void receiveNode(Node node) {
		perceptualBuffer.getModuleContent().mergeWith(node);	
	}
	
	/**
	 * Implementation of the PamListener interface.  Send received link to the
	 * the perceptualBuffer.
	 */
	public void receiveLink(Link l) {
		perceptualBuffer.getModuleContent().mergeWith(l);
	}	

	
	/**
	 * Implementation of the PamListener interface.  Send received Node Structure to the
	 * the perceptualBuffer.
	 */
	public void receiveNodeStructure(NodeStructure ns) {
		perceptualBuffer.getModuleContent().mergeWith(ns);	
	}
	

	/**
	 * Decays all Nodes of all buffers in the Workspace
	 */
	public void decayWorkspaceNodes(){
		decayCounter++;
		if (decayCounter >= decayTime) {
			logger.log(Level.FINER,"Decaying all workspace buffer content",LidaTaskManager.getActualTick());
			decayCounter=0;
			perceptualBuffer.decayNodes(lowerActivationBound);
			csm.decayNodes(lowerActivationBound);
			episodicBuffer.decayNodes(lowerActivationBound);
			broadcastQueue.decayNodes(lowerActivationBound);
		}
	}
	
}//class