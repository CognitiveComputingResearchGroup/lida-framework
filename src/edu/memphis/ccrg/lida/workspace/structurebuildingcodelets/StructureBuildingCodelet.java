package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public interface StructureBuildingCodelet extends LidaTask{

	 public void setSoughtContent(NodeStructure content);
	 public NodeStructure getSoughtContent();
	
	 public void setCodeletAction(CodeletAction a);
	 public CodeletAction getCodeletAction();

	 public void addAccessibleModule(WorkspaceBuffer module);
	 public Set<WorkspaceBuffer> getAccessibleModules();
	
	 /**
	  * Type is determined by what buffers are accessible to this codelet
	  * @return
	  */
	 public int getType();

}//interface