package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.Set;

import edu.memphis.ccrg.lida._perception.LinkImpl;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class TestCodeletAction implements CodeletAction{
	
	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
		NodeStructure struct = (NodeStructure)content.getContent();
		Set<Node> nodes = struct.getNodes();
		if(nodes != null)
			struct.addLink(new LinkImpl(null, null, null, 102390));

		return content;
	}//method

}
