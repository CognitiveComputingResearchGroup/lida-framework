package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.List;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public interface CurrentSituationalModel{

	public abstract void addWorkspaceContent(WorkspaceContent updatedContent);
	public abstract void sendCSMContent();
	public abstract boolean hasContent(NodeStructure whatIwant);
	public abstract NodeStructure getCSMContent();
	public abstract List<Object> getGuiContent();
	
}//interface
