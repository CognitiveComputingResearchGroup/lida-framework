package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.shared.Activatible;

public interface StructBuildCodelet extends Activatible {
	
	public abstract void setContext(CodeletsDesiredContent obj);

	public abstract void setCodeletAction(CodeletAction a);

	public abstract CodeletsDesiredContent getObjective();

	public abstract CodeletAction getCodeletAction();

}