package edu.memphis.ccrg.lida.framework;

/**
 * A ModuleDriver is a class that runs a major module of the 
 * framework such as PAM or Procedural Memory
 * 
 * @author ryanjmccall
 *
 */
public interface ModuleDriver extends Stoppable{

	public void setThreadID(long id);
	public long getThreadID();
}
