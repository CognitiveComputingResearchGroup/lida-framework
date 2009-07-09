package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public interface ContentDetectBehavior {
	
	public abstract boolean hasSoughtContent(CurrentSituationalModel csm);

	public abstract NodeStructure getSoughtContent(CurrentSituationalModel csm);

}
