package edu.memphis.ccrg.lida.workspace.main;

import java.util.List;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public interface Workspace{
	
	public abstract CurrentSituationalModel getCSM();	
	public abstract List<NodeStructure> getPerceptualBuffer();	
	public abstract List<NodeStructure> getEpisodicBuffer();
	public abstract List<NodeStructure> getBroadcastQueue();
}
