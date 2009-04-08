package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;

public class CodeletContext {
	
	Map<Long, Node> nodeObjectives;
	
	public CodeletContext(Map<Long, Node> nodeObjs){
		nodeObjectives = nodeObjs;
	}

	public Map<Long, Node> getNodeObjectives() {
		return nodeObjectives;
	}

	public boolean hasNodeObjective(Node n) {
		return nodeObjectives.containsKey(n.getId());
	}

}
