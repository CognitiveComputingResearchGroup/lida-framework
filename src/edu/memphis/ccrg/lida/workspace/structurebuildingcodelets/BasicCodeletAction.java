package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class BasicCodeletAction implements CodeletAction {

	public void performAction(WorkspaceBuffer buffer, WorkspaceBuffer csm) {
		NodeStructure ns = (NodeStructure) csm.getModuleContent();
		ns.mergeWith((NodeStructure) buffer.getModuleContent());
	} 


}//class