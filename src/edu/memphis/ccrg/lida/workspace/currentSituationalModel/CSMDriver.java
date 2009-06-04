package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class CSMDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private CurrentSituationalModel csm;
	private FrameworkTimer timer;
	private CSMListener guiListener;
	private FrameworkGui testGui;
	
	public CSMDriver(FrameworkTimer t, CurrentSituationalModel csm, CSMListener gui){
		timer = t;
		this.csm = csm;
		guiListener = gui;
	}

	public void run(){
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

	        csm.sendCSMContent();//TODO: send cue?
		}//while
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(Exception e){}
		keepRunning = false;		
	}//method

	public void addFlowGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
}//class
