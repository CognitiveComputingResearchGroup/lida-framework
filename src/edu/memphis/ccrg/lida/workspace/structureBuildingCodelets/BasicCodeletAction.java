package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public class BasicCodeletAction implements CodeletAction {

	public void performAction(CodeletReadable buffer, CodeletWritable cr) {		
		List<NodeStructure> content = buffer.getBuffer(CodeletReadable.PERCEPTUAL_BUFFER);	
		cr.addCodeletContent(content.get(0));		
	}//method

}//class