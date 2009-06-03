package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Starter;
import edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets.SpatialLinkCodeletAction;
import edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets.StructureBuildingCodeletImpl;

public class StructureBuildingCodeletDriver implements Runnable, Stoppable, Starter, WorkspaceListener {

	//Basics
	private boolean keepRunning = true;
	private FrameworkTimer timer;	
	private FrameworkGui testGui;
	
	private WorkspaceImpl workspace;
	private WorkspaceContent workspaceContent;
	private Map<CodeletActivatingContextImpl, StructureBuildingCodelet> codeletMap = new HashMap<CodeletActivatingContextImpl, StructureBuildingCodelet>();//TODO: equals, hashCode

	private final double defaultCodeletActivation = 1.0;		
	private List<Thread> codeletThreads = new ArrayList<Thread>();
	private List<Stoppable> codelets = new ArrayList<Stoppable>();
	
	public StructureBuildingCodeletDriver(FrameworkTimer timer, WorkspaceImpl w){
		this.timer = timer;
		workspace = w;	
	}//method
	
	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}//method

	public synchronized void receiveWorkspaceContent(WorkspaceContent content) {
		workspaceContent = content;		
	}//method

	public void run(){
		boolean usesPBuffer = true, usesEBuffer = false, usesPBroads = false;
		//TODO: Abstract this out of this class
		CodeletsDesiredContent objective = new CodeletsDesiredContent();
		CodeletAction actions = new SpatialLinkCodeletAction();		
		spawnNewCodelet(workspace, usesPBuffer, usesEBuffer, usesPBroads, defaultCodeletActivation, objective, actions);
		
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());
			}catch(Exception e){}//TODO: if PBUFFER Content is changed wake up
			timer.checkForStartPause();
			//if BufferContent activates a sbCodelet's context start a new codelet
			getWorkspaceContent();		
		}//while	
	}//method

	private void spawnNewCodelet(Workspace w, boolean usesPBuffer, boolean usesEBuffer, boolean usesPBroads, 
								 double startingActivation, CodeletsDesiredContent context, CodeletAction actions){
		//TODO: Abstract this out of this class
		List<CodeletReadable> buffers = new ArrayList<CodeletReadable>();
		buffers.add(workspace.getCSM());
		if(usesPBuffer)
			buffers.add(workspace.getPerceptualBuffer());
		if(usesEBuffer)
			buffers.add(workspace.getEpisodicBuffer());
		if(usesPBroads)
			buffers.add(workspace.getBroadcastBuffer());
		
		StructureBuildingCodelet newCodelet = new StructureBuildingCodeletImpl(timer, buffers, 
												    defaultCodeletActivation, context, actions);
		long threadNumber = codeletThreads.size() + 1;
		Thread codeletThread = new Thread((Runnable)newCodelet, "CODELET: " + threadNumber);
		codeletThreads.add(codeletThread);   
		codelets.add((Stoppable) newCodelet);	
		codeletThread.start();	
	}//method

	private void getWorkspaceContent() {
		NodeStructure struct = (NodeStructure)workspaceContent;
		if(struct != null){
			Collection<Node> nodes = struct.getNodes();
			for(Node n: nodes){
				//Activate codelets
			}
			
			List<Object> guiContent = new ArrayList<Object>();			
			guiContent.add(struct.getNodes().size());
			guiContent.add(struct.getLinks().size());			
			testGui.receiveGuiContent(FrameworkGui.FROM_SB_CODELETS, guiContent);
		}//if not null
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method

	public int getThreadCount() {
		return codeletThreads.size();
	}

	public void stopThreads() {
		System.out.println("\n Stopping Structure-building codelets");
		int size = codelets.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = codelets.get(i);
			if(s != null)
				s.stopRunning();					
		}//for	
	}//method

}//class