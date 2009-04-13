package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusIDs;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;

public class SpatialLinkCodeletAction implements CodeletAction{
	
	private int linkCount = 100;
	
	private char getAgentDirection(){
		char direction = ' ';
		return direction;
	}
	
	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
		//If I want to get a particular node in SB codelet then I need a map
		GraphImpl g = (GraphImpl)content;
		Map<Long, Node> nodeMap = g.getNodeMap();
		PamNodeImpl agent = (PamNodeImpl)nodeMap.get(WumpusIDs.agent);
		char agentDirection = ' ';
		if(agent != null){
			Set<SpatialLocation> locs = agent.getLocations();
			for(SpatialLocation sl: locs)
				agentDirection = sl.getDirection();		
			//System.out.println(agentDirection);		
		}//
	
		Set<Node> nodes = g.getNodes();		
		for(Node n: nodes){
			if(n instanceof PamNodeImpl){
				PamNodeImpl temp = (PamNodeImpl)n;
				Set<SpatialLocation> originalLocs = temp.getLocations();
				Set<SpatialLocation> copiedLocs = new HashSet<SpatialLocation>();
				synchronized(this){
					for(SpatialLocation oldSL: originalLocs)
						copiedLocs.add(new SpatialLocation(oldSL));
				}//
				
				for(SpatialLocation sl: copiedLocs){
					LinkType t = calcRelationType(sl, agentDirection);
					LinkImpl newLink = new LinkImpl(temp, sl, t, linkCount++);
		
					g.addLink(newLink);
				}//for
				
			}//if
		}//for	
		
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
