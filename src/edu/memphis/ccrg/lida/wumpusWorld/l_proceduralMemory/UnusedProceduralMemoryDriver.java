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
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
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
public class UnusedProceduralMemoryDriver implements ProceduralMemory, Runnable, Stoppable, PAMListener{

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
	private int leftRightCounter = 0;
		
	public UnusedProceduralMemoryDriver(FrameworkTimer timer, WumpusWorld environ) {
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
		//boolean runOneStep = false;
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
				coolDown = 2;
			}else
				coolDown--;	
			//counter++;
		}//while	
	}//method
	
	/**
	 * Observer pattern receive method
	 */
	public void receivePAMContent(WorkspaceContent pc) {
		workspaceStructure = (NodeStructure) pc;		
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
		Map<Long, Node> nodeMap = struct.getNodeMap();

		//AGENT		
		SpatialLocation agentLocation = new SpatialLocation(-7, -7);
		if(nodeMap.containsKey(WumpusNodeIDs.agent)){
			RyanPamNode agent = (RyanPamNode) nodeMap.get(WumpusNodeIDs.agent);
			agentLocation = agent.getLocation();
		}		
		//GOLD
		SpatialLocation goldLocation = new SpatialLocation(-5, -5);
		if(nodeMap.containsKey(WumpusNodeIDs.gold)){
			RyanPamNode gold = (RyanPamNode) nodeMap.get(WumpusNodeIDs.gold);
			goldLocation = gold.getLocation();
		}
		//WUMPUS
		SpatialLocation wumpusLocation = new SpatialLocation(-6, -6);
		if(nodeMap.containsKey(WumpusNodeIDs.wumpus)){
			RyanPamNode wumpus = (RyanPamNode) nodeMap.get(WumpusNodeIDs.wumpus);
			wumpusLocation = wumpus.getLocation();
		}
		//PITS
		Set<SpatialLocation> pitLocations = new HashSet<SpatialLocation>();
		if(nodeMap.containsKey(WumpusNodeIDs.pit)){
			RyanPamNode pit = (RyanPamNode) nodeMap.get(WumpusNodeIDs.pit);
			pitLocations = pit.getLocations();
		}
		//WALLS
		Set<SpatialLocation> wallLocations = new HashSet<SpatialLocation>();
		if(nodeMap.containsKey(WumpusNodeIDs.wall)){
			RyanPamNode wall = (RyanPamNode) nodeMap.get(WumpusNodeIDs.wall);
			wallLocations = wall.getLocations();
		}
		//CALC PRECONDITIONS
		
		SpatialLocation upperLeft = new SpatialLocation(0,0);
		SpatialLocation upperRight = new SpatialLocation(0,2);
		SpatialLocation bottomLeft = new SpatialLocation(2,0);
		SpatialLocation bottomRight = new SpatialLocation(2,2);
		SpatialLocation middle = new SpatialLocation(1, 1);
		SpatialLocation top = new SpatialLocation(0, 1);
		SpatialLocation bottom = new SpatialLocation(2, 1);
		SpatialLocation left = new SpatialLocation(1, 0);
		SpatialLocation right = new SpatialLocation(1, 2);
		
		//PIT LOCATIONS
		boolean pitInFrontOf = false;
		for(SpatialLocation spatLoc: pitLocations){
			if(spatLoc.isSameAs(middle))
				pitInFrontOf = true;		
		}
		//WALL RELATIONS
		boolean wallInFrontOf = false;	
		for(SpatialLocation spatLoc: wallLocations){
			if(spatLoc.isSameAs(middle))
				wallInFrontOf = true;
		}
		//WUMPUS RELATIONS
		boolean wumpusInFrontOf = false;
		if(wumpusLocation != null && wumpusLocation.isSameAs(middle))
			wumpusInFrontOf = true;
		
		
		//System.out.println("w " + wumpusInFrontOf + " pit " + pitInFrontOf);
		boolean safeToProceed = false;
		if(!wumpusInFrontOf && !pitInFrontOf)
			safeToProceed = true;
			
		//**************PRODUCTION RULES******************
		//Grab gold if it is right there.
		if(agentLocation.isSameAs(goldLocation)){
			action.setContent(Action.GRAB);
			//System.out.println("Action 1 grab");
			return action;
		}
		//Shoot the wumpus if it is in range.
		if(wumpusLocation.isSameAs(middle)){
			action.setContent(Action.SHOOT);
			//System.out.println("Action 2 shoot");
			return action;
		}
		//ORIENT TOWARD WUMPUS AND GOLD
		if(agentLocation.isSameAs(left)){ 
			if(wumpusLocation.isSameAs(upperLeft) || goldLocation.isSameAs(upperLeft)){
				action.setContent(Action.TURN_LEFT);
				//System.out.println("Action 3");
				return action;
			}
			if(wumpusLocation.isSameAs(bottomLeft) || goldLocation.isSameAs(bottomLeft)){
				action.setContent(Action.TURN_RIGHT);
				//System.out.println("Action 4");
				return action;
			}
		}
		if(agentLocation.isSameAs(right)){ 
			if(wumpusLocation.isSameAs(bottomRight) || goldLocation.isSameAs(bottomRight)){
				action.setContent(Action.TURN_LEFT);
				//System.out.println("Action 5");
				return action;
			}
			if(wumpusLocation.isSameAs(upperRight) || goldLocation.isSameAs(upperRight)){
				action.setContent(Action.TURN_RIGHT);
				//System.out.println("Action 6");
				return action;
			}
		}
		if(agentLocation.isSameAs(top)){ 
			if(wumpusLocation.isSameAs(upperLeft) || goldLocation.isSameAs(upperLeft)){
				action.setContent(Action.TURN_RIGHT);
				//System.out.println("Action 7");
				return action;
			}
			if(wumpusLocation.isSameAs(upperRight) || goldLocation.isSameAs(upperRight)){
				action.setContent(Action.TURN_LEFT);
				//System.out.println("Action 8");
				return action;
			}
		}
		if(agentLocation.isSameAs(bottom)){ 
			if(wumpusLocation.isSameAs(bottomLeft) || goldLocation.isSameAs(bottomLeft)){
				action.setContent(Action.TURN_LEFT);
				//System.out.println("Action 9");
				return action;
			}
			if(wumpusLocation.isSameAs(bottomRight) || goldLocation.isSameAs(bottomRight)){
				action.setContent(Action.TURN_RIGHT);
				//System.out.println("Action 10");
				return action;
			}
		}//END ORIENT TOWARD WUMPUS AND GOLD
		//Head forward toward gold when it's there
		if(safeToProceed && goldLocation.isSameAs(middle)){
			action.setContent(Action.GO_FORWARD);
			//System.out.println("Action 11");
			return action;
		}
		//No wall and safe, then go forward.
		if(safeToProceed && !wallInFrontOf){//go forward if safe
			action.setContent(Action.GO_FORWARD);
			//System.out.println("Action 12");
			return action;
		}
		//Halt in unwinnable situation
		if(!safeToProceed && goldLocation.isSameAs(middle) && !wumpusLocation.isSameAs(middle)){
			action.setContent(Action.END_TRIAL);
			//System.out.println("Action 13");
			return action;
		}
		//System.out.println("left right counter" + leftRightCounter);
		//Turn with periodicity if faced w/ just a pit or wall in front of 
		if(leftRightCounter % 3 == 0 || leftRightCounter % 5 == 0 || leftRightCounter % 8 == 0 ||
			leftRightCounter % 16 == 0){
			action.setContent(Action.TURN_LEFT);
			//System.out.println("Action 14 go left");
		}else{
			action.setContent(Action.TURN_RIGHT);
			//System.out.println("Action 15 go right ");
		}
		leftRightCounter ++;
		return action;
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