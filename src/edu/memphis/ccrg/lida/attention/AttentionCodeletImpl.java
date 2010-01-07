package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class AttentionCodeletImpl extends LidaTaskImpl implements
		AttentionCodelet {
	
	private WorkspaceBuffer csm; 
	private GlobalWorkspace global;
	private NodeStructure soughtContent;

    public AttentionCodeletImpl(WorkspaceBuffer csm, GlobalWorkspace g, int ticksPerStep,
    							double activation,LidaTaskManager tm, NodeStructure soughtContent){
    	super(ticksPerStep);
    	setActivation(activation);
    	this.csm = csm;
    	global = g;
    	this.soughtContent=soughtContent;
    	
    	//this.timer=timer;
    }

	protected void runThisLidaTask() {
		if (hasSoughtContent(csm)) {
			NodeStructure csmContent = getSoughtContent(csm);
			if (csmContent != null)
				global.addCoalition(new CoalitionImpl(csmContent,
						getActivation()));
		}// if
	}

	public boolean hasSoughtContent(WorkspaceBuffer csm) {
		NodeStructure model = (NodeStructure)csm.getModuleContent();
		Collection<Node> nodes = soughtContent.getNodes();
		Collection<Link> links = soughtContent.getLinks();
		for (Node n : nodes)
			if (!model.hasNode(n))
				return false;

		for (Link l : links)
			if (!model.hasLink(l))
				return false;

		return true;
	}

	public NodeStructure getSoughtContent(WorkspaceBuffer csm) {
		if (hasSoughtContent(csm))
			return (NodeStructure)csm.getModuleContent();
		else
			return null;
	}
	public String toString(){
		return "AttentionCodelet-"+ getTaskId();
	}

}// class