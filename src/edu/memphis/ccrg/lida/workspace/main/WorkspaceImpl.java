package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida._perception.interfaces.PAMContent;
import edu.memphis.ccrg.lida.actionSelection.BehaviorContent;
import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemoryContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemoryContent;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcasts;


/**
 * 
 * TODO: Not yet tested.
 * 
 * @author ryanjmccall
 *
 */
public class WorkspaceImpl implements Workspace{
	
	private PerceptualBuffer perceptualBuffer;
	private EpisodicBuffer episodicBuffer;
	private PreviousBroadcasts prevBroads;
	private CurrentSituationalModel csm;
	private BroadcastContent currentBroadcast;
	
	public WorkspaceImpl(PerceptualBuffer pb, EpisodicBuffer eb, 
						PreviousBroadcasts pbroads, CurrentSituationalModel csm){
		perceptualBuffer = pb;
		episodicBuffer = eb;
		prevBroads = pbroads;
		this.csm = csm;	
	}

	public void receivePAMContent(PAMContent pc) {
		perceptualBuffer.receivePAMContent(pc);		
	}

	public void receiveTEMLocalAssociation(
			TransientEpisodicMemoryContent association) {
		episodicBuffer.receiveTEMLocalAssociation(association);		
	}
	
	public void receivenDMContent(DeclarativeMemoryContent association) {
		episodicBuffer.receivenDMContent(association);		
	}

	public void receiveBroadcast(BroadcastContent bc) {
		prevBroads.receiveBroadcast(bc);		
	}

	public void receiveBehaviorContent(BehaviorContent c) {
		// TODO: Implementing this is a long way off (3.30.09)		
	}

}//class
