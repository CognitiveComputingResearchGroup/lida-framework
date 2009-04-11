package edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
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
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Action;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WorldApplication;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusIDs;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;

/**
 * Receives WorkspaceContent, calculates the next action to taken, 
 * and sends that action to the Environment module
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver implements Runnable, Stoppable, BroadcastListener, WorkspaceListener{

	//FIELDS
	private FrameworkTimer timer;
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkGui testGui;
	
	//Specific to this module
	private WorldApplication environment;//To send output
	private BroadcastContent broadcastContent = null;//Received input TODO: not used in this implementation
	private NodeStructure workspaceStructure = new GraphImpl();//The input for this impl.
	/**
	 * Used to paused the action selection.  Its value is changed from GUI click.
	 */
	private boolean shouldDoNoOp = true;
		
	public ProceduralMemoryDriver(FrameworkTimer timer, WorldApplication environ) {
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
		while(keepRunning){
			try{Thread.sleep(90 + timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			
			sendGuiContent();
			if(coolDown == 0){
				ActionContent behaviorContent = getAppropriateBehavior();
				environment.receiveBehaviorContent(behaviorContent);
				coolDown = 1;
			}else
				coolDown--;		
		}//while		
	}//method
	
	/**
	 * Observer pattern receive method
	 */
	public void receiveWorkspaceContent(WorkspaceContent content) {
		workspaceStructure = (NodeStructure)content;		
	}
	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = bc;		
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
	 * based on this information
	 * 
	 * @return
	 */
	private ActionContent getAppropriateBehavior() {		
		ActionContentImpl action = new ActionContentImpl(Action.NO_OP);
		if(shouldDoNoOp)
			return action;
		
		GraphImpl g = (GraphImpl)workspaceStructure;
		Map<Long, Node> nodeMap = g.getNodeMap();
		Set<Link> links = g.getLinks();
		
		SpatialLocation agentLocation = new SpatialLocation();
		//char agentDirection = ' ';
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
			Node sink = (Node)temp.getSink();		
			//All these links have sink as spatial location so I know source is the node.
			long sourceID = source.getId();
			SpatialLocation sl = (SpatialLocation)sink;
			
			if(source.getId() == WumpusIDs.wumpus && temp.getType() == LinkType.inLineWith)
				inLineWithWumpus = true;				
			
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
				//agentDirection = sl.getDirection();				
			}else if(sourceID == WumpusIDs.gold){
				goldLocation = sl;
			}else if(sourceID == WumpusIDs.wumpus){
				if(sl.isAtTheSameLocationAs(1, 1)){
					safeToProceed = false;
					System.out.println("wumpus not safe");
				}
				wumpusLocation = sl;
			}
		}//for
		
		//Production Rules		
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

	public synchronized void pauseActionSelection() {
		shouldDoNoOp = !shouldDoNoOp;		
		System.out.println("action selection operating " + !shouldDoNoOp);
	}

}//class