package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public interface Workspace{
	
	public static final int PBUFFER = 0;
	public static final int EBUFFER = 1;
	public static final int PBROADS = 2;
	public static final int CSM = 3;
	CodeletReadable getModuleReference(int moduleID);
	void addContentToCSM(WorkspaceContent updatedContent);
	
}
