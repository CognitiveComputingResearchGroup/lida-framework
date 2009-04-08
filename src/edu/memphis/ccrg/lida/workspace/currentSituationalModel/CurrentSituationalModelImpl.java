package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class CurrentSituationalModelImpl implements CurrentSituationalModel{
	
	private NodeStructure struct;
	private List<CSMListener> csmListeners = new ArrayList<CSMListener>();
	
	public CurrentSituationalModelImpl(){
		struct = new GraphImpl();
	}
	
	public void addCSMListener(CSMListener l){
		csmListeners.add(l);
	}
	
	public void sendCSMContent(){
		for(CSMListener l: csmListeners){
			l.receiveCSMContent(struct);
		}
	}//method
	
	public void addWorkspaceContent(WorkspaceContent content) {
		if(content != null){
			synchronized(this){
				struct = (GraphImpl)content.getContent();
			}
			
			Set<Node> nodes = struct.getNodes();
		}
	}//method

	public boolean hasContent(NodeStructure whatIwant) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public NodeStructure getContent(){
		return struct;
	}

}//class
