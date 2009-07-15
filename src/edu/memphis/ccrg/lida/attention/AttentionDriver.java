package edu.memphis.ccrg.lida.attention;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import edu.memphis.ccrg.lida.framework.FrameworkExecutorService;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.BroadcastLearner;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionDriver extends GenericModuleDriver implements ThreadSpawner, BroadcastListener, BroadcastLearner{

	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	private NodeStructure broadcastContent = new NodeStructureImpl();
	private Map<Long, Stoppable> runningCodelets = new HashMap<Long, Stoppable>();
	private ExecutorService executorService;
	//
	private double defaultActiv = 1.0;//TODO: move these to factory?
	private NodeStructure defaultObjective = new NodeStructureImpl();	
	
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
	    											   TimeUnit.SECONDS, taskQueue);
	}
	
	public void startInitialRunnables(List<Runnable> initialRunnables) {
		for(Runnable r: initialRunnables){
			if(r instanceof AttentionCodelet)
				executeCodelet((AttentionCodelet) r);
			else
				System.out.println("In AttentionCodeletDriver, a noncodelet object was submitted for execution");
		}		
	}

	public void startInitialCallables(List<Callable<Object>> initialCallables) {
		for (Callable<Object> c : initialCallables){
			if(c instanceof AttentionCodelet)
				executeCodelet((AttentionCodelet) c);
			else
				System.out.println("In AttentionCodeletDriver, a noncodelet object was submitted for execution");
		}
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
	
	@SuppressWarnings("unused")
	private void spawnAttentionCodelet(double activ, NodeStructure soughtContent){
		AttentionCodeletImpl ac = new AttentionCodeletImpl(csm, global, activ);
		executeCodelet(ac);
	}//method
	
	private void executeCodelet(AttentionCodelet ac){
		long id = ac.getId();
		executorService.submit(ac, id);
		synchronized(this){
			runningCodelets.put(id, ac);
		}
	}

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
		int size = runningCodelets.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = runningCodelets.get(i);
			if(s != null)
				s.stopRunning();					
		}//for
		executorService.shutdownNow();
		System.out.println("all attention codelets told to stop");
	}//method

	public int getSpawnedThreadCount() {
		return runningCodelets.size();
	}
	
}//class