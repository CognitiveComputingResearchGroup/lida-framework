/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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
	private static Logger logger = Logger.getLogger(BasicSchemeActivationBehavior.class.getCanonicalName());

	private ProceduralMemory pm;

	private double schemeSelectionThreshold = 0.6;

	public BasicSchemeActivationBehavior(ProceduralMemory pm){
		this.pm = pm;
	}

	@Override
	public void activateSchemesWithBroadcast(NodeStructure broadcast,
			Map<? extends Object, Set<Scheme>> schemeMap) {
		logger.log(Level.FINEST, "Scheme are tested for activation",
				LidaTaskManager.getActualTick());
		Collection<Node> nodes = broadcast.getNodes();
		Collection<Link> links = broadcast.getLinks();
		
		for (Node n : nodes)
			auxActivateSchemes(n, schemeMap);
		for (Link l : links)
			auxActivateSchemes(l, schemeMap);
	}

	private void auxActivateSchemes(Linkable l, Map<? extends Object, Set<Scheme>> schemeMap) {
		if (schemeMap.containsKey(l)) {
			Set<Scheme> schemes = schemeMap.get(l);
			int numSchemes=schemes.size();
			for (Scheme scheme : schemes) {
				NodeStructure context = scheme.getContext();
				int contextNodeCount = context.getNodeCount();
				scheme.excite(1.0 / (contextNodeCount * numSchemes));
				if (scheme.getActivation() > schemeSelectionThreshold) {
					// Copy?
					pm.sendInstantiatedScheme(scheme);
					logger.log(Level.FINE, "Scheme is instantiated",
								LidaTaskManager.getActualTick());
				}
			}// for
		}
	}// method

	public void setSchemeSelectionThreshold(double d) {
		schemeSelectionThreshold = d;
	}

}
