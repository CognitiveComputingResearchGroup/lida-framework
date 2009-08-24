package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class DefaultContentDetectBehavior implements ContentDetectBehavior{
	
	private NodeStructure soughtContent = new NodeStructureImpl();

	public boolean hasSoughtContent(CurrentSituationalModel csm) {
		NodeStructure model = csm.getModel();
		Collection<Node> nodes = soughtContent.getNodes();
		Collection<Link> links = soughtContent.getLinks();
		for(Node n: nodes)
			if(!model.hasNode(n))
				return false;
		
		for(Link l: links)
			if(!model.hasLink(l))	
				return false;
		
		return true;
	}

	public NodeStructure getSoughtContent(CurrentSituationalModel csm) {
		if(hasSoughtContent(csm))
			return csm.getModel();
		else 
			return null;
	}

}
