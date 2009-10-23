package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

/**
 * An encapsulation of the data that is produced from a run of a codelet.  
 * @author ryanjmccall
 *
 */
public interface CodeletResult {

	public void reportFinished();
	public boolean getCompletionStatus();
	public void setId(long id);
	public long getId();
	
}
