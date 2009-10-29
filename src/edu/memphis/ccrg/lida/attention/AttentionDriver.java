package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class AttentionDriver extends ModuleDriverImpl implements BroadcastListener {

	//private Logger logger = Logger.getLogger("lida.attention.AttentionDriver");
	//
	private WorkspaceBuffer csm;
	private GlobalWorkspace global;
	//
	private double defaultActiv = 1.0;// TODO: move these to factory?
	private NodeStructure broadcastContent;
	private int defaultTicksPerStep = 3;

	public AttentionDriver(WorkspaceBuffer csm, GlobalWorkspace gwksp, 
						   int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		this.csm = csm;
		global = gwksp;
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public void runThisDriver() {
		activateCodelets();
	}

	public void activateCodelets() {
//		//For testing only!!!!!!
//		if (getSpawnedTaskCount() < 10) {
//			addTask(new AttentionCodeletImpl(csm,global,defaultTicksPerStep,defaultActiv,getTaskManager() ,new NodeStructureImpl()));
//		}
	}

	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			// TODO:
			n.getId();
		}
	}// method

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return ModuleType.AttentionDriver +"";
	}

}// class