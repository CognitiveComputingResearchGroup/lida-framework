package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.HashSet;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class SbCodeletImpl extends LidaTaskImpl implements StructureBuildingCodelet{

	//Initialized by constructor
	private Set<CodeletAccessible> accessibleModules = new HashSet<CodeletAccessible>();
	
	private NodeStructure soughtContent;
	private CodeletAction action;
	//
	private int type;
	private CodeletResult results = new BasicCodeletResult();
	
	public SbCodeletImpl(LidaTaskManager tm){
		super(tm);
	}

	public Object getResult(){
		return results;
	}

	protected void runThisLidaTask(){	
		for(CodeletAccessible buffer: accessibleModules)
			action.performAction(buffer);	
		results.reportFinished();
	}
	
	//**** StructureBuildingCodelet methods ****
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	//
	public void setCodeletAction(CodeletAction a){
		action = a;
	}	
	public CodeletAction getCodeletAction(){
		return action;
	}

	public void addAccessibleModule(CodeletAccessible module) {
		accessibleModules.add(module);		
	}
	public Set<CodeletAccessible> getAccessibleModules() {
		return accessibleModules;
	}

	public void reset() {
		accessibleModules.clear();
		setNumberOfTicksPerStep(50);
		setActivation(0.0);
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
		type = 0;		
	}
	public int getType() {
		return type;
	}

	public String toString(){
		return "SBCodelet-"+ getTaskId();
	}
	
}//class SBCodelet
