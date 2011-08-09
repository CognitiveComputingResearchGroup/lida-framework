package edu.memphis.ccrg.lida.attentioncodelets;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * 
 * @author Ryan J. McCall
 */
public class DefaultAttentionCodelet extends NeighborhoodAttentionCodelet {

	private static final double DEFAULT_ATTENTION_THRESHOLD = 0.5;
	private double attentionThreshold = DEFAULT_ATTENTION_THRESHOLD;

	@Override
	public void init() {
		super.init();
		attentionThreshold = (Double) getParam("attentionThreshold",
				DEFAULT_ATTENTION_THRESHOLD);
	}

	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		soughtContent.clearNodeStructure();
		
		Node winner = null;
		double winnerActivation = -2.0;
		NodeStructure ns = (NodeStructure) buffer.getBufferContent(null);
        for (Node n : ns.getNodes()) {
        	double activation = n.getActivation();
            if (activation > attentionThreshold && activation > winnerActivation) {
            	winner = n;
            	winnerActivation = activation;
            }
        }
		
        if(winner != null){
        	soughtContent.addDefaultNode(winner);
        	return true;
        }
        
		return false;
	}

}
