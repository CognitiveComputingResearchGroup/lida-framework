/**
 * 
 */
package edu.memphis.ccrg.lida.workspace;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Task which operates workspace. This class provides a general way to control
 * various type of workspace. And it mainly responds for transferring content of
 * nodes coming from PAM to episodic memory and attentional codelets.
 * 
 * @author Javier Snaider
 * 
 */
public class CueBackgroundTask extends FrameworkTaskImpl {

	private static final double DEFAULT_ACT_THRESHOLD = 0.4;
	private double actThreshold = DEFAULT_ACT_THRESHOLD;
	private Workspace workspace;
	// TODO Remove this parameter. Too much. If the pbuffer decays fast enough
	// it should be relatively small?
	private static final Logger logger = Logger
			.getLogger(CueBackgroundTask.class.getCanonicalName());

	@Override
	public void init() {
		actThreshold = (Double) getParam("workspace.actThreshold",
				DEFAULT_ACT_THRESHOLD);
	}

	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		if (module instanceof Workspace) {
			workspace = (Workspace) module;
		}
	}

	/**
	 * Retrieves nodes from PAM and provides them to episodic memory. This
	 * function checks PAM's nodes number, and if there are at least 1 node in
	 * PAM, then provides them to episodic listener.
	 */
	@Override
	protected void runThisFrameworkTask() {
		WorkspaceBuffer perceptualBuffer = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns = (NodeStructure) perceptualBuffer.getBufferContent(null);

		NodeStructure cueNodeStrucutre = new NodeStructureImpl();
		// Current impl. of episodic memory only processes Nodes.
		// TODO add links when episodic memory supports them
		if(ns != null){
			for (Node n : ns.getNodes()) {
				if (n.getActivation() >= actThreshold) {
					cueNodeStrucutre.addDefaultNode(n);
				}
			}
		}else{
			logger.log(Level.WARNING, "Got a null nodestructure", TaskManager
					.getCurrentTick());
		}
		
		if (cueNodeStrucutre.getNodeCount() > 0) {
			logger.log(Level.FINER, "Cueing Episodic Memories", TaskManager
					.getCurrentTick());
			workspace.cueEpisodicMemories(cueNodeStrucutre);
		}
	}

	@Override
	public String toString() {
		return CueBackgroundTask.class.getSimpleName();
	}
}
