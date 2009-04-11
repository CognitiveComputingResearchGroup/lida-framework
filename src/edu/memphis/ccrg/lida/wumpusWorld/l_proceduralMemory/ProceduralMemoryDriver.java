package edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Action;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WorldApplication;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusIDs;
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
	private NodeStructure struct = new GraphImpl();
	
	private FrameworkGui testGui;
	private boolean shouldDoNoOp = true;
		
	public ProceduralMemoryDriver(FrameworkTimer timer, WorldApplication simulation) {
		this.timer = timer;
		environment = simulation;		
	}
	
	public void run() {
		int coolDown = 0;
		while(keepRunning){
			try{Thread.sleep(90 + timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			//
			sendGuiContent();
			if(coolDown == 0){
				ActionContent behaviorContent = getAppropriateBehavior();
				environment.receiveBehaviorContent(behaviorContent);
				coolDown = 1;
			}else{
				coolDown--;
			}
			
			
		}//while		
	}
	
	public synchronized void receiveCSMContent(WorkspaceContent content) {
		struct = (NodeStructure)content;		
	}//method
	
	private void sendGuiContent() {
		List<Object> content = new ArrayList<Object>();
		content.add(struct.getNodes().size());
		content.add(struct.getLinks().size());
		testGui.receiveGuiContent(FrameworkGui.FROM_PROCEDURAL_MEMORY, content);
	}//method
	
	private ActionContent getAppropriateBehavior() {		
		ActionContentImpl action = new ActionContentImpl(Action.NO_OP);
		if(shouldDoNoOp)
			return action;
		GraphImpl g = (GraphImpl)struct;
		Map<Long, Node> nodeMap = g.getNodeMap();
		Set<Node> nodes = g.getNodes();
		Set<Link> links = g.getLinks();
		
		SpatialLocation agentLocation = new SpatialLocation();
		char agentDirection = ' ';
		SpatialLocation goldLocation = new SpatialLocation();
		SpatialLocation wumpusLocation = new SpatialLocation();
		Set<SpatialLocation> pitLocations = new HashSet<SpatialLocation>();
		Set<SpatialLocation> wallLocations = new HashSet<SpatialLocation>();
		boolean inLineWithWumpus = false;
		boolean safeToProceed = true;
		boolean canMoveForward = true;//TODO: wall node
		for(Link l: links){
			LinkImpl temp = (LinkImpl)l;
			
			Node source = (Node)temp.getSource();
			if(source.getId() == WumpusIDs.wumpus && temp.getType() == LinkType.inLineWith){
				inLineWithWumpus = true;
			}
			
			Node sink = (Node)temp.getSink();
			long sourceID = source.getId();
			SpatialLocation sl = (SpatialLocation)sink;
			
			if(sourceID == WumpusIDs.pit){
				if(sl.isAtTheSameLocationAs(1, 1)){
					safeToProceed = false;
					System.out.println("pit not safe");
				}
				pitLocations.add(sl);
			}else if(sourceID == WumpusIDs.wall){
				if(sl.isAtTheSameLocationAs(1, 1))
					canMoveForward = false;
				wallLocations.add(sl);
			}else if(sourceID == WumpusIDs.agent){
				agentLocation = sl;
				agentDirection = sl.getDirection();				
			}else if(sourceID == WumpusIDs.gold){
				goldLocation = sl;
			}else if(sourceID == WumpusIDs.wumpus){
				if(sl.isAtTheSameLocationAs(1, 1)){
					safeToProceed = false;
					System.out.println("wumpus not safe");
				}
				wumpusLocation = sl;
			}

		}
		
		if(agentLocation.isAtTheSameLocationAs(goldLocation)){
			action.setContent(Action.GRAB);
		}else if(inLineWithWumpus){
			action.setContent(Action.SHOOT);
			System.out.println("shooting");
		}else if(safeToProceed && canMoveForward){
			action.setContent(Action.GO_FORWARD);	
		}else{
			action.setContent(Action.TURN_LEFT);
		}
		
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
			consciousContents = (NodeStructure)broadcastContent;
		}	
		return consciousContents.getNodes();
	}

	public long getThreadID(){
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

	public synchronized void pauseActionSelection() {
		shouldDoNoOp = !shouldDoNoOp;		
		System.out.println("action selection operating " + !shouldDoNoOp);
	}
}