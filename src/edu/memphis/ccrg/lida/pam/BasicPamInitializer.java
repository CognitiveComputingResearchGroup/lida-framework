package edu.memphis.ccrg.lida.pam;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * An {@link Initializer} for {@link PerceptualAssociativeMemory} which receives a parameter named
 * 'nodes' containing a list of node labels.  Nodes are created in {@link PerceptualAssociativeMemory} and 
 * nodes are added to the {@link GlobalInitializer}.
 * A parameter name 'links' contains a list of link definitions.
 * 
 * The definition for 'nodes' is: <br/>
 * <b>nodeLabel1,nodeLabel2,...</b>
 * <br/>
 * The definition for 'links' is: <br/>
 * <b>sourceNodeLabel1:sinkNodeLabel2,...</b>
 * 
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class BasicPamInitializer implements Initializer {

    private static final Logger logger = Logger.getLogger(BasicPamInitializer.class.getCanonicalName());

    @Override
    public void initModule(FullyInitializable module, Agent agent,
            Map<String, ?> params) {
        PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
        ElementFactory factory = ElementFactory.getInstance();

        String nodeLabels = (String) params.get("nodes");
        if (nodeLabels != null) {
            GlobalInitializer globalInitializer = GlobalInitializer.getInstance();
            String[] labels = nodeLabels.split(",");
            for (String label : labels) {
                label = label.trim();
                if("".equals(label)){
                	logger.log(Level.WARNING, 
        			"empty string found in nodes specification, node labels must be non-empty");
	                
                }else{
                	logger.log(Level.INFO, "loading PamNode: {0}", label);
	                PamNode node = (PamNode) factory.getNode("PamNodeImpl", label);
	                if(node == null){
	                	logger.log(Level.WARNING, "failed to get node {0}", label);
	                }else{
	                	node = pam.addDefaultNode(node);
	                	globalInitializer.setAttribute(label, node);
	                }
                }
            }
        }

        String linkLabels = (String) params.get("links");
        if (linkLabels != null) {
            String[] linkDefs = linkLabels.split(",");
            for (String linkDef : linkDefs) {
                linkDef = linkDef.trim();
                if("".equals(linkDef)){
                	logger.log(Level.WARNING, 
        			"empty string found in links specification, link defs must be non-empty");
                	continue;
                }
                logger.log(Level.INFO, "loading PamLink: {0}", linkDef);
                String[] nodes = linkDef.split(":");
                if (nodes.length != 2) {
                    logger.log(Level.WARNING, "bad link specification " + linkDef, TaskManager.getCurrentTick());
                    continue;
                }
                Node source = pam.getNode(nodes[0].trim());
                Node sink = pam.getNode(nodes[1].trim());
                if (source != null && sink != null) {
                    Link link = factory.getLink("PamLinkImpl", source, sink, PerceptualAssociativeMemoryImpl.PARENT);
                    pam.addDefaultLink(link);
                } else {
                    logger.log(Level.WARNING, "could not find source or sink " + linkDef, TaskManager.getCurrentTick());
                }
            }
        }
    }
}
