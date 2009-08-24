package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * A WorkspaceBuffer listener must receive buffer content.
 * The source of the content is coded using an int.  
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBufferListener {

	public abstract void receiveBufferContent(Module originatingBuffer, NodeStructure ns);

}
