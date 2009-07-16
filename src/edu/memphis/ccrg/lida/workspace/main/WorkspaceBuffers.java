package edu.memphis.ccrg.lida.workspace.main;

import java.util.List;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface WorkspaceBuffers {

	public abstract void addPerceptualBufferListener(WorkspaceBufferListener l);

	public abstract void addEpisodicBufferListener(WorkspaceBufferListener l);

	public abstract List<NodeStructure> getBroadcastQueue();

	public abstract List<NodeStructure> getEpisodicBuffer();

	public abstract List<NodeStructure> getPerceptualBuffer();

}