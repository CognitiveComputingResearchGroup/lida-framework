package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class BasicAttentionCodeletImpl extends AttentionCodeletImpl {
	
	public BasicAttentionCodeletImpl(){
		super();
	}
	
	@Override
	public boolean hasSoughtContent(WorkspaceBuffer buffer) {
		NodeStructure model = (NodeStructure) buffer.getModuleContent();
		Collection<Node> nodes = soughtContent.getNodes();
		Collection<Link> links = soughtContent.getLinks();
		for (Node n : nodes)
			if (!model.containsNode(n))
				return false;

		for (Link l : links)
			if (!model.containsLink(l))
				return false;

		return true;
	}
	
	@Override
	public NodeStructure getWorkspaceContent(WorkspaceBuffer buffer) {
		//TODO Naive implementation. Should not copy entire buffer.
		return ((NodeStructure) buffer.getModuleContent()).copy();
	}

}
