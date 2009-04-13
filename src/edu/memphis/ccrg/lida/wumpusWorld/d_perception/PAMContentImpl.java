package edu.memphis.ccrg.lida.wumpusWorld.d_perception;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class PAMContentImpl implements WorkspaceContent{

	private NodeStructure graph = new NodeStructureRyan();

	public Object getContent(){
		return graph;
	}

	public synchronized void setContent(NodeStructure struct) {
		graph = struct;			
	}//method
	
	public boolean isEmpty(){
		if(graph == null){
			return true;
		}else{
			return graph.getNodes().size() == 0;
		}
	}//isEmpty()

}
