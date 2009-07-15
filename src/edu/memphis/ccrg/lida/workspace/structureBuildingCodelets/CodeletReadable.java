package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface CodeletReadable {

	public static final int CSM = 0;
	public static final int PERCEPTUAL_BUFFER = 1;
	public static final int EPISODIC_BUFFER = 2;
	public static final int BROADCAST_QUEUE = 3;
	
	public abstract List<NodeStructure> getBuffer(int buffer);

}
