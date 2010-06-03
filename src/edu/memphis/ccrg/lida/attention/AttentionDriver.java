package edu.memphis.ccrg.lida.attention;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SbCodeletImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.StructureBuildingCodelet;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
 
public class AttentionDriver extends ModuleDriverImpl implements
		BroadcastListener {

	private Logger logger = Logger.getLogger("lida.attention.AttentionDriver");
	
	private WorkspaceBuffer csm;
	private GlobalWorkspace global;
	//
	private double defaultActiv = 1.0;// TODO: move these to factory?
	private NodeStructure broadcastContent;
	private static final int DEFAULT_TICKS_PER_STEP = 10;

	public AttentionDriver(WorkspaceBuffer csm, GlobalWorkspace gwksp,
			int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm, ModuleName.AttentionDriver);
		this.csm = csm;
		global = gwksp;
	}

	public AttentionDriver() {
		super(DEFAULT_TICKS_PER_STEP, ModuleName.AttentionDriver);
	}

	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	@Override
	public void runThisDriver(){
		//activateCodelets();
	}

	public void activateCodelets() {
		// //For testing only!!!!!!
		// if (getSpawnedTaskCount() < 10) {
		// addTask(new
		// AttentionCodeletImpl(csm,global,defaultTicksPerStep,defaultActiv
		// ,getTaskManager() ,new NodeStructureImpl()));
		// }
	}

	@Override
	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			// Implement learning here
			n.getId();
		}
	}// method


	@Override
	public void init(Map<String,?> params) {
		lidaProperties=params;
		int ticksperstep = 0;
		ticksperstep = (Integer)getParam("AttetionSelection.ticksperstep",DEFAULT_TICKS_PER_STEP);
		setNumberOfTicksPerStep(ticksperstep);
		defaultActiv=(Double)getParam("AttetionSelection.defaultActiv",1.0);
	}

	@Override
	public String toString() {
		return ModuleName.AttentionDriver + "";
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				csm = (WorkspaceBuffer) module.getSubmodule(ModuleName.CurrentSituationalModel);
			} else if (module instanceof GlobalWorkspace
					&& module.getModuleName() == ModuleName.GlobalWorkspace) {
				global = (GlobalWorkspace) module;
			}
		}
	}

	public void spawnNewCodelet() {
		//TODO: use factory
		
		//TODO: move tps var out
		int ticksPerStep = 5;
		double activation = 1.0;
		NodeStructure ns = new NodeStructureImpl();
		
		AttentionCodelet basic = new AttentionCodeletImpl(this.csm, this.global, ticksPerStep, activation, ns);
		
		//sbCodeletFactory.getCodelet(type, activation, context, actions);
		this.addTask(basic);
		logger.log(Level.FINER,"New attention codelet "+basic+"spawned",LidaTaskManager.getActualTick());
	}// method
		
}// class