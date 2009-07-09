package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletWritable;

public interface CurrentSituationalModel extends CodeletReadable, CodeletWritable{

	public abstract void addWorkspaceContent(NodeStructure updatedContent);
	public abstract void sendCSMContent();
	public abstract NodeStructure getCSMContent();
	
}//interface
