package edu.memphis.ccrg.lida.gui;

import java.util.List;

public interface FrameworkGui {
	
	
	
	
	public static final int PERCEPT_IN_PAM = 0;
	public static final int PERCEPTUAL_BUFFER = 1;
	public static final int CSM = 2;
	public static final int SB_CODELETS = 3;
	public static final int GLOBAL_WORKSPACE = 4;
	public static final int PROCEDURAL_MEMORY = 5;

	
	public abstract void receiveGuiContent(int lidaComponent, List<Object> content);

}
