package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class CoalitionImpl implements Coalition{
	
	private NodeStructureImpl struct = new NodeStructureImpl();	
	private double attentionCodeletActivation;

	public CoalitionImpl(NodeStructure content, double attnCodeActiv){
		struct = (NodeStructureImpl) content;
		attentionCodeletActivation = attnCodeActiv;
	}

	public double getActivation() {
		double sum = 0.0;
		for(Node n: struct.getNodes())
			sum += n.getActivation();
		return (sum / struct.getNodeCount()) * attentionCodeletActivation;
	}

	public BroadcastContent getContent() {
		return struct;
	}
	
	public void decay(long ticks) {
		for(Node n: struct.getNodes())
			n.decay(ticks);
	}

}//class
