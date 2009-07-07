package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkGuiProvider;
import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.BasicCodeletAction;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.StructBuildCodeletImpl;

public class StructBuildCodeletDriver implements FrameworkModuleDriver, ThreadSpawner, WorkspaceListener, FrameworkGuiProvider{

	private boolean keepRunning = true;
	private FrameworkTimer frameworkTimer;	
	private WorkspaceImpl workspace;
	private SBCodeletFactory sbCodeletFactory = SBCodeletFactory.getInstance();
	//
	private List<CodeletReadable> perceptualBuffer = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> episodicBuffer = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> broadcastQueue = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> allBuffers = new ArrayList<CodeletReadable>();
	private NodeStructureImpl workspaceContent = new NodeStructureImpl();
	//private Map<CodeletActivatingContextImpl, StructureBuildingCodelet> codeletMap = new HashMap<CodeletActivatingContextImpl, StructureBuildingCodelet>();//TODO: equals, hashCode	
	
	private double defaultActiv = 1.0;	
	private NodeStructure defaultObjective = new NodeStructureImpl();
	private CodeletAction defaultActions = new BasicCodeletAction();	
	//
	/**
	 * create a thread pool of indeterminate size
	 */
    private ExecutorService executorService = Executors.newCachedThreadPool();
	private List<Stoppable> runningCodelets = new ArrayList<Stoppable>();
	
	public StructBuildCodeletDriver(Workspace w, FrameworkTimer timer){
		workspace = (WorkspaceImpl) w;
		frameworkTimer = timer;
		//
		perceptualBuffer.add(workspace.getPerceptualBuffer());
		episodicBuffer.add(workspace.getEpisodicBuffer());
		broadcastQueue.add(workspace.getBroadcastBuffer());
		allBuffers.add(workspace.getPerceptualBuffer());
		allBuffers.add(workspace.getEpisodicBuffer());
		allBuffers.add(workspace.getBroadcastBuffer());		
	}//method

	/**
	 * Note that the Workspace receives this content from multiple buffers. So it may
	 * have originated from either the perceptual, episodic, or broadcast buffer.
	 */
	public synchronized void receiveWorkspaceContent(NodeStructure content, int originatingBuffer) {
		workspaceContent = (NodeStructureImpl) content;		
	}//method

	public void run(){
		
		spawnPerceptualCodelet(defaultActiv, defaultObjective, defaultActions);
//		spawnEpisodicCodelet(defaultActiv, defaultObjective, defaultActions);
//		spawnBroadcastCodelet(defaultActiv, defaultObjective, defaultActions);
//		spawnGeneralCodelet(defaultActiv, defaultObjective, defaultActions);
		
		while(keepRunning){
			try{
				Thread.sleep(frameworkTimer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}	
			frameworkTimer.checkForStartPause();
			//
			activateCodelets();	
			sendGuiContent();
		}//while	
	}//method

	/**
	 * TODO: if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		//CODE to determine when/what codelets to activate	
		
		List<Object> guiContent = new ArrayList<Object>();			
		guiContent.add(workspaceContent.getNodeCount());
		guiContent.add(workspaceContent.getLinkCount());
	}//method
	
	private void spawnPerceptualCodelet(double startActiv, NodeStructure objective, CodeletAction actions){
		spawnNewCodelet(perceptualBuffer, startActiv, objective, actions);
	}
	
	private void spawnEpisodicCodelet(double startActiv, NodeStructure objective, CodeletAction actions){
		spawnNewCodelet(episodicBuffer, startActiv, objective, actions);
	}
	
	private void spawnBroadcastCodelet(double startActiv, NodeStructure content, CodeletAction actions){
		spawnNewCodelet(broadcastQueue, startActiv, content, actions);
	}

	private void spawnGeneralCodelet(double startActiv, NodeStructure content, CodeletAction actions){
		spawnNewCodelet(allBuffers, startActiv, content, actions);
	}
	
	private void spawnNewCodelet(List<CodeletReadable> buffers, double activation, 
									NodeStructure context, CodeletAction actions){
		
		StructBuildCodeletImpl sbc = new StructBuildCodeletImpl(frameworkTimer, workspace, buffers, 
																activation, context, actions);
		//StructBuildCodeletImpl sbc2 = sbCodeletFactory.getCodelet(type, activation, args);
		runningCodelets.add(sbc);	
        executorService.execute(sbc);//put codelet in the work queue for the thread pool
	}//method

	public void stopRunning(){
		keepRunning = false;		
		stopRunningSpawnedThreads();
	}//method

	public void stopRunningSpawnedThreads() {
		executorService.shutdown();
		int size = runningCodelets.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = runningCodelets.get(i);
			if(s != null)
				s.stopRunning();					
		}//for	
		System.out.println("all structure-building codelets told to stop");
	}//method
	
	public int getSpawnedThreadCount() {
		return runningCodelets.size();
	}//method

	public void addFrameworkGui(FrameworkGui listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendGuiContent() {
		// TODO Auto-generated method stub
		
	}

}//class