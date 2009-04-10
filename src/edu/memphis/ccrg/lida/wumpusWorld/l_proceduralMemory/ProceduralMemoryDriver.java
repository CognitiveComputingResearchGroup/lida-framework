package edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory;

import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WorldApplication;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;

public class ProceduralMemoryDriver implements Runnable, Stoppable, BroadcastListener, CSMListener{

	//FIELDS
	private FrameworkTimer timer;
	private WorldApplication environment;
	//
	private boolean keepRunning = true;
	private long threadID;
	//
	//private BroadcastContent broadcastContent = new BroadcastContentImpl();
	private BroadcastContent broadcastContent = null;
	private WorkspaceContent wkspContent = new GraphImpl();
	private FrameworkGui testGui;
		
	public ProceduralMemoryDriver(FrameworkTimer timer,
			WorldApplication simulation) {
		this.timer = timer;
		environment = simulation;		
	}
	
	public void run() {
		while(keepRunning){
			try{Thread.sleep(23 + timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			//
			ActionContent behaviorContent = getAppropriateBehavior();
			environment.receiveBehaviorContent(behaviorContent);
		}//while		
	}//method
	
	public synchronized void receiveCSMContent(WorkspaceContent content) {
		wkspContent = content;		
	}//method

	private ActionContent getAppropriateBehavior() {
		int GO_FORWARD = 1;
		int TURN_RIGHT = 2;
		int TURN_LEFT = 3;
		int GRAB = 4;
		int SHOOT = 5;
		int NO_OP = 6;
		
		int GOLD_ID = 12;
		int PIT_ID = 13;
		int WUMPUS_ID = 14;
		int AGENT_ID = 15;
		
		ActionContentImpl action = new ActionContentImpl(NO_OP);
		
		NodeStructure struct;
		synchronized(this){
			struct = (NodeStructure)wkspContent;
		}
		GraphImpl g = (GraphImpl)struct;
		Map<Long, Node> nodeMap = g.getNodeMap();
		Set<Node> nodes = g.getNodes();
		Set<Link> links = g.getLinks();
//		
//		PamNodeImpl gold = (PamNodeImpl)nodeMap.get(GOLD_ID);
//		if(gold != null)
//			System.out.println(gold.toString());
//		//System.out.println()
//		
//		for(Node n: nodes)
//			System.out.println(n.getId() + " " + n.getLabel());
		int agentx = 0, agenty = 0;
		int goldx = -1, goldy = -1;
		int wumpusx = -2, wumpusy = -2;
		for(Link l: links){
			LinkImpl temp = (LinkImpl)l;
			Node source = (Node)temp.getSource();
			Node sink = (Node)temp.getSink();
			long sourceID = source.getId();
			SpatialLocation sl = (SpatialLocation)sink;
			
			if(sourceID == AGENT_ID){
				agentx = sl.getJ();
				agenty = sl.getI();
			}else if(sourceID == GOLD_ID){
				goldx = sl.getJ();
				goldy = sl.getI();
			}else if(sourceID == WUMPUS_ID){
				wumpusx = sl.getJ();
				wumpusy = sl.getI();
			}
			
			//System.out.println(temp.toString());
		}
		System.out.println(agentx + " " + agenty + " " + goldx + " " + goldy);
		
		if(agentx == goldx && agenty == goldy)
			action.setContent(GRAB);
		//if(agentx)
		
//		if(nodeMap.containsKey(GOLD_ID)){
//			PamNodeImpl gold = (PamNodeImpl)nodeMap.get(GOLD_ID);
//			Set<SpatialLocation> locations = gold.getLocations();
//			System.out.println(locations.size());
//		}
		
		//System.out.println("Nodes " + nodes.size() + "Links " + links.size() + 
		//		"MapNodes " +  nodeMap.values().size() + "MapKeys " + nodeMap.keySet().size());
		
//		for(Node n: nodes)
//			System.out.println(n.getLabel());
//		
//		System.out.println();
//		for(Link l: links){
//			SpatialLocation temp = (SpatialLocation)l.getSink();
//			System.out.println(l.toString() + " " + temp.getI() + " " + temp.getJ());
//		}
//		System.out.println();	


		

		return action;
	}//method

	private Set<Node> getNodesFromBroadcast() {
		NodeStructure consciousContents;
		synchronized(this){
//			consciousContents = (NodeStructure)broadcastContent.getContent();
			consciousContents = (NodeStructure)broadcastContent;
		}	
		return consciousContents.getNodes();
	}

	public long getThreadID() {
		return threadID;
	}

	public void setThreadID(long id) {
		threadID = id;		
	}

	public void stopRunning() {
		keepRunning = false;		
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = bc;		
	}

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
}