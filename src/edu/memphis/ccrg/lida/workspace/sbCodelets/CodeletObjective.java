package edu.memphis.ccrg.lida.workspace.sbCodelets;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;

public class CodeletObjective {
	
	Map<Long, Node> nodeObjectives;
	
	public CodeletObjective(Map<Long, Node> nodeObjs){
		nodeObjectives = nodeObjs;
	}

	public Map<Long, Node> getNodeObjectives() {
		return nodeObjectives;
	}

	public boolean hasNodeObjective(Node n) {
		return nodeObjectives.containsKey(n.getId());
	}

}
