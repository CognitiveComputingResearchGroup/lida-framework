package edu.memphis.ccrg.lida.pam;

import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;

public class ExcitationTask extends LidaTaskImpl {
	
	private PamNode startingNode;
	private double excitationAmount;
	private PamNodeStructure nodeStruct;

	public ExcitationTask(PamNode node, double activation,
			              PamNodeStructure pamNodeStructure) {
		startingNode = node;
		excitationAmount = activation;
		nodeStruct = pamNodeStructure;
	}

	public void run() {
		startingNode.excite(excitationAmount);
		Set<PamNode> nodes = nodeStruct.getParents(startingNode);
		while(nodes.size() != 0){
			
		}

	}//method



}
