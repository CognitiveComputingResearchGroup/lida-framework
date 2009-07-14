package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.workspace.broadcastBuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;

public interface Workspace{
	 
	public abstract CurrentSituationalModel getCSM();	
	public abstract PerceptualBuffer getPerceptualBuffer();	
	public abstract EpisodicBuffer getEpisodicBuffer();
	public abstract BroadcastQueue getBroadcastBuffer();
	
}
