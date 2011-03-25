package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Collection;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class MockAttentionCodeletImpl extends AttentionCodeletImpl {
	
	

	public MockAttentionCodeletImpl(){
		super();		
	}
	
	/**
	 * Returns true if specified WorkspaceBuffer contains this codelet's sought
	 * content.
	 * 
	 * @param buffer
	 *            the WorkspaceBuffer to be checked for content
	 * @return true, if successful
	 */
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
	
	/**
	 * Returns sought content and related content from specified
	 * WorkspaceBuffer.
	 * 
	 * @param buffer
	 *            the buffer
	 * @return the workspace content
	 */
	@Override
	public NodeStructure retreiveWorkspaceContent(WorkspaceBuffer buffer) {		
		return ((NodeStructure) buffer.getModuleContent()).copy();
	}
}
