package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.memphis.ccrg.lida.framework.FrameworkExecutorService;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGui;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class SBCodeletDriver extends GenericModuleDriver implements
		ThreadSpawner, WorkspaceListener, GuiContentProvider {

	private SBCodeletFactory sbCodeletFactory;

	private NodeStructureImpl workspaceContent = new NodeStructureImpl();
	// private Map<CodeletActivatingContextImpl, StructureBuildingCodelet>
	// codeletMap = new HashMap<CodeletActivatingContextImpl,
	// StructureBuildingCodelet>();//TODO: equals, hashCode
	private ThreadPoolExecutor executorService;
	private Map<Integer, Stoppable> runningCodelets = new HashMap<Integer, Stoppable>();

	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
	private List<Object> guiContent = new ArrayList<Object>();

	public SBCodeletDriver(Workspace w, FrameworkTimer timer) {
		super(timer);
		sbCodeletFactory = SBCodeletFactory.getInstance(w, timer);
	
		int corePoolSize = 5;
		int maxPoolSize = 10;
	    long keepAliveTime = 10;
	    ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(5);
	    executorService = new FrameworkExecutorService(this, corePoolSize, maxPoolSize, keepAliveTime, 
	    											   TimeUnit.SECONDS, taskQueue);
	}// method

	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
	}

	public void setInitialRunnables(List<Runnable> initialRunnables) {
		for (Runnable r : initialRunnables)
			executorService.execute(r);
	}

	/**
	 * Note that the Workspace receives this content from multiple buffers. So
	 * it may have originated from either the perceptual, episodic, or broadcast
	 * buffer.
	 */
	public synchronized void receiveWorkspaceContent(int originatingBuffer,
			NodeStructure content) {
		workspaceContent = (NodeStructureImpl) content;
	}// method
	
	@Override
	public void cycleStep() {
		activateCodelets();
		sendGuiContent();
	}

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// TODO: CODE to determine when/what codelets to activate

		guiContent.clear();
		guiContent.add(workspaceContent.getNodeCount());
		guiContent.add(workspaceContent.getLinkCount());
	}// method
	
	public void sendGuiContent() {
		for (FrameworkGui fg : guis)
			fg.receiveGuiContent(FrameworkGui.FROM_SBCODELETS, guiContent);
	}

	@SuppressWarnings("unused")
	/*
	 * @param type - See SBCodeletFactory for which integer values correspond to
	 * which type
	 */
	private void spawnNewCodelet(int type, double activation,
			NodeStructure context, CodeletAction actions) {
		StructureBuildingCodelet sbc = sbCodeletFactory.getCodelet(type,
				activation, context, actions);
		synchronized(this){
			runningCodelets.put(sbc.hashCode(), sbc);
		}
		// put codelet in the work queue for the thread pool
		executorService.execute(sbc);
	}// method
	
	public void receiveFinishedTask(Runnable r, Throwable t) {
		StructureBuildingCodelet sbc = (StructureBuildingCodelet) r;
		synchronized(this){
			runningCodelets.remove(sbc.hashCode());
		}
	}

	public void stopRunning() {
		keepRunning = false;
		stopSpawnedThreads();
	}// method

	public void stopSpawnedThreads() {
		int size = runningCodelets.size();
		for (int i = 0; i < size; i++) {
			Stoppable s = runningCodelets.get(i);
			if (s != null)
				s.stopRunning();
		}// for
		executorService.shutdownNow();
		System.out.println("all structure-building codelets told to stop");
	}// method

	public int getSpawnedThreadCount() {
		return runningCodelets.size();
	}// method

}// class