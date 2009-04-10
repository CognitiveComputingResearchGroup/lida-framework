package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface CurrentSituationalModel {

	NodeStructure getContent();

	void sendCSMContent();

	boolean hasContent(NodeStructure whatIwant);
	
	

}
