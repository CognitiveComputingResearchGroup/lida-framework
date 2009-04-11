package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public interface CurrentSituationalModel{

	NodeStructure getContent();

	void sendCSMContent();

	boolean hasContent(NodeStructure whatIwant);

	void addWorkspaceContent(WorkspaceContent updatedContent);
	
	

}
