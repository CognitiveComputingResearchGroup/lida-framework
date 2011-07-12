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
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

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
