package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.shared.Activatible;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface StructBuildCodelet extends Activatible {
	
	public abstract void setContext(NodeStructure obj);

	public abstract void setCodeletAction(CodeletAction a);

	public abstract NodeStructure getObjective();

	public abstract CodeletAction getCodeletAction();

}