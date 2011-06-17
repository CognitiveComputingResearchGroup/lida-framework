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
 * nodes are added to the {@link GlobalInitializer}
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class BasicPamInitializer implements Initializer {
	
	private static final Logger logger = Logger.getLogger(BasicPamInitializer.class.getCanonicalName());

	public void initModule(FullyInitializable module, Agent agent,
			Map<String, ?> params) {
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
		ElementFactory factory = ElementFactory.getInstance();

		String nodeLabels = (String) params.get("nodes");
		if (nodeLabels != null) {
			GlobalInitializer globalInitializer = GlobalInitializer
					.getInstance();
			String[] labels = nodeLabels.split(",");
			for (String label : labels) {
				label = label.trim();
				PamNode node = (PamNode) factory.getNode("PamNodeImpl", label);
				node = pam.addDefaultNode(node);
				globalInitializer.setAttribute(label, node);
			}
		}
		
		String linkLabels = (String) params.get("links");
		if (linkLabels != null) {
			String[] linkDefs = linkLabels.split(",");
			for (String linkDef : linkDefs) {
				linkDef = linkDef.trim();
				String[] nodes = linkDef.split("\\.");
				if(nodes.length != 2){
					logger.log(Level.WARNING, "bad link specification " + linkDef, TaskManager.getCurrentTick());
					continue;
				}
				Node source = pam.getNode(nodes[0]);
				Node sink = pam.getNode(nodes[1]);
				if(source != null && sink != null){
					Link link = factory.getLink(source, sink, PerceptualAssociativeMemoryImpl.NONE);
					pam.addDefaultLink(link);
				}else{
					logger.log(Level.WARNING, "could not find source or sink " + linkDef, TaskManager.getCurrentTick());
				}
			}
		}
	}
}
