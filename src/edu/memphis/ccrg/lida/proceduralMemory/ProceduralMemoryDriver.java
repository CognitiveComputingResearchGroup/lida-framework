package edu.memphis.ccrg.lida.proceduralMemory;


import java.util.Set;

import edu.domain.wumpusWorld.WorldApplication;
import edu.memphis.ccrg.lida.actionSelection.BehaviorContent;
import edu.memphis.ccrg.lida.actionSelection.BehaviorContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
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
	private BroadcastContent broadcastContent = new BroadcastContentImpl();
		
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
			BehaviorContent behaviorContent = getAppropriateBehavior(nodes);
			environment.receiveBehaviorContent(behaviorContent);
			
		}//while		
	}

	private BehaviorContent getAppropriateBehavior(Set<Node> nodes) {
		BehaviorContentImpl action = new BehaviorContentImpl();
		
		System.out.print("Nodes in proc mem ");
		for(Node n: nodes)
			System.out.print(n.getLabel() + " ");
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
			consciousContents = (NodeStructure)broadcastContent.getContent();
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

}