package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.shared.Activatible;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface StructureBuildingCodelet extends Activatible, Runnable, Stoppable {
	
	public abstract void setSoughtContent(NodeStructure content);

	public abstract void setCodeletAction(CodeletAction a);

	public abstract NodeStructure getSoughtContent();

	public abstract CodeletAction getCodeletAction();

}