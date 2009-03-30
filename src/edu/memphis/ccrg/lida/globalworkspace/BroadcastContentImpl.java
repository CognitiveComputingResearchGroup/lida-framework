package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class BroadcastContentImpl implements BroadcastContent{
	
	private NodeStructure struct;
	
	public BroadcastContentImpl(NodeStructure struct){
		this.struct = struct;
	}
	
	public BroadcastContentImpl() {
		struct = new GraphImpl();
	}

	public void setContent(NodeStructure struct){
		this.struct = struct;
	}

	public Object getContent() {
		return struct;
	}

}
