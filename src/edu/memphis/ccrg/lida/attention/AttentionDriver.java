package edu.memphis.ccrg.lida.attention;

import java.util.Collection;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.BroadcastLearner;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class AttentionDriver extends ModuleDriverImpl implements TaskSpawner, BroadcastListener, BroadcastLearner{

	private Logger logger=Logger.getLogger("lida.attention.AttentionDriver");
	//
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	//
	private double defaultActiv = 1.0;//TODO: move these to factory?
	private NodeStructure broadcastContent;
	
	public AttentionDriver(LidaTaskManager timer, CurrentSituationalModel csm, GlobalWorkspace gwksp){
		super(timer);
		this.csm = csm;
		global = gwksp;
	}
	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public void cycleStep() {
		activateCodelets();
		//learn();
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

	public void receiveFinishedTask(LidaTask finishedTask, Throwable t) {
		super.receiveFinishedTask(finishedTask, t);
		// TODO Auto-generated method stub		
	}
	
	public void learn(){
		Collection<Node> nodes = broadcastContent.getNodes();
		for(Node n: nodes){
			//TODO:
			n.getId();
		}
	}//method


}//class