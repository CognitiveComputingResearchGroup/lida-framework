package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * This interface defines how codelets can access data from Workspace sub modules.
 * Modules that need to be accessible to codelets should implement this interface.
 * 
 * @author ryanjmccall
 *
 */
public interface CodeletAccessible {

	/**
	 * @return NodeStructure representation of the buffer contents.
	 */
	public abstract NodeStructure getBufferContent(Module m);
	
	/**
	 * @return A collection of NodeStructures comprising the contents of the buffer
	 */
	public abstract Collection<NodeStructure> getContentCollection(Module m);
}
