package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, CodeletReadable{
	
	private NodeStructureImpl struct = new NodeStructureImpl();
	private List<CSMListener> csmListeners = new ArrayList<CSMListener>();
	
	public CurrentSituationalModelImpl(){
		struct = new NodeStructureImpl();
	}
	
	public void addCSMListener(CSMListener l){
		csmListeners.add(l);
	}
	
	public void sendCSMContent(){
		for(CSMListener l: csmListeners)
			l.receiveCSMContent(struct);
	}//method
	
	public synchronized void addWorkspaceContent(WorkspaceContent content) {
		struct = (NodeStructureImpl)content;	
		//TODO Don't just overwrite the previous contents?
	}//method

	public boolean hasContent(NodeStructure whatIwant) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public NodeStructure getContent(){
		return struct;
	}

	public WorkspaceContent lookForContent(NodeStructure objective) {
		// TODO Auto-generated method stub
		return null;
	}

}//class
