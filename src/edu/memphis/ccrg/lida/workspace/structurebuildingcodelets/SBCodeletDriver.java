package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.TaskSpawner;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

public class SBCodeletDriver extends GenericModuleDriver implements TaskSpawner, GuiContentProvider {

	private Logger logger=Logger.getLogger("lida.workspace.structurebuildingcodelets.SBCodeletDriver");
	private SBCodeletFactory sbCodeletFactory;
	// private Map<CodeletActivatingContextImpl, StructureBuildingCodelet>
	// codeletMap = new HashMap<CodeletActivatingContextImpl,
	// StructureBuildingCodelet>();//TODO: equals, hashCode
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private List<Runnable> runningCodelets = new ArrayList<Runnable>();
	//Gui
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<Object> guiContent = new ArrayList<Object>();

	public SBCodeletDriver(Workspace w, FrameworkTaskManager timer) {
		super(timer);
		sbCodeletFactory = SBCodeletFactory.getInstance(w, timer);
	}// method
	
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}
	
	@Override
	public void cycleStep() {
		activateCodelets();
		sendEvent();
	}

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// TODO: CODE to determine when/what codelets to activate
	}// method

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					FrameworkGuiEvent.CSM, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}
	}//method

	@SuppressWarnings("unused")
	/*
	 * @param type - See SBCodeletFactory for which integer values correspond to
	 * which type
	 */
	private void spawnNewCodelet(int type, double activation, NodeStructure context, 
								 							  CodeletAction actions){
		StructureBuildingCodelet sbc = sbCodeletFactory.getCodelet(type, activation, 
																   context, actions);
		addTask(sbc);
	}// method

	public void setInitialCallableTasks(List<? extends Callable<Object>> initialCallables) {
		for (Callable<Object> c : initialCallables){
			FutureTask<Object> ft = new FutureTask<Object>(c);
			addTask(ft);
		}
	}
	public void setInitialRunnableTasks(List<? extends Runnable> initialRunnables) {
		for (Runnable r : initialRunnables)
			addTask(r);
	}
	public void addTask(Runnable r) {
		runningCodelets.add(r);
		executorService.execute(r);
	}
	public Collection<Runnable> getAllTasks() {
		return Collections.unmodifiableList(runningCodelets);
	}
	public void receiveFinishedTask(Runnable finishedTask, Throwable t) {
		runningCodelets.remove(finishedTask);
	}
	
	public int getSpawnedThreadCount() {
		return runningCodelets.size();
	}// method
	public void stopSpawnedThreads() {
		executorService.shutdown();
		int size = runningCodelets.size();
		for (int i = 0; i < size; i++) {
			Runnable s = runningCodelets.get(i);
			if ((s != null)&&(s instanceof LidaTask))
				((LidaTask)s).stopRunning();				
			
		}// for
		logger.info("all structure-building codelets told to stop");
	}// method

}// class