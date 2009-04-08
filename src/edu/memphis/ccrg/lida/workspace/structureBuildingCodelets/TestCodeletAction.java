package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.Set;

import edu.memphis.ccrg.lida._perception.LinkImpl;
import edu.memphis.ccrg.lida._perception.PamNodeImpl;
import edu.memphis.ccrg.lida._perception.SpatialLocation;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class TestCodeletAction implements CodeletAction{
	
	private int linkCount = 100;
	
	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
		NodeStructure struct = (NodeStructure)content.getContent();
		Set<Node> nodes = struct.getNodes();
		for(Node n: nodes){
			if(n instanceof PamNodeImpl){
				PamNodeImpl temp = (PamNodeImpl)n;
				Set<SpatialLocation> locs = temp.getLocations();
				for(SpatialLocation sl: locs){
					LinkImpl newLink = new LinkImpl(temp, sl, LinkType.child, linkCount++);
					calcRelationType(newLink, sl);
					struct.addLink(newLink);
				}//for
			}//if
		}//for			

		return content;
	}//method

	private void calcRelationType(LinkImpl newLink, SpatialLocation sl) {
		String label = "";
		int i = sl.getI();
		int j = sl.getJ();		
		
		if(i == 0)
			label += "Left";
		else if(i == 2)
			label += "Right";

		label += " ";
		if(j == 0)
			label += "Above";
		else if(j == 2)
			label += "Below";
		
		newLink.setLabel(label);		
	}//method

}//class
