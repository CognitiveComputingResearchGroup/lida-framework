package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class StructBuildCodeletDriver implements Runnable, Stoppable, ThreadSpawner, WorkspaceListener, GuiContentProvider {

	private boolean keepRunning = true;
	private FrameworkTimer frameworkTimer;	
	private FrameworkGui flowGui;
	private WorkspaceImpl workspace;
	//
	private List<CodeletReadable> perceptualCodeletReadables;
	private List<CodeletReadable> episodicCodeletReadables;
	private List<CodeletReadable> broadcastCodeletReadables;
	private List<CodeletReadable> allCodeletReadables;
	private NodeStructureImpl bufferContent = new NodeStructureImpl();
	//private Map<CodeletActivatingContextImpl, StructureBuildingCodelet> codeletMap = new HashMap<CodeletActivatingContextImpl, StructureBuildingCodelet>();//TODO: equals, hashCode	
	
	private double defaultActiv = 1.0;	
	private NodeStructure defaultObjective = new NodeStructureImpl();
	private CodeletAction defaultActions = new BasicCodeletAction();	
	//
	/**
	 * create a thread pool of indeterminate size
	 */
    private ExecutorService execSvc = Executors.newCachedThreadPool();
	private List<Stoppable> codeletStoppables = new ArrayList<Stoppable>();
	private List<Object> guiContent = new ArrayList<Object>();
	
	public StructBuildCodeletDriver(Workspace w, FrameworkTimer timer, FrameworkGui flowGui){
		workspace = (WorkspaceImpl) w;
		frameworkTimer = timer;
		this.flowGui = flowGui;
		//
		perceptualCodeletReadables.add(workspace.getPerceptualBuffer());
		episodicCodeletReadables.add(workspace.getEpisodicBuffer());
		broadcastCodeletReadables.add(workspace.getBroadcastBuffer());
		allCodeletReadables.add(workspace.getPerceptualBuffer());
		allCodeletReadables.add(workspace.getEpisodicBuffer());
		allCodeletReadables.add(workspace.getBroadcastBuffer());		
	}//method

	public synchronized void receiveWorkspaceContent(NodeStructure content) {
		bufferContent = (NodeStructureImpl) content;		
	}//method

	public void run(){
		
//		spawnPerceptualCodelet(defaultActiv, defaultObjective, defaultActions);
//		spawnEpisodicCodelet(defaultActiv, defaultObjective, defaultActions);
//		spawnBroadcastCodelet(defaultActiv, defaultObjective, defaultActions);
//		spawnGeneralCodelet(defaultActiv, defaultObjective, defaultActions);
		
		while(keepRunning){
			try{Thread.sleep(frameworkTimer.getSleepTime());
			}catch(Exception e){}
			frameworkTimer.checkForStartPause();
			//
			activateCodelets();	
			flowGui.receiveGuiContent(FrameworkGui.FROM_SBCODELETS, getGuiContent());
		}//while	
	}//method

	/**
	 * TODO:if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		//CODE to determine when/what codelets to activate	
		
		guiContent.clear();
		guiContent.add(bufferContent.getNodeCount());
		guiContent.add(bufferContent.getLinkCount());			
	}//method
	
	private void spawnPerceptualCodelet(double startActiv, NodeStructure objective, CodeletAction actions){
		spawnNewCodelet(perceptualCodeletReadables, startActiv, objective, actions);
	}
	
	private void spawnEpisodicCodelet(double startActiv, NodeStructure objective, CodeletAction actions){
		spawnNewCodelet(episodicCodeletReadables, startActiv, objective, actions);
	}
	
	private void spawnBroadcastCodelet(double startActiv, NodeStructure content, CodeletAction actions){
		spawnNewCodelet(broadcastCodeletReadables, startActiv, content, actions);
	}

	private void spawnGeneralCodelet(double startActiv, NodeStructure content, CodeletAction actions){
		spawnNewCodelet(allCodeletReadables, startActiv, content, actions);
	}
	
	private void spawnNewCodelet(List<CodeletReadable> buffers, double startingActivation, 
									NodeStructure context, CodeletAction actions){
		
		StructBuildCodeletImpl sbc = new StructBuildCodeletImpl(frameworkTimer, workspace, buffers, 
											defaultActiv, context, actions);
		codeletStoppables.add(sbc);	
        execSvc.execute(sbc);//put codelet in the work queue for the thread pool
	}//method

	public void stopRunning(){
		keepRunning = false;		
		stopSpawnedThreads();
	}//method

	public int getSpawnedThreadCount() {
		return codeletStoppables.size();
	}//method

	public void stopSpawnedThreads() {
		System.out.println("\n Stopping Structure-building codelets");
		int size = codeletStoppables.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = codeletStoppables.get(i);
			if(s != null)
				s.stopRunning();					
		}//for	
	}//method

	public List<Object> getGuiContent() {
		return guiContent;
	}

}//class