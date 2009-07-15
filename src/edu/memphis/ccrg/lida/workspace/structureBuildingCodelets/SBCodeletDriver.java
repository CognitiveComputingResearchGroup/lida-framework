package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import edu.memphis.ccrg.lida.framework.FrameworkExecutorService;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

public class SBCodeletDriver extends GenericModuleDriver implements	ThreadSpawner, GuiContentProvider{

	private SBCodeletFactory sbCodeletFactory;
	
	/**
	 * Used to execute the StructureBuildingCodelets
	 */
	private ThreadPoolExecutor executorService;
	
	/**
	 * A map of the running codelets where the key is the codelet's id
	 */
	private Map<Long, Stoppable> runningCodelets = new HashMap<Long, Stoppable>();

	// private Map<CodeletActivatingContextImpl, StructureBuildingCodelet>
	// codeletMap = new HashMap<CodeletActivatingContextImpl,
	// StructureBuildingCodelet>();

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

	public void startInitialRunnables(List<Runnable> initialRunnables) {
		for (Runnable r : initialRunnables){
			if(r instanceof StructureBuildingCodelet)
				executeCodelet((StructureBuildingCodelet) r);
			else
				System.out.println("In SBCodeletDriver, a noncodelet object was submitted for execution");
		}
	}
	
	public void startInitialCallables(List<Callable<Object>> initialCallables) {
		for (Callable<Object> c : initialCallables){
			if(c instanceof StructureBuildingCodelet)
				executeCodelet((StructureBuildingCodelet) c);
			else
				System.out.println("In SBCodeletDriver, a noncodelet object was submitted for execution");
		}
	}
	
	@Override
	public void cycleStep() {
		activateCodelets();
	}

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// TODO: CODE to determine when/what codelets to activate
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
		executeCodelet(sbc);
	}// method
	
	private void executeCodelet(StructureBuildingCodelet sbc){
		long id = sbc.getId();
		executorService.submit(sbc);
		synchronized(this){
			runningCodelets.put(id, sbc);
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * Since 
	 */
	public void receiveFinishedTask(FutureTask<Object> finishedTask, Throwable t) {
		Object o = null;
		try {
			o = finishedTask.get();
		}catch (InterruptedException e1){
			e1.printStackTrace();
		}catch (ExecutionException e1){
			e1.printStackTrace();
		}

		if(o != null){
			CodeletResult cr = (CodeletResult) o;
			removeFinishedCodelet(cr.getId());
			//Plan for other kinds of objects being returned depending on the codelet
		}
	}//method
	
	public boolean removeFinishedCodelet(Long codeletId){
		Stoppable removedCodelet = null;
		synchronized(this){
			removedCodelet = runningCodelets.remove(codeletId);
		}
		//System.out.println(runningCodelets.size());
		return removedCodelet != null;
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
		executorService.shutdownNow();//TODO: move this?
		System.out.println("all structure-building codelets told to stop");
	}// method

	public int getSpawnedThreadCount() {
		return runningCodelets.size();
	}// method

}// class