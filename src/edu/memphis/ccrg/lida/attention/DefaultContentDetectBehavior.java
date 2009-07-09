package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class DefaultContentDetectBehavior implements ContentDetectBehavior{
	
	private NodeStructure soughtContent = new NodeStructureImpl();

	public boolean hasSoughtContent(CurrentSituationalModel csm) {
		NodeStructure model = csm.getCSMContent();
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
			return csm.getCSMContent();
		else 
			return null;
	}

}
