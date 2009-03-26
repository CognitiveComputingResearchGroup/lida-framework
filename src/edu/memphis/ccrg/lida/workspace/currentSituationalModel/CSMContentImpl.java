package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public class CSMContentImpl implements CurrentSituationalModelContent {
	
	private NodeStructure struct;

	public CSMContentImpl(NodeStructure struct) {
		this.struct = struct;
	}

	public Object getContent() {
		return struct;
	}

}
