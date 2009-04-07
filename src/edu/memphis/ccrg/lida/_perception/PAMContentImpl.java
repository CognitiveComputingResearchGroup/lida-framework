package edu.memphis.ccrg.lida._perception;

import edu.memphis.ccrg.lida._perception.interfaces.PAMContent;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class PAMContentImpl implements PAMContent{

	private NodeStructure graph = new GraphImpl();

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
