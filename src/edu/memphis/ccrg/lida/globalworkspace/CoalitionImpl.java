package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class CoalitionImpl extends ActivatibleImpl implements Coalition{
	
	private NodeStructure struct = new NodeStructureImpl();	
	private double attentionCodeletActivation;

	public CoalitionImpl(NodeStructure content, double attnCodeActiv){
		struct = content;
		attentionCodeletActivation = attnCodeActiv;
		updateActivation();
	}

	private void updateActivation() {
		double sum = 0.0;
		for(Node n: struct.getNodes())
			sum += n.getActivation();
		for(Link l: struct.getLinks())
			sum += l.getActivation();
		setActivation((attentionCodeletActivation * sum) / (struct.getNodeCount() + struct.getLinkCount()));
	}

	public BroadcastContent getContent() {
		return (BroadcastContent) struct;
	}
	
}//class