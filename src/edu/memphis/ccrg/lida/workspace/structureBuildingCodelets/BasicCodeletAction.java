package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public class BasicCodeletAction implements CodeletAction {

	public void performAction(List<NodeStructure> buffer, CodeletWritable cr) {			
		cr.addCodeletContent(buffer.get(0));		
	}//method

}//class