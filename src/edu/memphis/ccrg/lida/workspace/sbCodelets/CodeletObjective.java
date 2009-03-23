package edu.memphis.ccrg.lida.workspace.sbCodelets;

import java.util.Set;

import edu.memphis.ccrg.lida.shared.Node;

public class CodeletObjective {
	
	Set<Node> nodeObjectives;
	
	public CodeletObjective(Set<Node> nodes){
		nodeObjectives = nodes;
	}

	public Set<Node> getNodeObjectives() {
		return nodeObjectives;
	}

}
