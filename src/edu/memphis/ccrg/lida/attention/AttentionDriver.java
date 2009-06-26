package edu.memphis.ccrg.lida.attention;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionDriver implements Runnable, Stoppable, ThreadSpawner, BroadcastListener{

	private boolean keepRunning = true;
	private BroadcastContent broadcastContent = new NodeStructureImpl();
	//
	private FrameworkTimer timer;
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	//
	private double defaultActiv = 1.0;
	private NodeStructure defaultObjective = new NodeStructureImpl();	
    private ExecutorService execSvc = Executors.newCachedThreadPool();
	private List<Stoppable> codeletStoppables = new ArrayList<Stoppable>();
	
	public AttentionDriver(FrameworkTimer timer, CurrentSituationalModel csm, GlobalWorkspace gwksp){
		this.timer = timer;
		this.csm = csm;
		global = gwksp;
	}
	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = bc;
	}

	public void run() {
		spawnAttentionCodelet(defaultActiv, defaultObjective);
		
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}	
			timer.checkForStartPause();
			
		}//while
	}//method
	
	private void spawnAttentionCodelet(double activ, NodeStructure soughtContent){
		AttentionCodeletImpl ac = new AttentionCodeletImpl(csm, global, activ, soughtContent);
		codeletStoppables.add(ac);	
		execSvc.execute(ac);//put codelet in the work queue for the thread pool
	}//method

	public void stopRunning() {
		keepRunning = false;	
		stopSpawnedThreads();
	}

	public void stopSpawnedThreads() {
		System.out.println("Stopping Attention codelets");
		int size = codeletStoppables.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = codeletStoppables.get(i);
			if(s != null)
				s.stopRunning();					
		}//for
	}//method

	public int getSpawnedThreadCount() {
		return codeletStoppables.size();
	}
	
}//class