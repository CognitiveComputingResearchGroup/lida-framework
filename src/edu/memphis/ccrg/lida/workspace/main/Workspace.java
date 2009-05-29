package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;

public interface Workspace{
	
	//REMOVE THESE
	public static final int PBUFFER = 0;
	public static final int EBUFFER = 1;
	public static final int PBROADS = 2;
	public static final int CSM = 3;
	public WorkspaceContent getCodeletDesiredContent(int moduleID, CodeletsDesiredContent soughtContent); 
	void addContentToCSM(WorkspaceContent updatedContent);
	
}
