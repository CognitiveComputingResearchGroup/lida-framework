package edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientEpisodicMemory.CueListener;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Action;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusWorld;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusNodeIDs;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanNodeStructure;

/**
 * Receives WorkspaceContent, calculates the next action to taken, 
 * and sends that action to the Environment module
 * 
 * @author ryanjmccall
 */
public class GoodProceduralMemoryDriver implements ProceduralMemory, Runnable, Stoppable, BroadcastListener, CueListener{

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
	private boolean inManualMode = false;
	private final int numCoolDownCycles = 2;
	private int leftRightCounter = 0;
		
	public GoodProceduralMemoryDriver(FrameworkTimer timer, WumpusWorld environ) {
		this.timer = timer;
		environment = environ;		
	}//constructor
	

	public void addFlowGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
	/**
	 * This loop drives the action selection
	 */
	public void run() {
		int coolDown = 0;
	
		try{Thread.sleep(800);}catch(Exception e){}		
		ActionContentImpl behaviorContent = new ActionContentImpl();

		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			
			sendGuiContent();
			if(coolDown == 0){
				if(inManualMode)
					behaviorContent.setContent(Action.NO_OP);
				else
					behaviorContent = getAppropriateBehavior();
				
				environment.receiveBehaviorContent(behaviorContent);
				coolDown = numCoolDownCycles;
			}else
				coolDown--;	

		}//while	
	}//method
	
	/**
	 * Observer pattern receive method
	 */
	public void receiveCue(WorkspaceContent cue) {
		workspaceStructure = (NodeStructure)cue;
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
	 * If GUI signals noOp then return NoOp.  Otherwise, this method gets the links from
	 * the NodeStructure and stores the detailed information regarding the 
	 * locations of the entities in the environment.  Finally actions are chosen
	 * based on this information.
	 * @return
	 */
	private ActionContentImpl getAppropriateBehavior() {		
		ActionContentImpl action = new ActionContentImpl(Action.NO_OP);		
		RyanNodeStructure struct = (RyanNodeStructure)workspaceStructure;		
		
		Set<Link> sameLocationLinks = struct.getLinksByType(LinkType.SAME_LOCATION);
		Set<Link> inLineWithLinks = struct.getLinksByType(LinkType.IN_LINE_WITH);
		Set<Link> inFrontOfLinks = struct.getLinksByType(LinkType.IN_FRONT_OF);
		Set<Link> rightOfLinks = struct.getLinksByType(LinkType.RIGHT_OF);
		Set<Link> leftOfLinks = struct.getLinksByType(LinkType.LEFT_OF);
			
		//Grab gold if it is right there.
		if(preconditionIsMet(sameLocationLinks, WumpusNodeIDs.gold)){
			action.setContent(Action.GRAB);
			return action;
		}
		//Shoot the wumpus if it is in range.
		if(preconditionIsMet(inLineWithLinks, WumpusNodeIDs.wumpus) ||
		   preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.wumpus)){
			action.setContent(Action.SHOOT);
			return action;
		}
		//Orient left or right towards gold or wumpus 
		if(preconditionIsMet(rightOfLinks, WumpusNodeIDs.gold) ||
		   preconditionIsMet(rightOfLinks, WumpusNodeIDs.wumpus)){
			action.setContent(Action.TURN_RIGHT);
			return action;
		}
		if(preconditionIsMet(leftOfLinks, WumpusNodeIDs.gold) ||
		   preconditionIsMet(leftOfLinks, WumpusNodeIDs.wumpus)){
			action.setContent(Action.TURN_LEFT);
			return action;
		}
		//Safe to proceed if no pits or wumpus in front of you
		boolean safeToProceed = true;
		if(preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.wumpus) || 
		   preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.pit)){
			safeToProceed = false;
		}
		//Head forward toward gold when it's there
		if(safeToProceed && 
		  (preconditionIsMet(inLineWithLinks, WumpusNodeIDs.gold) || 
		   preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.gold))){
			action.setContent(Action.GO_FORWARD);
			return action;
		}
		//No wall and safe, then go forward.
		if(safeToProceed && !preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.wall)){
			action.setContent(Action.GO_FORWARD);
			return action;

		}	
		//Gold-on-pit scenario
		if(!safeToProceed && 
		    preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.gold) &&
		   !preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.wumpus)){//Halt in unwinnable situation
			action.setContent(Action.END_TRIAL);
			return action;
		}
		//If obstacle in front of
		if(preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.wall) || 
		   preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.pit)){
			//If in a wall/pit 'corner'
			if(preconditionIsMet(rightOfLinks, WumpusNodeIDs.wall) ||
			   preconditionIsMet(rightOfLinks, WumpusNodeIDs.pit)){
				action.setContent(Action.TURN_LEFT);
				return action;
			}
			if(preconditionIsMet(leftOfLinks, WumpusNodeIDs.wall) ||
			   preconditionIsMet(leftOfLinks, WumpusNodeIDs.pit)){
				action.setContent(Action.TURN_RIGHT);
			    return action;
			}
		}
		//Turn randomly if faced w/ just a pit or wall in front of 
		if(preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.pit) ||
		   preconditionIsMet(inFrontOfLinks, WumpusNodeIDs.wall)){
			if(leftRightCounter % 3 == 0 || leftRightCounter % 5 == 0 || leftRightCounter % 8 == 0 ||
				leftRightCounter % 16 == 0)
				action.setContent(Action.TURN_LEFT);
			else
				action.setContent(Action.TURN_RIGHT);
			leftRightCounter++;
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

	public void stopRunning() {
		keepRunning = false;		
	}

	public synchronized void toggleActionSelection(){
		inManualMode = !inManualMode;
	}

	public synchronized void stopActionSelection() {
		inManualMode = true;
	}

	public boolean getStartingMode() {
		return inManualMode;
	}


	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		// TODO Auto-generated method stub
		
	}

}//class