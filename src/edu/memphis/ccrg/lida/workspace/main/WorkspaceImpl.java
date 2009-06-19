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
import edu.memphis.ccrg.lida.workspace.BroadcastBuffer.BroadcastBuffer;
import edu.memphis.ccrg.lida.workspace.BroadcastBuffer.BroadcastBufferListener;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMListener;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferListener;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferListener;

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
									  PerceptualBufferListener,
									  BroadcastBufferListener,
									  EpisodicBufferListener, 
									  CSMListener{
	
	//Workspace contains these components
	private PerceptualBuffer perceptualBuffer;
	private EpisodicBuffer episodicBuffer;
	private BroadcastBuffer broadcastBuffer;
	private CurrentSituationalModel csm;
	
	//These listeners listen to the Workspace
	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private WorkspaceListener pamWorkspaceListener;
	private WorkspaceListener sbCodeletWorkspaceListener;
	
	public WorkspaceImpl(PerceptualBuffer pb, EpisodicBuffer eb, 
						 BroadcastBuffer pbroads, CurrentSituationalModel csm){
		perceptualBuffer = pb;
		episodicBuffer = eb;
		broadcastBuffer = pbroads;
		this.csm = csm;	
	}//
	
	//****Output from the Workspace to other modules
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}
	
	public void addPamListener(WorkspaceListener listener){
		pamWorkspaceListener = listener;
	}
	
	public void addCodeletListener(WorkspaceListener listener){
		sbCodeletWorkspaceListener = listener;
	}
	
	public void cue(WorkspaceContent content){
		for(CueListener c: cueListeners)
			c.receiveCue(content);
	}
	
	public void sendContentToPAM(WorkspaceContent content){
		pamWorkspaceListener.receiveWorkspaceContent(content);
	}
	
	//WorkspaceImpl listens to its submodules and forwards the content 
	// that they send to WorkspaceImpl to the appropriate modules outside the workspace.
	//
	//1. Contents from the 3 buffers is sent to the sb codelet driver for context-sensitive
	//    codelet activation. 
	//2. Contents from perceptual & episodic buffers as well as the csm cues the episodic memories
	//3. Episodic memories are sent to PAM for top-down PAM activation
	//TODO May want to also activate PAM based on new representations created by codelets
	//that get produced and put in the CSM
	public void receivePBufferContent(WorkspaceContent content){
		sbCodeletWorkspaceListener.receiveWorkspaceContent(content);	
		cue(content);
	}
	public void receivePrevBroadcastContent(WorkspaceContent content) {
		sbCodeletWorkspaceListener.receiveWorkspaceContent(content);			
	}
	public void receiveEBufferContent(WorkspaceContent content) {
		pamWorkspaceListener.receiveWorkspaceContent(content);
		sbCodeletWorkspaceListener.receiveWorkspaceContent(content);		
		cue(content);
	}
	public void receiveCSMContent(WorkspaceContent content) {
		cue(content);		
	}

	//****Input into the Workspace from other Modules is sent to the appropriate
	//submodules
	public void receivePAMContent(NodeStructure ns) {
		perceptualBuffer.receivePAMContent(ns);		
	}
	public void receiveLocalAssociation(WorkspaceContent association) {
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
	public void addContentToCSM(WorkspaceContent updatedContent) {
		csm.addWorkspaceContent(updatedContent);		
	}

	/**
	 * for codelets to access the buffers
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
	public BroadcastBuffer getBroadcastBuffer(){
		return broadcastBuffer;
	}
	
}//class