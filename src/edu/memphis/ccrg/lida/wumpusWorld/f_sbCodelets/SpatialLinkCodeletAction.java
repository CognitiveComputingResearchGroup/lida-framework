package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;

import java.util.Set;

import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusIDs;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanNodeStructure;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;

public class SpatialLinkCodeletAction implements CodeletAction{
	
	private int linkCount = 100;
	
	private char getAgentDirection(RyanNodeStructure g){
		char dir = ' ';
		PamNodeImpl agent = (PamNodeImpl)g.getNode(WumpusIDs.agent);
		if(agent != null){
			Set<SpatialLocation> locs = agent.getLocations();
			for(SpatialLocation sl: locs)
				dir = sl.getDirection();	
		}
		
		return dir;
	}//method

	
	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
		RyanNodeStructure graph = (RyanNodeStructure)content;		
		char agentDirection = getAgentDirection(graph);
	
		Set<Node> nodes = graph.getNodes();		
		for(Node n: nodes){
			PamNodeImpl temp = (PamNodeImpl)n;
			Set<SpatialLocation> locations = temp.getLocations();

			for(SpatialLocation sl: locations){
				LinkType t = calcRelationType(sl, agentDirection);
				LinkImpl newLink = new LinkImpl(temp, sl, t, new Long(linkCount));
				linkCount++;
				graph.addLink(newLink);
			}//for
				
		}//for nodes 		
		return content;
	}//method

	private LinkType calcRelationType(SpatialLocation sl, char agentDirection) {
		
//		leftof,
//		rightOf,
//		above,
//		below,
		
		LinkType type = LinkType.none;
		int i = sl.getI();
		int j = sl.getJ();		
		
		if(agentDirection == 'V'){
			if(j == 1){
				type = LinkType.inLineWith;
				if(i == 1)
					type = LinkType.inFrontOf;
			}else if(j == 0 && i == 0){
				type = LinkType.rightOf;
			}else if(j == 2 && i == 0){
				type = LinkType.leftOf;
			}
		}else if(agentDirection == 'A'){
			if(j == 1){
				type = LinkType.inLineWith;
				if(i == 1)
					type = LinkType.inFrontOf;
			}else if(j == 0 && i == 2){
				type = LinkType.leftOf;
			}else if(j == 2 && i == 2){
				type = LinkType.rightOf;
			}
		}else if(agentDirection == '<'){
			if(i == 1){
				type = LinkType.inLineWith;
				if(j == 1)
					type = LinkType.inFrontOf;
			}else if(i == 0 && j == 2)
				type = LinkType.rightOf;
			else if(i == 2 && j == 2)
				type = LinkType.leftOf;
		}else if(agentDirection == '>'){
			if(i == 1){
				type = LinkType.inLineWith;
				if(j == 1)
					type = LinkType.inFrontOf;
			}else if(i == 2 && j == 0)
				type = LinkType.rightOf;
			else if(i == 0 && j == 0)
				type = LinkType.leftOf;
		}

		return type;		
	}//method

}//class
