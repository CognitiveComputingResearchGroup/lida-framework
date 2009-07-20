package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientEpisodicMemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastBuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;

public interface Workspace{
	 
	public abstract void addContentToCSM(NodeStructure updatedContent);
	public abstract CurrentSituationalModel getCSM();	
	public abstract PerceptualBuffer getPerceptualBuffer();	
	public abstract EpisodicBuffer getEpisodicBuffer();
	public abstract BroadcastQueue getBroadcastBuffer();
	public abstract void addCueListener(CueListener l);
	
}
