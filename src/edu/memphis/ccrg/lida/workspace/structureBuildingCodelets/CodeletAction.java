package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.workspace.main.Workspace;

public interface CodeletAction {

	public void performAction(CodeletReadable buffer, Workspace w);
	
}
