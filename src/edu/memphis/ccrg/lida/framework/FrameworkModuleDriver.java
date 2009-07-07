package edu.memphis.ccrg.lida.framework;

/**
 * A FrameworkModuleDriver is a class that runs a major module of the 
 * framework such as PAM or Procedural Memory
 * 
 * @author ryanjmccall
 *
 */
public interface FrameworkModuleDriver extends Runnable{
	
	/**
	 * Halt the operation of the Driver
	 */
	public abstract void stopRunning();

}
