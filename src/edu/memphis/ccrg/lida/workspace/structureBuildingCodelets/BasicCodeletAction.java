package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

public class BasicCodeletAction implements CodeletAction {

	public void performAction(CodeletReadable buffer, Workspace w) {		
		List<NodeStructure> content = buffer.getBuffer();	
		w.addContentToCSM(content.get(0));		
	}//method

}//class