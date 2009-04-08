package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

public interface StructureBuildingCodelet {

	public abstract void setActivation(double a);

	public abstract void setContext(CodeletContext obj);

	public abstract void setCodeletAction(CodeletAction a);

	public abstract double getActivation();

	public abstract CodeletContext getObjective();

	public abstract CodeletAction getCodeletAction();

}