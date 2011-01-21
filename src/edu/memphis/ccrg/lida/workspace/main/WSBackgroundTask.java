/**
 * 
 */
package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * @author Javier Snaider
 * 
 */
public class WSBackgroundTask extends LidaTaskImpl {

	private static final double DEFAULT_ACT_THRESHOLD = 0.4;
	private Workspace workspace;
	//TODO Remove this parameter.  Too much.
	private double actThreshold;
	private int cueFrequency;
	private int cueFrequencyCounter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl#runThisLidaTask()
	 */
	@Override
	protected void runThisLidaTask() {
		cue();
		moveToCSM();
	}

	private void moveToCSM() {
		WorkspaceBuffer percepBuff = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns = (NodeStructure) percepBuff.getModuleContent();
		WorkspaceBuffer csm = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.CurrentSituationalModel);
		((NodeStructure) csm.getModuleContent()).mergeWith(ns);
	}

	private void cue() {
		if (cueFrequencyCounter++ >= cueFrequency) {
			cueFrequencyCounter = 0;
			WorkspaceBuffer perceptualBuffer = (WorkspaceBuffer) workspace
					.getSubmodule(ModuleName.PerceptualBuffer);
			NodeStructure ns = (NodeStructure) perceptualBuffer.getModuleContent();
			NodeStructure cueNodeStrucutre = new NodeStructureImpl();

			//TODO What about adding the links?
			for (Node n : ns.getNodes()) {
				if (n.getActivation() >= actThreshold) {
					cueNodeStrucutre.addNode(n);
				}
			}
			if (cueNodeStrucutre.getNodeCount() > 0) {
				workspace.cueEpisodicMemories(cueNodeStrucutre);
			}
		}
	}

	@Override
	public void init() {
		actThreshold = (Double) getParam("workspace.actThreshold",
				DEFAULT_ACT_THRESHOLD);
		cueFrequency = (Integer) getParam("workspace.cueFrequency", 1);
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module instanceof Workspace
				&& module.getModuleName() == ModuleName.Workspace) {
			workspace = (Workspace) module;
		}
	}
	
	public String toString(){
		return WSBackgroundTask.class.getSimpleName();
	}
}
