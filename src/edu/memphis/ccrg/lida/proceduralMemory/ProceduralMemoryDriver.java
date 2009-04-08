package edu.memphis.ccrg.lida.proceduralMemory;


import java.util.Set;

import edu.memphis.ccrg.lida._environment.wumpusWorld.WorldApplication;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelContent;

public class ProceduralMemoryDriver implements Runnable, Stoppable, BroadcastListener{

	//FIELDS
	
	private FrameworkTimer timer;
	private WorldApplication environment;
	//
	private boolean keepRunning = true;
	private long threadID;
	//
	//private BroadcastContent broadcastContent = new BroadcastContentImpl();
	private BroadcastContent broadcastContent = null;
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
			
			Set<Node> nodes = getNodesFromBroadcast();		
			ActionContent behaviorContent = getAppropriateBehavior(nodes);
			environment.receiveBehaviorContent(behaviorContent);
			
		}//while		
	}

	private ActionContent getAppropriateBehavior(Set<Node> nodes) {
		int GO_FORWARD = 1;
		int TURN_RIGHT = 2;
		int TURN_LEFT = 3;
		int GRAB = 4;
		int SHOOT = 5;
		int NO_OP = 6;
		
		ActionContentImpl action = new ActionContentImpl(NO_OP);
		
		System.out.print("Nodes in proc mem ");
		for(Node n: nodes){
			System.out.print(n.getLabel() + " ");
			if(n.getId() == 13){
				action.setContent(TURN_LEFT);
				return action;
			}
		}
		System.out.println();
		
		//TODO: lookup table
//		if(nodes.contains()){
//			action.setContent(1);
//		}
		
		return action;
	}

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