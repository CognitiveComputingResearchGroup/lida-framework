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
//		if(graph != null){
//			Set<Node> nodes = graph.getNodes();
//			if(nodes != null)
//				System.out.println(nodes.size() + " nodes in PAM content (the percept)");
//		}
			
	}
	
	public boolean isEmpty(){
		if(graph == null){
			return true;
		}else{
			return graph.getNodes().size() > 0;
		}
	}

}
