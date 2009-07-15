package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

public interface CodeletResult {

	public void reportFinished();
	public boolean getCompletionStatus();
	public void setId(long id);
	public long getId();
	
}
