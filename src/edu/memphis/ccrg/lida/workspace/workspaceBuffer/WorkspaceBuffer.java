package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * This interface defines how codelets can access data from Workspace sub modules.
 * Modules that need to be accessible to codelets should implement this interface.
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBuffer {

	/**
	 * @return NodeStructure representation of the buffer contents.
	 */
	public abstract NodeStructure getModuleContent();
}
