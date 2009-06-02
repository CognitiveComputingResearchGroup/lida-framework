package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 * This class defines what a codelet seeks as a being a NodeStructure
 * 
 */
public class CodeletsDesiredContent {
	
	private NodeStructure nodeObjectives;
	
	public CodeletsDesiredContent(){
		nodeObjectives = new NodeStructureImpl();
	}
	
	public CodeletsDesiredContent(NodeStructure nodeObjs){
		nodeObjectives = nodeObjs;
	}

	public NodeStructure getNodeObjectives() {
		return nodeObjectives;
	}

	public boolean hasNodeObjective(Node n) {
		System.out.println("test hasNodeObjective method !!");
		return nodeObjectives.getNode(n.getId()) == null;
	}

}
