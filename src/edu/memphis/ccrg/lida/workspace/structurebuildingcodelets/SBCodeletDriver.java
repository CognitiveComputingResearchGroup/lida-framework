package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class SBCodeletDriver extends GenericModuleDriver implements
		ThreadSpawner, WorkspaceListener, GuiContentProvider {

	private SBCodeletFactory sbCodeletFactory;
	//
	private NodeStructureImpl workspaceContent = new NodeStructureImpl();
	// private Map<CodeletActivatingContextImpl, StructureBuildingCodelet>
	// codeletMap = new HashMap<CodeletActivatingContextImpl,
	// StructureBuildingCodelet>();//TODO: equals, hashCode
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private List<Runnable> runningCodelets = new ArrayList<Runnable>();

	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<Object> guiContent = new ArrayList<Object>();

	public SBCodeletDriver(Workspace w, FrameworkTimer timer) {
		super(timer);
		sbCodeletFactory = SBCodeletFactory.getInstance(w, timer);
	}// method

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

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// TODO: CODE to determine when/what codelets to activate

		List<Object> guiContent = new ArrayList<Object>();
		guiContent.add(workspaceContent.getNodeCount());
		guiContent.add(workspaceContent.getLinkCount());
	}// method

	@SuppressWarnings("unused")
	/*
	 * @param type - See SBCodeletFactory for which integer values correspond to
	 * which type
	 */
	private void spawnNewCodelet(int type, double activation,
			NodeStructure context, CodeletAction actions) {
		StructureBuildingCodelet sbc = sbCodeletFactory.getCodelet(type,
				activation, context, actions);
		runningCodelets.add(sbc);
		// put codelet in the work queue for the thread pool
		executorService.execute(sbc);
	}// method

	public void stopSpawnedThreads() {
		executorService.shutdown();
		int size = runningCodelets.size();
		for (int i = 0; i < size; i++) {
			Runnable s = runningCodelets.get(i);
			if ((s != null)&&(s instanceof Stoppable)){
				((Stoppable)s).stopRunning();				
			}
		}// for
		System.out.println("all structure-building codelets told to stop");
	}// method

	public int getSpawnedThreadCount() {
		return runningCodelets.size();
	}// method

	@Override
	public void cycleStep() {
		activateCodelets();
		sendEvent();
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);

	}

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					FrameworkGuiEvent.CSM, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}
	}

	public void addRunnable(Runnable r) {
		runningCodelets.add(r);
		executorService.execute(r);
	}

	public List<Runnable> getAllRunnables() {

		return Collections.unmodifiableList(runningCodelets);
	}

	public void addTask(Runnable r) {
		// TODO Auto-generated method stub
		
	}

	public Collection<Runnable> getAllTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public void receiveFinishedTask(Runnable finishedTask, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	public void setInitialCallableTasks(
			List<? extends Callable<Object>> initialCallables) {
		// TODO Auto-generated method stub
		
	}

	public void setInitialTasks(List<? extends Runnable> initialRunnables) {
		// TODO Auto-generated method stub
		
	}

}// class