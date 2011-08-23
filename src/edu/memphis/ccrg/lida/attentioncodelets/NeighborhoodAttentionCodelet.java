/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Creates {@link Coalition}s if the nodes specified in the 'nodes' parameter are present.
 * The resulting {@link Coalition} includes these nodes and their neighbors.
 * 
 * @author Ryan J. McCall
 */
public class NeighborhoodAttentionCodelet extends DefaultAttentionCodelet {

    private static final Logger logger = Logger.getLogger(NeighborhoodAttentionCodelet.class.getCanonicalName());

    @Override
	public void init() {
		super.init();
		super.attentionThreshold = 0.0;//want nodes regardless of their activation
		
		String nodeLabels = (String) getParam("nodes", null);
		if (nodeLabels != null) {
            GlobalInitializer globalInitializer = GlobalInitializer.getInstance();
            String[] labels = nodeLabels.split(",");
            for (String label : labels) {
                label = label.trim();
                Node node = (Node) globalInitializer.getAttribute(label);
                if (node != null) {
                    soughtContent.addDefaultNode(node);
                }else{
                	logger.log(Level.WARNING, "could not find node with label: {0} in global initializer", label);
                }
            }
        }
    }
    
    /**
     * Returns true if specified WorkspaceBuffer contains this codelet's sought
     * content.
     * 
     * @param buffer
     *            the WorkspaceBuffer to be checked for content
     * @return true, if successful
     */
    @Override
    public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
        NodeStructure model = (NodeStructure) buffer.getBufferContent(null);

        for (Linkable ln : soughtContent.getLinkables()) {
            if (!model.containsLinkable(ln)) {
                return false;
            }
        }

        logger.log(Level.FINEST, "Attn codelet {1} found sought content",
                new Object[]{TaskManager.getCurrentTick(), this});
        return true;
    }
   
}