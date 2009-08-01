package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class SBCodeletImpl extends LidaTaskImpl implements StructureBuildingCodelet{

	//Initialized by constructor
	private LidaTaskManager timer;
	
	private List<List<NodeStructure>> accessibleBuffers = new ArrayList<List<NodeStructure>>();
	private List<CodeletWritable> writables = new ArrayList<CodeletWritable>();
	
	private NodeStructure soughtContent;
	private CodeletAction action;
	//
	private int type;
	private CodeletResult results = new BasicCodeletResult();
	
	public void addFrameworkTimer(LidaTaskManager timer) {
		this.timer = timer;
	}	

	public Object getResult(){
		return results;
	}
	
	public void run() {
		timer.checkForStartPause();
		 	
		for(List<NodeStructure> buffer: accessibleBuffers)
			for(CodeletWritable writable: writables)
				action.performAction(buffer, writable);	

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

	public void addReadableBuffer(List<NodeStructure> buffer) {
		accessibleBuffers.add(buffer);		
	}
	public void addWritableModule(CodeletWritable module) {
		writables.add(module);
	}
	public List<List<NodeStructure>> getReadableBuffers() {
		return accessibleBuffers;
	}
	public List<CodeletWritable> getWriteableBuffers() {
		return writables;
	}
	//
	public void reset() {
		accessibleBuffers.clear();
		writables.clear();
		setNumberOfTicksPerStep(50);
		setActivation(0.0);
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
		type = 0;		
	}
	public int getType() {
		return type;
	}
	
	/**
	 * This method compares this object with any kind of Node. returns true if
	 * the id of both are the same.
	 */
//	public boolean equals(Object o) {
//		if (!(o instanceof StructureBuildingCodelet)) {
//			return false;
//		}
//		StructureBuildingCodelet temp = (StructureBuildingCodelet) o;
//		return temp.getId() == id && temp.getType() == type;
//	}
//
//	public int hashCode() {
//		int hash = 1;
//	    Integer v1 = new Integer(type);
//	    Long v2 = new Long(id);
//	    hash = hash * 31 + v2.hashCode();
//	    hash = hash * 31 + (v1 == null ? 0 : v1.hashCode());
//	    return hash;
//	}

	
}//class SBCodelet
