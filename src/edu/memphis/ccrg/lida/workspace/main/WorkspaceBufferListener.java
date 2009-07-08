package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface WorkspaceBufferListener {
	
	public static final int PBUFFER = 0;
	public static final int EBUFFER = 1;
	public static final int BQUEUE = 2;
	public static final int CSM = 3;
	
	public abstract void receiveBufferContent(int buffer, NodeStructure ns);

}
