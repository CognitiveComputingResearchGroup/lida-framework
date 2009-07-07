package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface CurrentSituationalModel{

	public abstract void addWorkspaceContent(NodeStructure updatedContent);
	public abstract void sendCSMContent();
	public abstract boolean hasContent(NodeStructure whatIwant);
	public abstract NodeStructure getCSMContent();
	
}//interface
