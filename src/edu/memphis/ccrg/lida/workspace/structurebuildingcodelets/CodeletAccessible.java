package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface CodeletAccessible {

	public abstract NodeStructure getBufferContent();
	public abstract Collection<NodeStructure> getContentCollection();
}
