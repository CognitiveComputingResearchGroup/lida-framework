package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletWritable;

public interface CurrentSituationalModel extends CodeletWritable, CodeletAccessible{

	public abstract void addWorkspaceContent(NodeStructure updatedContent);
	public abstract void sendCSMContent();
	public abstract NodeStructure getModel();
	public abstract void addBufferListener(WorkspaceBufferListener workspace);
	
}//interface
