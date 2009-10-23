package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface CodeletWritable {
	
	public abstract void addCodeletContent(Module m, NodeStructure ns);
	//public abstract void  delete? 
}
