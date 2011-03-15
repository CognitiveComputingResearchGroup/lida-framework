/**
 * 
 */
package edu.memphis.ccrg.lida.workspace;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Task which operates workspace. 
 * This class provides a general way to control various type of workspace.
 * And it mainly responds for transferring content of nodes coming from PAM 
 * to episodic memory and attentional codelets.
 * 
 * TODO I don't like how it moves to CSM AND cues.  I prefer one task for each.
 * @author Javier Snaider
 * 
 */
public class WSBackgroundTask extends LidaTaskImpl {

	private static final double DEFAULT_ACT_THRESHOLD = 0.4;
	private double actThreshold = DEFAULT_ACT_THRESHOLD;
	private Workspace workspace;
	//TODO Remove this parameter.  Too much.  If the pbuffer decays fast enough it should be relatively small?
	private int cueFrequency;
	private int cueFrequencyCounter;

	private static final Logger logger = Logger.getLogger(WSBackgroundTask.class.getCanonicalName());
	
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

	/**
	 * Retrieves nodes from PAM and provides them to attentional codelets.
	 * This function gets PAM's nodes and provides them to CurrentSituationalModel,
	 * which will be accessed by attentional codelets.
	 */
	private void moveToCSM() {
		WorkspaceBuffer percepBuff = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns = (NodeStructure) percepBuff.getModuleContent();
		WorkspaceBuffer csm = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.CurrentSituationalModel);
		((NodeStructure) csm.getModuleContent()).mergeWith(ns);
	}

	/**
	 * Retrieves nodes from PAM and provides them to episodic memory. 
	 * This function checks PAM's nodes number, and if there are at least 1 node in 
	 * PAM, then provides them to episodic listener.  
	 */
	private void cue() {
		
		//IF we separate tasks we can remove this parameter and use ticksPerRun
		if (cueFrequencyCounter++ >= cueFrequency) {
			cueFrequencyCounter = 0;
			WorkspaceBuffer perceptualBuffer = (WorkspaceBuffer) workspace
					.getSubmodule(ModuleName.PerceptualBuffer);
			NodeStructure ns = (NodeStructure) perceptualBuffer.getModuleContent();
			
			NodeStructure cueNodeStrucutre = new NodeStructureImpl();
			//TODO What about adding the links?
			for (Node n : ns.getNodes()) {
				if (n.getActivation() >= actThreshold) {
					cueNodeStrucutre.addDefaultNode(n);
				}
			}
			if (cueNodeStrucutre.getNodeCount() > 0) {
				logger.log(Level.FINER, "Cueing Episodic Memories", LidaTaskManager.getCurrentTick());
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
		if (module instanceof Workspace) {
			workspace = (Workspace) module;
		}
	}
	
	@Override
	public String toString(){
		return WSBackgroundTask.class.getSimpleName();
	}
}
