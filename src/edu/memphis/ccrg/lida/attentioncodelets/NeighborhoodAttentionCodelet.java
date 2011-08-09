/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Creates {@link Coalition}s if the nodes specified in the 'nodes' parameter are present.
 * The resulting {@link Coalition} includes these nodes and their neighbors.
 * 
 * @author Ryan J. McCall
 */
public class NeighborhoodAttentionCodelet extends BasicAttentionCodelet {

    private static final Logger logger = Logger.getLogger(NeighborhoodAttentionCodelet.class.getCanonicalName());
    private Map<String, Object> params = new HashMap<String, Object>();

    @Override
    public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
        NodeStructure bufferNS = buffer.getBufferContent(params);
        NodeStructure subGraph = new NodeStructureImpl();
        if (bufferNS != null) {
            //TODO add getNeighborhood(Node, int) method in NodeStructure?
            for (Node n : soughtContent.getNodes()){//typically a small number
                if (bufferNS.containsNode(n)) {
                    subGraph.addDefaultNode(bufferNS.getNode(n.getId()));
                    Map<Linkable, Link> sinks = bufferNS.getConnectedSinks(n);
                    for (Linkable sink : sinks.keySet()) {
                        if (sink instanceof Node) {
                            subGraph.addDefaultNode((Node) sink);
                            Link connectingLink = sinks.get(sink);
                            subGraph.addDefaultLink(connectingLink);
                        }
                    }

                    Map<Node, Link> sources = bufferNS.getConnectedSources(n);
                    for (Node source : sources.keySet()) {
                        subGraph.addDefaultNode(source);
                        Link connectingLink = sources.get(source);
                        subGraph.addDefaultLink(connectingLink);
                    }
                }
            }
        }else{
            logger.log(Level.WARNING, "Buffer returned null NodeStructure", TaskManager.getCurrentTick());
        }
        return subGraph;
    }
}
