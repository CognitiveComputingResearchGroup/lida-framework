package edu.memphis.ccrg.lida.example.vision.codelets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.BasicCodeletAction;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.StructBuildCodeletImpl;

public class VisionSBCodeDriver implements Runnable, Stoppable, ThreadSpawner, WorkspaceListener {

	private boolean keepRunning = true;
	private FrameworkTimer frameworkTimer;	
	private FrameworkGui flowGui;
	private WorkspaceImpl workspace;
	private NodeStructureImpl workspaceContent = new NodeStructureImpl();
	//private Map<CodeletActivatingContextImpl, StructureBuildingCodelet> codeletMap = new HashMap<CodeletActivatingContextImpl, StructureBuildingCodelet>();//TODO: equals, hashCode	
	/**
	 * create a thread pool of indeterminate size
	 */
    private ExecutorService execSvc = Executors.newCachedThreadPool();
	private final double defaultActiv = 1.0;	
	private List<Stoppable> codeletStoppables = new ArrayList<Stoppable>();
	
	public VisionSBCodeDriver(Workspace w, FrameworkTimer timer, FrameworkGui flowGui){
		workspace = (WorkspaceImpl) w;
		frameworkTimer = timer;
		this.flowGui = flowGui;
	}//method

	public synchronized void receiveWorkspaceContent(WorkspaceContent content) {
		workspaceContent = (NodeStructureImpl) content;		
	}//method

	public void run(){
		while(keepRunning){
			try{Thread.sleep(frameworkTimer.getSleepTime());
			}catch(Exception e){}
			frameworkTimer.checkForStartPause();
			//
			activateCodelets();		
		}//while	
	}//method

	/**
	 * TODO:if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		Collection<Node> nodes = workspaceContent.getNodes();
		for(Node n: nodes){
			boolean usesPBuffer = true, usesEBuffer = false, usesPBroads = false;
			//TODO: Abstract this out of this class
			CodeletsDesiredContent objective = new CodeletsDesiredContent();
			CodeletAction actions = new BasicCodeletAction();		
			spawnNewCodelet(workspace, usesPBuffer, usesEBuffer, usesPBroads, defaultActiv, objective, actions);
			
		}
			
		List<Object> guiContent = new ArrayList<Object>();			
		guiContent.add(workspaceContent.getNodeCount());
		guiContent.add(workspaceContent.getLinkCount());			
		flowGui.receiveGuiContent(FrameworkGui.FROM_SB_CODELETS, guiContent);
	}//method
	
	private void spawnNewCodelet(Workspace w, boolean usesPBuffer, boolean usesEBuffer, boolean usesPBroads, 
			 double startingActivation, CodeletsDesiredContent context, CodeletAction actions){

		List<CodeletReadable> buffers = new ArrayList<CodeletReadable>();
		buffers.add(workspace.getCSM());
		if(usesPBuffer)
			buffers.add(workspace.getPerceptualBuffer());
		if(usesEBuffer)
			buffers.add(workspace.getEpisodicBuffer());
		if(usesPBroads)
			buffers.add(workspace.getBroadcastBuffer());
		
		StructBuildCodeletImpl sbc = new StructBuildCodeletImpl(frameworkTimer, buffers, 
											defaultActiv, context, actions);
		codeletStoppables.add(sbc);	
        execSvc.execute(sbc);//put codelet in the work queue for the thread pool
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method

	public int getThreadCount() {
		return codeletStoppables.size();
	}//method

	public void stopThreads() {
		System.out.println("\n Stopping Structure-building codelets");
		int size = codeletStoppables.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = codeletStoppables.get(i);
			if(s != null)
				s.stopRunning();					
		}//for	
	}//method

}//class