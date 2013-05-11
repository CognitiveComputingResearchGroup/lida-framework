package edu.memphis.ccrg.lida.workspace.workspacebuffers;

import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.workspace.Workspace;

/**
 * A {@link WorkspaceBuffer} storing the recent contents of consciousness. It is
 * a submodule of the {@link Workspace}.
 * 
 * @author Ryan J. McCall
 * @author Javier Snaider
 * 
 */
public interface BroadcastQueue extends WorkspaceBuffer, BroadcastListener {

	/**
	 * Returns content of specified position
	 * 
	 * @param index
	 *            position in the queue
	 * @return {@link CognitiveContentStructure} at index position or null
	 */
	public CognitiveContentStructure getPositionContent(int index);

}
