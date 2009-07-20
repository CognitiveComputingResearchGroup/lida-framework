package edu.memphis.ccrg.lida.attention;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import edu.memphis.ccrg.lida.framework.FrameworkExecutorService;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.BroadcastLearner;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionDriver extends GenericModuleDriver implements ThreadSpawner, BroadcastListener, BroadcastLearner{

	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	//
	private double defaultActiv = 1.0;//TODO: move these to factory?
    private ExecutorService executorService = Executors.newCachedThreadPool();
	private List<Runnable> runningCodelets = new ArrayList<Runnable>();
	private NodeStructure broadcastContent;
	
	public AttentionDriver(FrameworkTimer timer, CurrentSituationalModel csm, GlobalWorkspace gwksp){
		super(timer);
		this.csm = csm;
		global = gwksp;
		//
		int corePoolSize = 5;
		int maxPoolSize = 10;
	    long keepAliveTime = 10;
	    ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(5);
	    executorService = new FrameworkExecutorService(this, corePoolSize, maxPoolSize, keepAliveTime, 
	    											   TimeUnit.SECONDS);
	}

	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	@Override
	public void cycleStep() {
		activateCodelets();		
	}
		
	public void activateCodelets(){
		//TODO: 
			}	
			
//	private void spawnAttentionCodelet(double activ, NodeStructure soughtContent){
//		AttentionCodeletImpl ac = new AttentionCodeletImpl(csm, global, activ);
//		runningCodelets.add(ac);	
//		executorService.execute(ac);//put codelet in the work queue for the thread pool
//	}//method
	
//	private void executeCodelet(AttentionCodelet ac){
//		long id = ac.getId();
//		executorService.submit(ac, id);
//		synchronized(this){
//			runningCodelets.put(id, ac);
//		}
//	}

	public void receiveFinishedTask(FutureTask<Object> finishedTask, Throwable t) {
		// TODO Auto-generated method stub
		
	}
	
	public void learn(){
		Collection<Node> nodes = broadcastContent.getNodes();
		for(Node n: nodes){
			//TODO:
			n.getId();
		}
	}//method

	public void stopRunning() {
		keepRunning = false;	
		stopSpawnedThreads();
	}

	public void stopSpawnedThreads() {
		executorService.shutdown();
		int size = runningCodelets.size();
		for(int i = 0; i < size; i++){			
			Runnable s = runningCodelets.get(i);
			if ((s != null)&&(s instanceof Stoppable)){
				((Stoppable)s).stopRunning();				
			}
		}//for
		System.out.println("all attention codelets told to stop");
	}//method

	public int getSpawnedThreadCount() {
		return runningCodelets.size();
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
	
}//class