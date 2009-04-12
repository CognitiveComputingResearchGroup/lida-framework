package edu.memphis.ccrg.lida.wumpusWorld.i_csm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMListener;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;

public class CurrentSituationalModelImpl implements CurrentSituationalModel{
	
	private NodeStructure struct = new GraphImpl();
	private List<CSMListener> csmListeners = new ArrayList<CSMListener>();
	
	public CurrentSituationalModelImpl(){
		struct = new GraphImpl();
	}
	
	public void addCSMListener(CSMListener l){
		csmListeners.add(l);
	}
	
	public void sendCSMContent(){
		for(CSMListener l: csmListeners)
			l.receiveCSMContent(struct);
	}//method
	
	public synchronized void addWorkspaceContent(WorkspaceContent content) {
		struct = (NodeStructure)content;
	}//method

	public boolean hasContent(NodeStructure whatIwant) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public NodeStructure getContent(){
		return struct;
	}

	public WorkspaceContent getCodeletsObjective(CodeletsDesiredContent objective) {
		// TODO Auto-generated method stub
		return null;
	}

}//class
