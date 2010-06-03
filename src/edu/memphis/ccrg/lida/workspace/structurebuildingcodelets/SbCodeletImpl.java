package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.HashSet;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Basic implementation of a structure-building codelet
 * @author Ryan J McCall
 *
 */
public class SbCodeletImpl extends LidaTaskImpl implements StructureBuildingCodelet{

	/**
	 * Set of workspace buffers this codelet 'looks at'
	 */
	private Set<WorkspaceBuffer> accessibleModules = new HashSet<WorkspaceBuffer>();
	
	private WorkspaceBuffer csm;
	
	/**
	 * The node structure that is requisite for this codelet's action 
	 */
	private NodeStructure soughtContent = new NodeStructureImpl();
	
	/**
	 * This codelet's action
	 */
	private CodeletAction action = new BasicCodeletAction();
	
	/**
	 * This codelet's type
	 */
	private int type = 0;
	
	/**
	 * Expected results of this codelets
	 */
	private CodeletResult results = new BasicCodeletResult();
	
	public SbCodeletImpl(WorkspaceBuffer csm, WorkspaceBuffer perceptualBuffer){
		super();
		this.csm = csm;
		accessibleModules.add(perceptualBuffer);
	}
                            
	protected void runThisLidaTask(){	
		System.out.println("Running codelet");
		for(WorkspaceBuffer buffer: accessibleModules){
//			System.out.println("Basic codelet performing action on buffer " + buffer.toString());
			action.performAction(buffer, csm);	
		}
		results.reportFinished();
	}
	
	/**
	 * Clears this codelet's fields in preparation for reuse. 
	 * Idea is that the same codelet object is reconfigured at runtime
	 * after it finishes to be run as a different altogether codelet. 
	 */
	public void reset() {
		accessibleModules.clear();
		setNumberOfTicksPerStep(50);
		setActivation(0.0);
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
		type = 0;		
	}
	
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	
	public void setCodeletAction(CodeletAction a){
		action = a;
	}	
	public CodeletAction getCodeletAction(){
		return action;
	}
	
	public void setResults(CodeletResult r){
		results = r;
	}
	public Object getResults(){
		return results;
	}

	public void addAccessibleModule(WorkspaceBuffer module) {
		accessibleModules.add(module);		
	}
	public Set<WorkspaceBuffer> getAccessibleModules() {
		return accessibleModules;
	}

	public void setType(int t){
		type = t;
	}
	public int getType() {
		return type;
	}

	/**
	 * Returns a String represetation of this codelet
	 */
	public String toString(){
		return "SBCodelet-"+ getTaskId();
	}
	
}//class 
