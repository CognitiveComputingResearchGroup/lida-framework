package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida._environment.wumpusWorld.WorldApplication;
import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

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
		NodeStructure struct;
		synchronized(this){
			struct = (NodeStructure)wkspContent;
		}
		GraphImpl g = (GraphImpl)struct;
		Set<Node> nodes = g.getNodes();
		Set<Link> links = g.getLinks();
		
		for(Node n: nodes)
			System.out.println(n.getLabel());
		
		System.out.println();
		for(Link l: links){
			System.out.println(l.toString());
		}
		System.out.println();	

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

		for(Node n: nodes){
			long nodeID = n.getId();

			if(nodeID == GOLD_ID){ //
				action.setContent(GRAB);
			}else if(nodeID == PIT_ID){
				action.setContent(GO_FORWARD);
			}else if(nodeID == WUMPUS_ID){
				action.setContent(TURN_RIGHT);
			}
		}//for		
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