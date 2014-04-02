package edu.memphis.ccrg.lida.motivation.pam;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.pam.BasicPamInitializer;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * An {@link Initializer} for the {@link MotivationPerceptualAssociativeMemory} that
 * extends {@link BasicPamInitializer} by allowing feeling nodes to be declared.
 * @author Ryan J McCall
 */
public class MotivationPamInitializer extends BasicPamInitializer {
	
	private static final Logger logger = Logger.getLogger(MotivationPamInitializer.class.getCanonicalName());
	
	@Override
	public void initModule(FullyInitializable m, Agent agent, Map<String, ?> params) {
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory)m;
		initNodes(pam, params);		
		initLinkCategories(pam, params);
		initLinks(pam, params);
	}
	
	/**
	 * Parses a parameter called "nodes".
	 * The parameter's value should be a String consisting of a sequence of nodeDefs separated by ','.
	 * Each NodeDef must have the following form:
	 * <i>label:baseLevelActivation:factoryName:valence:isDrive</i> </br>
	 * Examples:</br>
	 * cup</br>
	 * cup:0.1</br>
	 * cup:0.1:PamNodeImpl</br>
	 * sweet:0.0:FeelingPamNodeImpl:positive</br>
	 * thirst:0.0:FeelingPamNodeImpl:negative:drive</br>
	 * 
	 * Only the label is mandatory, but this order must be preserved, e.g., factory name cannot come second.
	 */
	protected static void initNodes(PerceptualAssociativeMemory pam, Map<String, ?> params) {
		String nodes = (String) params.get("nodes");
		if (nodes != null) {
			String[] defs = nodes.split(",");
			for (String nodeDef: defs) {
				nodeDef = nodeDef.trim();
				String[] nodeParams = nodeDef.split(":");
				String label = nodeParams[0];
				if ("".equals(label)) {
					logger.log(Level.WARNING,
							"Empty string found in node specification, node labels must be non-empty");
				}else{
					logger.log(Level.INFO, "Loading PamNode: {0}", label);
					PamNode node = null;
					if(nodeParams.length >= 3){
						node=pam.addNode(nodeParams[2], label);
						if(node instanceof FeelingPamNodeImpl){ 
							FeelingPamNodeImpl feelingNode = (FeelingPamNodeImpl) node;
							if(nodeParams.length >= 4){
								if("negative".equalsIgnoreCase(nodeParams[3])){
									feelingNode.setValenceSign(-1);
								}
							}
							if(nodeParams.length >= 5){
								if("drive".equalsIgnoreCase(nodeParams[4])){
									feelingNode.setDrive(true);
								}
							}
						}
					}else{
						node=pam.addDefaultNode(label);
					}
					if (node == null) {
						logger.log(Level.WARNING,
								"Failed to get Node '{0}' from PAM.", label);
					}else{
						globalInitializer.setAttribute(label, node);
						if (nodeParams.length >= 2) {
							parseBaseLevelActivation(nodeParams[1],node);
						}
					}
				}
			}
		}
	}
}
