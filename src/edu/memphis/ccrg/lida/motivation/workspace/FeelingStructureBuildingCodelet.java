package edu.memphis.ccrg.lida.motivation.workspace;

import java.util.ArrayList;
import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.motivation.shared.FeelingNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.StructureBuildingCodeletImpl;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Tries to associate feeling nodes with coincidentally active event nodes.
 * @author Ryan J. McCall
 */
public class FeelingStructureBuildingCodelet extends StructureBuildingCodeletImpl {
	
	@Override
	protected void runThisFrameworkTask() {
		NodeStructure bufferContent = writableBuffer.getBufferContent(null);
		Collection<FeelingNode> feelingNodes = new ArrayList<FeelingNode>();
		Collection<Node> eventNodes = new ArrayList<Node>();
		for(Node n: bufferContent.getNodes()){
			if(n instanceof FeelingNode){
				feelingNodes.add((FeelingNode) n);
			}else {
				eventNodes.add(n);
			}
		}
		for(Node feelingNode: feelingNodes){		
			for(Node eventNode: eventNodes){
				//Add a link from Feeling to Max active node. 
				//Runs the risk of connecting Feelings to non-event nodes if such nodes occur.
				//Activation is harmonic mean
				double linkActivation = 2*feelingNode.getActivation()*eventNode.getActivation()/(feelingNode.getActivation()+eventNode.getActivation());
				bufferContent.addDefaultLink(feelingNode, eventNode, 
											PerceptualAssociativeMemoryImpl.PARENT_LINK_CATEGORY, 
											linkActivation, 0.0);
			}
		}
	}
	
	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		return null;
	}
	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		return false;
	}
}