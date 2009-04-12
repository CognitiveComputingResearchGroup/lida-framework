package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;

public class CodeletsDesiredContent {
	
	Map<Long, Node> nodeObjectives;
	
	public CodeletsDesiredContent(){
		nodeObjectives = new HashMap<Long, Node>();
	}
	
	public CodeletsDesiredContent(Map<Long, Node> nodeObjs){
		nodeObjectives = nodeObjs;
	}

	public Map<Long, Node> getNodeObjectives() {
		return nodeObjectives;
	}

	public boolean hasNodeObjective(Node n) {
		return nodeObjectives.containsKey(n.getId());
	}

}
