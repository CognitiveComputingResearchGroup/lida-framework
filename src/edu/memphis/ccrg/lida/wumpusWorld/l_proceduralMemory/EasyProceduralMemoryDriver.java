package edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Action;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusWorld;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusNodeIDs;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanNodeStructure;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;

/**
 * Receives WorkspaceContent, calculates the next action to taken, 
 * and sends that action to the Environment module
 * 
 * @author ryanjmccall
 */
public class EasyProceduralMemoryDriver implements ProceduralMemory, Runnable, Stoppable, BroadcastListener, WorkspaceListener{

	//FIELDS
	private FrameworkTimer timer;
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkGui testGui;
	
	//Specific to this module
	private WumpusWorld environment;//To send output
	//private BroadcastContent broadcastContent = null;//TODO: not used in this implementation
	private NodeStructure workspaceStructure = new RyanNodeStructure();//The input for this impl.
	/**
	 * Used to paused the action selection.  Its value is changed from GUI click.
	 */
	private boolean shouldDoNoOp = true;
		
	public EasyProceduralMemoryDriver(FrameworkTimer timer, WumpusWorld environ) {
		this.timer = timer;
		environment = environ;		
	}//constructor
	

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
	/**
	 * This loop drives the action selection
	 */
	public void run() {
		int coolDown = 0;
		int counter = 0;		
		//boolean runOneStep = false;
		
		ActionContentImpl behaviorContent = new ActionContentImpl();
		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			
			sendGuiContent();
			if(coolDown == 0){
				if(shouldDoNoOp)
					behaviorContent.setContent(Action.NO_OP);
				else
					behaviorContent = getAppropriateBehavior();
				
				environment.receiveBehaviorContent(behaviorContent);
				coolDown = 2;
			}else
				coolDown--;	
			counter++;
		}//while	
		long finishTime = System.currentTimeMillis();				
		System.out.println("Proc: Ave cycle time: " + 
							Printer.rnd((finishTime - startTime)/(double)counter));
		
	}//method
	
	/**
	 * Observer pattern receive method
	 */
	public void receiveWorkspaceContent(WorkspaceContent content) {
		workspaceStructure = (NodeStructure)content;		
	}
	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		//broadcastContent = bc;		
	}
	
	private void sendGuiContent() {
		List<Object> content = new ArrayList<Object>();
		content.add(workspaceStructure.getNodes().size());
		content.add(workspaceStructure.getLinks().size());
		testGui.receiveGuiContent(FrameworkGui.FROM_PROCEDURAL_MEMORY, content);
	}//method
	
	/**
	 * If GUI signals noOp then return NoOp.  Otherwise, this method unpacks
	 * the NodeStructure and stores the detailed information regarding the 
	 * locations of the entities in the environment.  Finally actions are chosen
	 * based on this information.
	 * @return
	 */
	private ActionContentImpl getAppropriateBehavior() {		
		ActionContentImpl action = new ActionContentImpl(Action.NO_OP);		
		RyanNodeStructure struct = (RyanNodeStructure)workspaceStructure;		
		Map<LinkType, Set<Link>> links = struct.getLinksByType();
		
		if(preconditionIsMet(links.get(LinkType.sameLocationAs), WumpusNodeIDs.gold)){
			action.setContent(Action.GRAB);
			return action;
		}
		if(preconditionIsMet(links.get(LinkType.inLineWith), WumpusNodeIDs.wumpus) ||
		   preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.wumpus)){
			action.setContent(Action.SHOOT);
			return action;
		}
		if(preconditionIsMet(links.get(LinkType.rightOf), WumpusNodeIDs.gold) ||
		   preconditionIsMet(links.get(LinkType.rightOf), WumpusNodeIDs.wumpus)){
			action.setContent(Action.TURN_RIGHT);
			return action;
		}
		if(preconditionIsMet(links.get(LinkType.leftOf), WumpusNodeIDs.gold) ||
		   preconditionIsMet(links.get(LinkType.leftOf), WumpusNodeIDs.wumpus)){
			action.setContent(Action.TURN_LEFT);
			return action;
		}
		boolean safeToProceed = true;
		if(preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.wumpus) || 
		   preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.pit)){
			safeToProceed = false;
		}
		if(safeToProceed && 
		  (preconditionIsMet(links.get(LinkType.inLineWith), WumpusNodeIDs.gold) || 
		   preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.gold))){
			action.setContent(Action.GO_FORWARD);
			return action;
		}
		if(safeToProceed && 
		   !preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.wall)){
			if(Math.random() > 0.1){
				action.setContent(Action.GO_FORWARD);
				return action;
			}	
		}	
		if(!safeToProceed && 
		    preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.gold) &&
		   !preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.wumpus)){//Halt in unwinnable situation
			action.setContent(Action.END_TRIAL);
			return action;
		}
		if(preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.wall) || 
		   preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.pit)){
			if(preconditionIsMet(links.get(LinkType.rightOf), WumpusNodeIDs.wall) ||
			   preconditionIsMet(links.get(LinkType.rightOf), WumpusNodeIDs.pit)){
				action.setContent(Action.TURN_LEFT);
				return action;
			}
			if(preconditionIsMet(links.get(LinkType.leftOf), WumpusNodeIDs.wall) ||
			   preconditionIsMet(links.get(LinkType.leftOf), WumpusNodeIDs.pit)){
				action.setContent(Action.TURN_RIGHT);
			    return action;
			}
		}
		if(preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.pit) ||
		   preconditionIsMet(links.get(LinkType.inFrontOf), WumpusNodeIDs.wall)){
			if(Math.random() > 0.5)
				action.setContent(Action.TURN_LEFT);
			else
				action.setContent(Action.TURN_RIGHT);
		}
		return action;
	}//method

	private boolean preconditionIsMet(Set<Link> links, long sinkId) {		
		if(links != null){
			if(sinkId == WumpusNodeIDs.wall || sinkId == WumpusNodeIDs.pit)
				return specialPreconditionIsMet(links, sinkId);
			
			for(Link l: links){
				Node n = (Node)l.getSink();
				if(n.getId() == sinkId)
					return true;
			}
		}
		return false;
	}//method

	private boolean specialPreconditionIsMet(Set<Link> links, long sinkId) {
		if(sinkId == WumpusNodeIDs.pit){
			for(Link l: links){
				Node n = (Node)l.getSink();
				long id = n.getId();
				if(id >= WumpusNodeIDs.pit && id < WumpusNodeIDs.wall)
					return true;
			}
			return false;
		}else if(sinkId == WumpusNodeIDs.wall){
			for(Link l: links){
				Node n = (Node)l.getSink();
				long id = n.getId();
				if(id > WumpusNodeIDs.wall)
					return true;
			}
			return false;
		}else{
			System.out.println("Error in special precondition");
			return false;
		}
	}//method

	public long getThreadID(){
		return threadID;
	}

	public void setThreadID(long id) {
		threadID = id;		
	}

	public void stopRunning() {
		keepRunning = false;		
	}

	public synchronized void pauseActionSelection(){
		shouldDoNoOp = !shouldDoNoOp;
	}

	public boolean getStartingMode() {
		return shouldDoNoOp;
	}


	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		// TODO Auto-generated method stub
		
	}
}//class