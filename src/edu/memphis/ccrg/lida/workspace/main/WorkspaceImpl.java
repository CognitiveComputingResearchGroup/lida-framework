package edu.memphis.ccrg.lida.workspace.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import edu.memphis.ccrg.lida.workspace.broadcastqueue.BroadcastQueue;
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

public class WorkspaceImpl extends LidaModuleImpl implements Workspace, PamListener, 
									  	LocalAssociationListener,
									  	BroadcastListener, 
									  	SensoryMotorListener{
	
	private static Logger logger = Logger.getLogger("lida.workspace.main.Workpace");

	/**
	 * TEM and DM are cue listeners
	 */
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private List<WorkspaceListener> wsListeners = new ArrayList<WorkspaceListener>();

	private double lowerActivationBound = 0.0;
	
	public WorkspaceImpl(WorkspaceBuffer episodicBuffer, WorkspaceBuffer perceptualBuffer,WorkspaceBuffer csm, BroadcastQueue broadcastQueue ){
		super (ModuleName.Workspace);
		getSubmodules().put(ModuleName.EpisodicBuffer,episodicBuffer);
		getSubmodules().put(ModuleName.BroadcastQueue,broadcastQueue);
		getSubmodules().put(ModuleName.PerceptualBuffer,perceptualBuffer);
		getSubmodules().put(ModuleName.CurrentSituationalModel,csm);
		
		setActivationLowerBound(lowerActivationBound);
	}
	public WorkspaceImpl(){
		super (ModuleName.Workspace);
	}
	
	/**
	 * @param lowerActivationBound the lowerActivationBound to set
	 */
	public void setActivationLowerBound(double lowerActivationBound) {
		this.lowerActivationBound = lowerActivationBound;
		for (LidaModule lm:getSubmodules().values()){
			((WorkspaceBuffer)lm).setLowerActivationBound(lowerActivationBound);
		}
	}
	
	public void init (Map<String,?> parameters){		
		Object o = parameters.get("workspace.activationLowerBound");
		if (o != null)
			setActivationLowerBound((Double) o);
		else
			logger.warning("Unable to set Activation lower bound parameter, using the default instead");
		
//		System.out.println("submodules " + getSubmodules().size());
//		getSubmodules().get(ModuleName.EpisodicBuffer).init(parameters);
//		getSubmodules().get(ModuleName.BroadcastQueue).init(parameters);
//		getSubmodules().get(ModuleName.PerceptualBuffer).init(parameters);
//		getSubmodules().get(ModuleName.CurrentSituationalModel).init(parameters);
	}
	
	public void addListener(ModuleListener listener) {
		if (listener instanceof WorkspaceListener){
			addWorkspaceListener((WorkspaceListener)listener);
		}else if (listener instanceof CueListener){
			addCueListener((CueListener)listener);
		}
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
		for(WorkspaceListener listener: wsListeners){
			listener.receiveWorkspaceContent(ModuleName.EpisodicBuffer, (WorkspaceContent)content);
		}
	}
	
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
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).addNode(node);
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

	public Object getModuleContent() {
		return null;
	}

	@Override
	public void receiveAction(LidaAction a) {
		// TODO Maybe just pam receives this and not the workspace		
	}
	
	/**
	 * Not applicable for WorkspaceImpl
	 */
	public void learn() {}
	
	public void addSubModule(LidaModule lm){
		super.addSubModule(lm);
		//System.out.println(lm.getModuleName());
		
	}
	
}//class