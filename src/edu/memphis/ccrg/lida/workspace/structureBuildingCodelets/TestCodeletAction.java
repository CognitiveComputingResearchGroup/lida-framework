package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.Set;
import edu.memphis.ccrg.lida._perception.LinkImpl;
import edu.memphis.ccrg.lida._perception.PamNodeImpl;
import edu.memphis.ccrg.lida._perception.SpatialLocation;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class TestCodeletAction implements CodeletAction{
	
	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
		NodeStructure struct = (NodeStructure)content.getContent();
		Set<Node> nodes = struct.getNodes();
		for(Node n: nodes){
			if(n instanceof PamNodeImpl){
				PamNodeImpl temp = (PamNodeImpl)n;
				Set<SpatialLocation> locs = temp.getLocations();
				for(SpatialLocation sl: locs){
					
				}//for
			}//if
		}//for			
			
		struct.addLink(new LinkImpl(null, null, null, 102390));
		return content;
	}//method

}//class
