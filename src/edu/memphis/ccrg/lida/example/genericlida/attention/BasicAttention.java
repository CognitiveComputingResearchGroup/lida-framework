package edu.memphis.ccrg.lida.example.genericlida.attention;

import edu.memphis.ccrg.lida.attention.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class BasicAttention extends AttentionCodeletImpl {

	public BasicAttention(WorkspaceBuffer csm, GlobalWorkspace g,
			int ticksPerStep, double activation, NodeStructure soughtContent) {
		super(csm, g, ticksPerStep, activation, soughtContent);
		// TODO Auto-generated constructor stub
	}

	private static final double THRESHOLD = 0.5;

	protected void runThisLidaTask() {
		NodeStructure ns = new NodeStructureImpl();
		for (Node n : ((NodeStructure) csm.getModuleContent()).getNodes()) {
			if (n.getActivation()>=THRESHOLD){
				ns.addNode(n);
			}
		}
		if (ns != null){
			global.addCoalition(new CoalitionImpl(ns,
					getActivation()));
		}
	}
}
