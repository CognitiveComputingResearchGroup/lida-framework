package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public class BasicCodeletAction implements CodeletAction {

	public void performAction(Collection<NodeStructure> buffer, CodeletWritable cr) {
		for(NodeStructure ns: buffer)
			cr.addCodeletContent(ns);
	}//method

}//class