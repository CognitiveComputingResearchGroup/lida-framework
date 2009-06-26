package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class CoalitionImpl implements Coalition{
	
	private NodeStructureImpl struct = new NodeStructureImpl();	
	private double attentionCodeletActivation;

	public CoalitionImpl(NodeStructure content, double attnCodeActiv){
		struct = (NodeStructureImpl) content;
		attentionCodeletActivation = attnCodeActiv;
	}

	public void decay() {
		// TODO Auto-generated method stub
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

}//class
