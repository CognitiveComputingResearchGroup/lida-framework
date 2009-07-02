package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface WorkspaceListener {
	
	public static final int FROM_PBUFFER = 1;
	public static final int FROM_EBUFFER = 2;
	public static final int FROM_BQUEUE = 3;
	
	public abstract void receiveWorkspaceContent(NodeStructure content, int originatingBuffer);

}
