package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

public class BasicCodeletResult implements CodeletResult {
	
	private boolean finishedRunningNormally = false;
	private long id = 0;
	
	public void reportFinished(){
		finishedRunningNormally = true;
	}
	public boolean getCompletionStatus(){
		return finishedRunningNormally;
	}
	
	public void setId(long id){
		this.id = id;
	}
	public long getId(){
		return id;
	}
	

}
