package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class CSMContentImpl implements CurrentSituationalModelContent {
	
	private NodeStructure struct;

	public CSMContentImpl(NodeStructure struct) {
		this.struct = struct;
	}

	public CSMContentImpl() {
		struct = new GraphImpl();
	}

	public Object getContent() {
		return struct;
	}

}
