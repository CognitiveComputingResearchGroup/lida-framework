package edu.memphis.ccrg.lida.workspace.sbCodelets;

public interface StructureBuildingCodelet {

	public abstract void setActivation(double a);

	public abstract void setContext(CodeletObjective obj);

	public abstract void setCodeletAction(CodeletAction a);

	public abstract double getActivation();

	public abstract CodeletObjective getObjective();

	public abstract CodeletAction getCodeletAction();

}