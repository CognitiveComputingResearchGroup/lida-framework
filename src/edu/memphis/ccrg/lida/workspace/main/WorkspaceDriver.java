/**
 * 
 */
package edu.memphis.ccrg.lida.workspace.main;

import java.util.Map;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * @author Javier Snaider
 * 
 */
public class WorkspaceDriver extends ModuleDriverImpl {

	private static final double DEFAULT_ACT_THRESHOLD = 0.4;
	private Workspace workspace;
	private double actThreshold;
	private int cueLap;
	private int cueLapCounter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.ModuleDriverImpl#runThisDriver()
	 */
	@Override
	protected void runThisDriver() {
		cue();
		moveToCSM();

	}

	private void moveToCSM() {
		WorkspaceBuffer percepBuff = (WorkspaceBuffer) workspace
		.getSubmodule(ModuleName.PerceptualBuffer);		
		NodeStructure ns = (NodeStructure) percepBuff.getModuleContent();
		WorkspaceBuffer csm = (WorkspaceBuffer) workspace
		.getSubmodule(ModuleName.CurrentSituationalModel);		
		((NodeStructure)csm.getModuleContent()).mergeWith(ns);
	}

	private void cue() {
		if (cueLapCounter++ >= cueLap) {
			cueLapCounter = 0;
			WorkspaceBuffer percepBuff = (WorkspaceBuffer) workspace
					.getSubmodule(ModuleName.PerceptualBuffer);
			NodeStructure ns = (NodeStructure) percepBuff.getModuleContent();
			NodeStructure cueNs = new NodeStructureImpl();

			for (Node n : ns.getNodes()) {
				if (n.getActivation() >= actThreshold) {
					cueNs.addNode(n);
				}
			}
			if (cueNs.getNodeCount() > 0) {
				workspace.cue(cueNs);
			}
		}
	}

	@Override
	public String toString() {
		return ModuleName.WorkspaceDriver + "";
	}

	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				workspace = (Workspace) module;
			}
		}
	}

	@Override
	public void init(Map<String, ?> params) {
		this.lidaProperties = params;
		actThreshold = (Double) getParam("workspace.actThreshold",DEFAULT_ACT_THRESHOLD);
		cueLap = (Integer)getParam("workspace.cueLap",1);
	}

}
