package edu.memphis.ccrg.lida.attention;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * WARNING:	Renaming this class requires renaming values in
 * 	  configs/factoriesData.xml
 */
public class BasicAttentionCodeletImpl extends AttentionCodeletImpl {
	
	private static final Logger logger = Logger.getLogger(BasicAttentionCodeletImpl.class.getCanonicalName());
	
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

		logger.log(Level.FINE, "Attn codelet " + this.toString() + " found sought content", LidaTaskManager.getCurrentTick());
		return true;
	}
	
	@Override
	public NodeStructure getWorkspaceContent(WorkspaceBuffer buffer) {
		//TODO Naive implementation. Should not copy entire buffer.
		return ((NodeStructure) buffer.getModuleContent()).copy();
	}

}
