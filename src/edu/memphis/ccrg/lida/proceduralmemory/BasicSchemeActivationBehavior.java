package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class BasicSchemeActivationBehavior implements SchemeActivationBehavior {
	private static Logger logger = Logger.getLogger("lida.proceduralmemory");

	private ProceduralMemory pm;

	private double schemeSelectionThreshold = 0.6;

	public BasicSchemeActivationBehavior(ProceduralMemory pm, double selectionThreshold) {
		this.pm = pm;
		this.schemeSelectionThreshold = selectionThreshold;
	}

	public void activateSchemesWithBroadcast(NodeStructure broadcast,
			Map<Linkable, Set<Scheme>> schemeMap) {
		logger.log(Level.FINEST, "Scheme are tested for activation",
				LidaTaskManager.getActualTick());
		Collection<Node> nodes = broadcast.getNodes();
		Collection<Link> links = broadcast.getLinks();
		
		System.out.println("broadcast contains " + nodes.size() + " links " + links.size());
		
		for (Node n : nodes)
			auxActivateSchemes(n, schemeMap);
		for (Link l : links)
			auxActivateSchemes(l, schemeMap);
	}

	private void auxActivateSchemes(Linkable l,
			Map<Linkable, Set<Scheme>> schemeMap) {
		if (schemeMap.containsKey(l)) {
			Set<Scheme> schemes = schemeMap.get(l);
			int cantSchemes=schemes.size();
			for (Scheme s : schemes) {
				for (NodeStructure ns : s.getContextConditions()) {
					int contextCount = ns.getNodeCount();
					s.excite(1.0 / (contextCount * cantSchemes));
					if (s.getActivation() > schemeSelectionThreshold) {
						// Copy?
						pm.sendInstantiatedScheme(s);
						logger.log(Level.FINE, "Scheme is instantiated",
								LidaTaskManager.getActualTick());
					}
				}
			}// for
		}
	}// method

	public void setSchemeSelectionThreshold(double d) {
		schemeSelectionThreshold = d;
	}

}
