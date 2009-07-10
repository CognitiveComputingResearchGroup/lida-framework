package edu.memphis.ccrg.lida.framework.gui;

import java.util.List;

public interface FrameworkGui {
	/**
	 * A Framework GUI may receive content from the following modules
	 * 
	 */
	public static final int FROM_PAM = -2;
	public static final int FROM_TEM = -1;
	public static final int FROM_DM = 0;
	public static final int FROM_PERCEPTUAL_BUFFER = 1;
	public static final int FROM_EPISODIC_BUFFER = 2;
	public static final int FROM_BROADCAST_QUEUE = 3;
	public static final int FROM_CSM = 4;
	public static final int FROM_SBCODELETS = 5;
	public static final int FROM_GLOBAL_WORKSPACE = 6;
	public static final int FROM_PROCEDURAL_MEMORY = 7;
	public static final int FROM_ACTION_SELECTION = 8;
	public static final int FROM_ENVIRONMENT = 9;
	
	
	/**
	 * A Framework GUI must receive content when it is sent.
	 * 
	 * @param lidaComponent - tells which module is sending the content
	 * @param content
	 */
	public abstract void receiveGuiContent(int lidaComponent, List<Object> guiContent);

}
