package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.shared.NodeStructureImp;

public class BroadcastContentImpl implements BroadcastContent{
	
	private Object struct;
	
	public BroadcastContentImpl(Object struct){
		this.struct = struct;
	}
	
	public BroadcastContentImpl() {
		struct = new NodeStructureImp();
	}

	public void setContent(Object struct){
		this.struct = struct;
	}

	public Object getContent() {
		return struct;
	}

}
