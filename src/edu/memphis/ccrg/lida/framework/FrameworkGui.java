package edu.memphis.ccrg.lida.framework;

import java.util.List;

public interface FrameworkGui {
	/**
	 * A Framework GUI may receive content from the following modules
	 * 
	 */
	public static final int FROM_PAM = 0;
	public static final int FROM_PERCEPTUAL_BUFFER = 1;
	public static final int FROM_CSM = 2;
	public static final int FROM_SB_CODELETS = 3;
	public static final int FROM_GLOBAL_WORKSPACE = 4;
	public static final int FROM_PROCEDURAL_MEMORY = 5;
	
	/**
	 * A Framework GUI must receive content when it is sent.
	 * 
	 * @param lidaComponent - tells which module is sending the content
	 * @param content
	 */
	public abstract void receiveGuiContent(int lidaComponent, List<Object> content);

}
