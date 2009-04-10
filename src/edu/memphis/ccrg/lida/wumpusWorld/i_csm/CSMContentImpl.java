package edu.memphis.ccrg.lida.wumpusWorld.i_csm;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelContent;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;

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
