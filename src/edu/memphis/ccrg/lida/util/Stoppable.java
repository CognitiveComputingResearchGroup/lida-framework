package edu.memphis.ccrg.lida.util;

public interface Stoppable {
	
	public void stopRunning();
	public long getThreadID();
	public void setThreadID(long id);
//	public void registerToTimer();

}
