package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

public interface StructureBuildingCodelet {

	public abstract void setActivation(double a);

	public abstract void setContext(CodeletsDesiredContent obj);

	public abstract void setCodeletAction(CodeletAction a);

	public abstract double getActivation();

	public abstract CodeletsDesiredContent getObjective();

	public abstract CodeletAction getCodeletAction();

}