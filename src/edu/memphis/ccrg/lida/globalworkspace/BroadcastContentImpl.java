package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;

public class BroadcastContentImpl implements BroadcastContent{
	
	private Object struct;
	
	public BroadcastContentImpl(Object struct){
		this.struct = struct;
	}
	
	public BroadcastContentImpl() {
		struct = new GraphImpl();
	}

	public void setContent(Object struct){
		this.struct = struct;
	}

	public Object getContent() {
		return struct;
	}

}
