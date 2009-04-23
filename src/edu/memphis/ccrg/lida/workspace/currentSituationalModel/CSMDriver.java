package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class CSMDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private CurrentSituationalModel csm;
	private long threadID;
	private FrameworkTimer timer;
	private CSMListener guiListener;
	private FrameworkGui testGui;
	
	public CSMDriver(FrameworkTimer t, CurrentSituationalModel csm, CSMListener gui){
		timer = t;
		this.csm = csm;
		guiListener = gui;
	}

	public void run(){
		//int counter = 0;		
		//long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(25 + timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
		
			NodeStructure struct = csm.getContent();
			Collection<Node> nodes = struct.getNodes();
			Collection<Link> links = struct.getLinks();
			
			List<Object> content = new ArrayList<Object>();
	        content.add(nodes.size());
	        content.add(links.size());
	        testGui.receiveGuiContent(FrameworkGui.FROM_CSM, content);

	        csm.sendCSMContent();
			
			//CoalitionImpl coalition = new CoalitionImpl(content);
			//BroadcastContentImpl content = new BroadcastContentImpl(struct);
			
			//counter++;			
		}//while keepRunning
		//long finishTime = System.currentTimeMillis();				
		//System.out.println("CSM: Ave. cycle time: " + 
		//					Printer.rnd((finishTime - startTime)/(double)counter));
		//System.out.println("CSM: Num. cycles: " + counter);		
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(Exception e){}
		keepRunning = false;		
	}//public void stopRunning()
	
	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
}
