//package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.Collection;
//import edu.memphis.ccrg.lida.shared.Node;
//import edu.memphis.ccrg.lida.shared.LinkImpl;
//import edu.memphis.ccrg.lida.shared.LinkType;
//import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
//import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusNodeIDs;
//import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;
//import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;
//import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanNodeStructure;
//import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
//
//public class SpatialLinkCodeletAction implements CodeletAction{
//	
//	private int linkCount = 100;
//	
//	private char getAgentDirection(RyanPamNode agent){
//		char dir = ' ';
//		if(agent != null){
//			Set<SpatialLocation> locs = agent.getLocations();
//			for(SpatialLocation sl: locs)
//				dir = sl.getDirection();	
//		}
//		return dir;
//	}//method
//	
//	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
//		RyanNodeStructure graph = (RyanNodeStructure)content;	
//		RyanPamNode agent = (RyanPamNode)graph.getNode(WumpusNodeIDs.agent);
//		char agentDirection = getAgentDirection(agent);
//		Collection<Node> nodes = graph.getNodes();
//		Set<Node> newNodesToBeAdded = new HashSet<Node>();
//		for(Node n: nodes){
//			RyanPamNode objectInWW = (RyanPamNode)n;
//			Set<SpatialLocation> locations = objectInWW.getLocations();		
//			int i = 1;
//			int numLocations = locations.size();
//			for(SpatialLocation sl: locations){
//				if(objectInWW.getId() != WumpusNodeIDs.agent){
//					//For links between agent and objects
//					LinkType spatialRelationType = calcRelationType(sl, agentDirection);
//					if(numLocations > 1){ //For objects that appear multiple times
//						RyanPamNode copy = new RyanPamNode(objectInWW);
//						copy.setId(copy.getId() + i);
//						copy.setLabel(copy.getLabel() + i);
//						newNodesToBeAdded.add(copy);
//						graph.addLink(new LinkImpl(agent, copy, spatialRelationType, linkCount++ + ""));
//					}else
//						graph.addLink(new LinkImpl(agent, objectInWW, spatialRelationType, linkCount++ + ""));
//				}//if not the agent
//				//For links between each object in WW and one of its exact locations
//				graph.addLink(new LinkImpl(objectInWW, sl, LinkType.GROUNDING, linkCount++ + ""));
//				i++;
//			}//for all spatial locations of n	
//		}//for nodes 	
//		
//		for(Node n: newNodesToBeAdded)
//			graph.addNode(n);
//		
//		return content;
//	}//method
//
//	private LinkType calcRelationType(SpatialLocation sl, char agentDirection) {
//		LinkType type = LinkType.NONE;
//		int i = sl.getI();
//		int j = sl.getJ();		
//		if(agentDirection == 'V'){
//			if(j == 1){
//				if(i == 0)
//					type = LinkType.SAME_LOCATION;
//				else if(i == 1)
//					type = LinkType.IN_FRONT_OF;
//				else				
//					type = LinkType.IN_LINE_WITH;				
//			}else if(j == 0 && i == 0){
//				type = LinkType.RIGHT_OF;
//			}else if(j == 2 && i == 0){
//				type = LinkType.LEFT_OF;
//			}
//		}else if(agentDirection == 'A'){
//			if(j == 1){
//				if(i == 0)
//					type = LinkType.IN_LINE_WITH;
//				else if(i == 1)
//					type = LinkType.IN_FRONT_OF;
//				else 
//					type = LinkType.SAME_LOCATION;				      
//			}else if(j == 0 && i == 2){
//				type = LinkType.LEFT_OF;
//			}else if(j == 2 && i == 2){
//				type = LinkType.RIGHT_OF;
//			}
//		}else if(agentDirection == '<'){
//			if(i == 1){
//				if(j == 0)
//					type = LinkType.IN_LINE_WITH;
//				else if(j == 1)
//					type = LinkType.IN_FRONT_OF;
//				else if(j == 2)
//					type = LinkType.SAME_LOCATION;
//			}else if(i == 0 && j == 2)
//				type = LinkType.RIGHT_OF;
//			else if(i == 2 && j == 2)
//				type = LinkType.LEFT_OF;
//		}else if(agentDirection == '>'){
//			if(i == 1){
//				if(j == 0)
//					type = LinkType.SAME_LOCATION;
//				else if(j == 1)
//					type = LinkType.IN_FRONT_OF;
//				else
//					type = LinkType.IN_LINE_WITH;
//			}else if(i == 2 && j == 0)
//				type = LinkType.RIGHT_OF;
//			else if(i == 0 && j == 0)
//				type = LinkType.LEFT_OF;
//		}
//		return type;		
//	}//method
//
//}//class
