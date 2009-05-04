package edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.actionSelection.ActionContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
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
public class RandomProceduralMemoryDriver implements ProceduralMemory, Runnable, Stoppable, PAMListener{

	//FIELDS
	private FrameworkTimer timer;
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkGui testGui;
	private final int numCoolDownCycles = 1;
	
	//Specific to this module
	private WumpusWorld environment;//To send output
	//private BroadcastContent broadcastContent = null;//TODO: not used in this implementation
	private NodeStructure workspaceStructure = new RyanNodeStructure();//The input for this impl.
	/**
	 * Used to paused the action selection.  Its value is changed from GUI click.
	 */
	private boolean inManualMode = false;
		
	public RandomProceduralMemoryDriver(FrameworkTimer timer, WumpusWorld environ) {
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
		SpatialLocation agentLocation = new SpatialLocation();
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
		//PRODUCTION RULES
		//Grab gold if it is right there.
		if(agentLocation.isSameAs(goldLocation)){
			action.setContent(Action.GRAB);
			return action;
		}
		double d = Math.random();
		if(d < (1.0 / 3.0)){
			action.setContent(Action.TURN_LEFT);
		}else if(d < (2.0 / 3.0)){
			action.setContent(Action.TURN_RIGHT);
		}else{
			action.setContent(Action.GO_FORWARD);
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