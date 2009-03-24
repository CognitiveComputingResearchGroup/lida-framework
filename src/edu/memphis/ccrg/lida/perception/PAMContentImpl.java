package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.perception.interfaces.PAMContent;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class PAMContentImpl implements PAMContent{

	private NodeStructure graph = null;

	public Object getContent(){
		return graph;
	}

	public void setContent(NodeStructure struct) {
		graph = struct;		
	}
	
	public boolean isEmpty(){
		if(graph == null){
			return true;
		}else{
			return graph.getNodes().size() > 0;
		}
	}

}
