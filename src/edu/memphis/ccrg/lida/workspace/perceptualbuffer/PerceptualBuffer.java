package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferListener;

/**
 * PerceptualBuffer is not currently being used. Considered for removal.
 * @author ryanjmccall
 *
 */
public interface PerceptualBuffer extends WorkspaceBuffer{

	public abstract void addBufferListener(WorkspaceBufferListener workspace);

	public abstract void cueEpisodicMemory();

}
