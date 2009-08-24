package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public class BasicCodeletAction implements CodeletAction {

	public void performAction(Collection<NodeStructure> buffer, CodeletWritable destination) {
		for(NodeStructure ns: buffer)
			performAction(ns, destination);
	}//method

	public void performAction(NodeStructure buffer, CodeletWritable destination) {
		destination.addCodeletContent(buffer);		
	}

}//class