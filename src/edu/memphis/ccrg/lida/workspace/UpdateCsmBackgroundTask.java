package edu.memphis.ccrg.lida.workspace;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * A background task in the {@link Workspace} which transfers percepts from 
 * the Perceptual buffer to the Current Situational Model
 * @author Ryan J. McCall
 *
 */
public class UpdateCsmBackgroundTask extends LidaTaskImpl {

	private static final Logger logger = Logger
			.getLogger(UpdateCsmBackgroundTask.class.getCanonicalName());
	
	private Workspace workspace;

	@Override
	public void init() {
	}

	@Override
	public void setAssociatedModule(LidaModule module, String moduleUsage) {
		if (module instanceof Workspace) {
			workspace = (Workspace) module;
		}
	}

	/**
	 * Retrieves nodes from PAM and provides them to attentional codelets. This
	 * function gets PAM's nodes and provides them to CurrentSituationalModel,
	 * which will be accessed by attentional codelets.
	 */
	@Override
	protected void runThisLidaTask() {
		logger.log(Level.FINER, "Updating CSM with perceptual buffer content.", LidaTaskManager
				.getCurrentTick());
		
		WorkspaceBuffer percepBuff = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns = (NodeStructure) percepBuff.getBufferContent(null);
		WorkspaceBuffer csm = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.CurrentSituationalModel);
		((NodeStructure) csm.getBufferContent(null)).mergeWith(ns);
	}
	
	@Override
	public String toString() {
		return UpdateCsmBackgroundTask.class.getSimpleName();
	}

}
