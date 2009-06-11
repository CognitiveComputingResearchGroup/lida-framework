package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 * Receives WorkspaceContent, calculates the next action to taken, 
 * and sends that action to the Environment module
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver implements ProceduralMemory, Runnable, Stoppable, BroadcastListener{

	//FIELDS
	private FrameworkTimer timer;
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkGui testGui;
	private NodeStructureImpl broadcast = new NodeStructureImpl();
	private List<ProceduralMemoryListener> listeners;
	private Set<Scheme> schemes = new HashSet<Scheme>();
		
	public ProceduralMemoryDriver(FrameworkTimer timer) {
		this.timer = timer;
		listeners = new ArrayList<ProceduralMemoryListener>();
	}//constructor

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);		
	}
		
	public void addFlowGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcast = (NodeStructureImpl)bc;		
	}

	/**
	 * This loop drives the procedural memory
	 */
	public void run() {
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			
			sendGuiContent();
			
		}//while	
	}//method
	
	private void sendGuiContent() {
		List<Object> content = new ArrayList<Object>();
		content.add(broadcast.getNodes().size());
		content.add(broadcast.getLinks().size());
		testGui.receiveGuiContent(FrameworkGui.FROM_PROCEDURAL_MEMORY, content);
	}//method

	public void stopRunning() {
		keepRunning = false;		
	}

}//class