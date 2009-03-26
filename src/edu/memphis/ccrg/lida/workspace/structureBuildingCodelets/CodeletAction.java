package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.Set;


import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class CodeletAction{

	public WorkspaceContent getResultOfAction(WorkspaceContent content) {
		WorkspaceContent result = content;
		if(content != null){
			NodeStructure struct = (NodeStructure)content.getContent();
			if(struct != null){
				Set<Node> nodes = struct.getNodes();
				if(nodes != null){
					int i = 0;//TODO:
				}
					//System.out.println("codelet action " + nodes.size());
			}//struct != null
			
		}//content != null
		return result;
	}//method

}//class
