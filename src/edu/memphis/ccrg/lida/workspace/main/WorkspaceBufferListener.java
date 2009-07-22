package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.shared.NodeStructure;

/**
 * A WorkspaceBuffer listener must receive buffer content.
 * The source of the content is coded using an int.  
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBufferListener {
	
	public static final int PBUFFER = 0;
	public static final int EBUFFER = 1;
	public static final int CSM = 2;
	
	public abstract void receiveBufferContent(int buffer, NodeStructure ns);

}
