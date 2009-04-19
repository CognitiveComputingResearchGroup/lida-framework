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
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Action;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusWorld;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusIDs;
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
	private boolean shouldDoNoOp = false;
		
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
		Map<Long, Node> nodeMap = struct.getNodeMap();
		//Map<String, Link> linkMap = struct.getLinkMap();
		//AGENT		
		SpatialLocation agentLocation = new SpatialLocation();
		if(nodeMap.containsKey(WumpusIDs.agent)){
			RyanPamNode agent = (RyanPamNode) nodeMap.get(WumpusIDs.agent);
			agentLocation = agent.getLocation();
		}
		//GOLD
		SpatialLocation goldLocation = new SpatialLocation(-5, -5);
		LinkType goldRelation = LinkType.none;
		if(nodeMap.containsKey(WumpusIDs.gold)){
			RyanPamNode gold = (RyanPamNode) nodeMap.get(WumpusIDs.gold);
			goldLocation = gold.getLocation();
			//
			Set<Link> goldLinks = struct.getLinks(gold);
			if(goldLinks != null)
				for(Link temp: goldLinks)
					if(temp.getType() != LinkType.spatial)
						goldRelation = temp.getType();
		}
		//WUMPUS
		SpatialLocation wumpusLocation = new SpatialLocation();
		LinkType wumpusRelation = LinkType.none;
		boolean inLineWithWumpus = false;
		if(nodeMap.containsKey(WumpusIDs.wumpus)){
			RyanPamNode wumpus = (RyanPamNode) nodeMap.get(WumpusIDs.wumpus);
			wumpusLocation = wumpus.getLocation();
			//
			Set<Link> wumpusLinks = struct.getLinks(wumpus);
			if(wumpusLinks != null){
				for(Link temp: wumpusLinks){
					LinkType tempType = temp.getType();
					if(tempType != LinkType.spatial){
						wumpusRelation = temp.getType();
						if(wumpusRelation == LinkType.inLineWith || wumpusRelation == LinkType.inFrontOf)
							inLineWithWumpus = true;
					}
				}
			}//
		}//
		//PITS
		Set<SpatialLocation> pitLocations = new HashSet<SpatialLocation>();
		if(nodeMap.containsKey(WumpusIDs.pit)){
			RyanPamNode pit = (RyanPamNode) nodeMap.get(WumpusIDs.pit);
			pitLocations = pit.getLocations();
		}
		//WALLS
		Set<SpatialLocation> wallLocations = new HashSet<SpatialLocation>();
		if(nodeMap.containsKey(WumpusIDs.wall)){
			RyanPamNode wall = (RyanPamNode) nodeMap.get(WumpusIDs.wall);
			wallLocations = wall.getLocations();
		}
		//CALC PRECONDITIONS
		boolean pitInFrontOf = false;
		for(SpatialLocation spatLoc: pitLocations)
			if(spatLoc.isAtTheSameLocationAs(1, 1))
				pitInFrontOf = true;
		
		boolean wallInFrontOf = false;	
		for(SpatialLocation spatLoc: wallLocations)
			if(spatLoc.isAtTheSameLocationAs(1, 1))
				wallInFrontOf = true;
		
		boolean wumpusInFrontOf = false;
		if(wumpusLocation != null && wumpusLocation.isAtTheSameLocationAs(1, 1))
			wumpusInFrontOf = true;
		
		boolean safeToProceed = false;
		if(!wumpusInFrontOf && !pitInFrontOf)
			safeToProceed = true;

		//Production Rules		
		if(agentLocation.isAtTheSameLocationAs(goldLocation)){//Grab gold if on the same square
			action.setContent(Action.GRAB);
		}else if(inLineWithWumpus){//Shoot wumpus if in line w/ it
			action.setContent(Action.SHOOT);
		}else if(goldRelation == LinkType.rightOf || wumpusRelation == LinkType.rightOf){//turn right when gold to right
			action.setContent(Action.TURN_RIGHT);
		}else if(goldRelation == LinkType.leftOf || wumpusRelation == LinkType.leftOf){//turn left when gold to left
			action.setContent(Action.TURN_LEFT);
		}else if(safeToProceed && (goldRelation == LinkType.inLineWith || goldRelation == LinkType.inFrontOf)){
			action.setContent(Action.GO_FORWARD);
		}else if(safeToProceed && !wallInFrontOf){//go forward if safe
			if(Math.random() > 0.1)
				action.setContent(Action.GO_FORWARD);
			else
				action.setContent(Action.TURN_LEFT);
		}else if(!safeToProceed && goldRelation == LinkType.inFrontOf && wumpusRelation != LinkType.inFrontOf){//Halt in unwinnable situation
			action.setContent(Action.END_TRIAL);
		}else if(Math.random() > 0.5){ //else turn left or right randomly
			action.setContent(Action.TURN_LEFT);
		}else{
			action.setContent(Action.TURN_RIGHT);
		}
		
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