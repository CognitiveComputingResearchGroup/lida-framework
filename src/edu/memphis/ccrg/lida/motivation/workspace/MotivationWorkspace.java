package edu.memphis.ccrg.lida.motivation.workspace;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.motivation.shared.FeelingNode;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * This {@link Workspace} adds percepts directly to the CSM as opposed to the PerceptualBuffer.
 * @author ryanjmccall
 */
public class MotivationWorkspace extends WorkspaceImpl {
	
	private LinkCategory temporalCategory;

	void setTemporalCategory(LinkCategory c){
		temporalCategory = c;
	}

	@Override
	public void receivePercept(NodeStructure ns) {
		WorkspaceBuffer buffer = (WorkspaceBuffer)getSubmodule(ModuleName.CurrentSituationalModel);
		buffer.addBufferContent((WorkspaceContent) ns);
	}

	@Override
	public void receivePercept(Node n) {
		WorkspaceBuffer csm = (WorkspaceBuffer)getSubmodule(ModuleName.CurrentSituationalModel);
		NodeStructure csmStructure = csm.getBufferContent(null);
		csmStructure.addNode(n, n.getFactoryType());
		if(!(n instanceof FeelingNode)){
			buildTemporalLink(n, csmStructure);
		}
	}

	/*
	 * It appears preferable to perform this operation right when a node is added to the CSM
	 * as opposed to performing this op cyclicly in a task.
	 */
	private void buildTemporalLink(Node n, NodeStructure csmStructure) {
		BroadcastQueue queue = (BroadcastQueue) getSubmodule(ModuleName.BroadcastQueue);
		NodeStructure queueStructure = queue.getPositionContent(0);
		if (queueStructure != null) {
			Node sink = n;
			if(!queueStructure.containsNode(sink)){ 
				Node source = WorkspaceUtils.getFirstEventNode(queueStructure, temporalCategory);
				if(source != null){
					if(!csmStructure.containsNode(source)){ // Avoid endlessly exciting source
						csmStructure.addNode(source, source.getFactoryType());
					}
					ExtendedId oppositeId = new ExtendedId(sink.getId(), source.getExtendedId(), temporalCategory.getId());
					if(!csmStructure.containsLink(oppositeId)){	// Avoid creating cycles
						csmStructure.addDefaultLink(source, sink, temporalCategory, 1.0, 0.0);						
					}
				}	
			}
		}
	}
	
	@Override
	public void receivePercept(Link l) {
		WorkspaceBuffer buffer = (WorkspaceBuffer)getSubmodule(ModuleName.CurrentSituationalModel);
		NodeStructure ns = buffer.getBufferContent(null);
		ns.addLink(l,l.getFactoryType());
	}
}