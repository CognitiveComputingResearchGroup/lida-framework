package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class CoalitionImpl extends ActivatibleImpl implements Coalition{
	
	private NodeStructure struct = new NodeStructureImpl();	
	private double attentionCodeletActivation;

	public CoalitionImpl(NodeStructure content, double attnCodeActiv){
		struct = content;
		attentionCodeletActivation = attnCodeActiv;
		setActivation(calculateActivation());
		
	}

	public double calculateActivation() {
		double sum = 0.0;
		for(Node n: struct.getNodes())
			sum += n.getActivation();
		return (sum / struct.getNodeCount()) * attentionCodeletActivation;
	}

	public BroadcastContent getContent() {
		return (BroadcastContent)struct;
	}
	
}//class
