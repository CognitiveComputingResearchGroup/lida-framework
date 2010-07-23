package edu.memphis.ccrg.lida.workspace.main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;
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

public class WorkspaceImpl extends LidaModuleImpl implements Workspace,  
									  PamListener, 
									  LocalAssociationListener,
									  BroadcastListener, 
									  SensoryMotorListener{
	
	private static Logger logger = Logger.getLogger("lida.workspace.main.Workpace");

	/**
	 * TEM and DM are cue listeners
	 */
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private List<WorkspaceListener> wsListeners = new ArrayList<WorkspaceListener>();

	private static final int DEFAULT_DECAY_TIME=10; 

	//Workspace contains these components
//	private WorkspaceBuffer episodicBuffer;
//	private WorkspaceBuffer perceptualBuffer;
//	private BroadcastQueue broadcastQueue;
//	private WorkspaceBuffer csm;
	private int decayCounter = 0;
	private int decayTime=DEFAULT_DECAY_TIME;
	private double lowerActivationBound;
			
	/**
	 * @param lowerActivationBound the lowerActivationBound to set
	 */
	public void setLowerActivationBound(double lowerActivationBound) {
		this.lowerActivationBound = lowerActivationBound;
		for (LidaModule lm:getSubmodules().values()){
			((WorkspaceBuffer)lm).setLowerActivationBound(lowerActivationBound);
		}
	}

	/**
	 * @param decayTime
	 *            the decayTime to set
	 */
	public void setDecayTime(int decayTime) {
		this.decayTime = decayTime;
	}

	
	public WorkspaceImpl(WorkspaceBuffer episodicBuffer, WorkspaceBuffer perceptualBuffer,WorkspaceBuffer csm, BroadcastQueue broadcastQueue ){
		super (ModuleName.Workspace);
		getSubmodules().put(ModuleName.EpisodicBuffer,episodicBuffer);
		getSubmodules().put(ModuleName.BroadcastQueue,broadcastQueue);
		getSubmodules().put(ModuleName.PerceptualBuffer,perceptualBuffer);
		getSubmodules().put(ModuleName.CurrentSituationalModel,csm);
		
		for (LidaModule lm:getSubmodules().values()){
			((WorkspaceBuffer)lm).setLowerActivationBound(lowerActivationBound);
		}
	}

	public WorkspaceImpl (){
		super (ModuleName.Workspace);		
	}

	//Implementations for Workspace interface
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}

	public void addWorkspaceListener(WorkspaceListener listener){
		wsListeners.add(listener);
	}
	
	public void cue(NodeStructure content){
		for(CueListener c: cueListeners){
			c.receiveCue(content);
		}
		logger.log(Level.FINER,"Cue Performed ",LidaTaskManager.getActualTick());
	}
	private void sendToListeners(NodeStructure content){
		for(WorkspaceListener c: wsListeners){
			c.receiveWorkspaceContent(ModuleName.EpisodicBuffer, (WorkspaceContent)content);
		}
	}
	/**
	 * Not applicable for WorkspaceImpl
	 */
	public void learn() {}
	
	/**
	 * Received broadcasts are sent to the broadcast queue.
	 */
	public void receiveBroadcast(BroadcastContent bc) {
		((BroadcastListener)getSubmodule(ModuleName.BroadcastQueue)).receiveBroadcast(bc);	
	}
	
	/**
	 * Received local associations are merged into the episodic buffer.
	 * Then they are sent to PAM.
	 */
	public void receiveLocalAssociation(NodeStructure association) {
		WorkspaceContent ns = (WorkspaceContent) getSubmodule(ModuleName.EpisodicBuffer).getModuleContent(); 
		ns.mergeWith(association);
		sendToListeners(ns);
	}
	/**
	 * Implementation of the PamListener interface. Send received node to the
	 * the perceptualBuffer.
	 */
	public void receiveNode(Node node) {
		//System.out.println("adding " + node.getLabel());
		//System.out.println("adding node to percept. buffer " + getSubmodule(ModuleName.PerceptualBuffer));
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).addNode(node);
		
		//NodeStructure pb = (NodeStructure) getSubmodule(ModuleName.PerceptualBuffer).getModuleContent();
		//System.out.println("after adding the buffer has " + pb.getNodeCount());
	}
	
	/**
	 * Implementation of the PamListener interface.  Send received link to the
	 * the perceptualBuffer.
	 */
	public void receiveLink(Link l) {
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).addLink(l);
	}	

	
	/**
	 * Implementation of the PamListener interface.  Send received Node Structure to the
	 * the perceptualBuffer.
	 */
	public void receiveNodeStructure(NodeStructure ns) {
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).mergeWith(ns);	
	}
	

	/**
	 * Decays all Nodes of all buffers in the Workspace
	 */
//	public void decayWorkspaceNodes(){
//		decayCounter++;
//		if (decayCounter >= decayTime) {
//			logger.log(Level.FINER,"Decaying all workspace buffer content",LidaTaskManager.getActualTick());
//			decayCounter=0;
//			perceptualBuffer.decayNodes(lowerActivationBound);
//			csm.decayNodes(lowerActivationBound);
//			episodicBuffer.decayNodes(lowerActivationBound);
//			broadcastQueue.decayNodes(lowerActivationBound);
//		}
//	}

	public Object getModuleContent() {
		return null;
	}
	public void addListener(ModuleListener listener) {
		if (listener instanceof WorkspaceListener){
			addWorkspaceListener((WorkspaceListener)listener);
		}else if (listener instanceof CueListener){
			addCueListener((CueListener)listener);
		}
	}

	@Override
	public void receiveAction(LidaAction a) {
		// TODO Maybe just pam receives this and not the workspace		
	}
	
}//class