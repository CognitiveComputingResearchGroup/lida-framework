package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.List;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver implements Runnable, Stoppable, BroadcastListener{

	private boolean keepRunning = true;
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();
	private NodeStructure broadcast = new NodeStructureImpl();
	//
	private ProceduralMemory procMem;
	private FrameworkTimer timer;
	private FrameworkGui flowGui;
		
	public ProceduralMemoryDriver(ProceduralMemory pm, FrameworkTimer timer, FrameworkGui gui) {
		procMem = pm;
		this.timer = timer;
		flowGui = gui;		
	}//constructor

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);		
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
		flowGui.receiveGuiContent(FrameworkGui.FROM_PROCEDURAL_MEMORY, procMem.getGuiContent());
	}//method

	public void stopRunning() {
		keepRunning = false;		
	}

}//class