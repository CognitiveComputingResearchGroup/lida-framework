/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * A basic algorithm to activate {@link Scheme}s given a {@link NodeStructure}
 * @author Ryan J. McCall
 *
 */
public class BasicSchemeActivationBehavior implements SchemeActivationBehavior {

	private static final Logger logger = Logger
			.getLogger(BasicSchemeActivationBehavior.class.getCanonicalName());

	private ProceduralMemory pm;
	private static final double DEFAULT_SELECTION_THRESHOLD = 0.6;
	private double schemeSelectionThreshold = DEFAULT_SELECTION_THRESHOLD;

	public BasicSchemeActivationBehavior(ProceduralMemory pm) {
		this.pm = pm;
	}

	/**
	 * params[0] must contain Map<? extends Object, Set<Scheme>> with all the
	 * Schemes of {@link ProceduralMemory}
	 * 
	 * @see edu.memphis.ccrg.lida.proceduralmemory.SchemeActivationBehavior#activateSchemesWithBroadcast(edu.memphis.ccrg.lida.framework.shared.NodeStructure,
	 *      java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void activateSchemesWithBroadcast(NodeStructure broadcast, Object... params) {
		logger.log(Level.FINEST, "activating schemes from broadcast",
				LidaTaskManager.getCurrentTick());
		Set<Scheme> toInstantiate = new HashSet<Scheme>();
		Map<?, Set<Scheme>> schemeMap = (Map<?, Set<Scheme>>) params[0];
		for (Node n: broadcast.getNodes()) {
			Set<Scheme> schemes = schemeMap.get(n);
			if (schemes != null) {
				for (Scheme scheme : schemes) {
					scheme.excite(n.getActivation() / scheme.getContext().getNodeCount());
					if (scheme.getActivation() >= schemeSelectionThreshold) {
						//To prevent repeats we stored all schemes over threshold in a set.
						//repeats occur with this algorithm when the scheme selection threshold is low
						toInstantiate.add(scheme);
					}
				}
			}
		}
		
		for(Scheme s: toInstantiate){
			pm.sendInstantiatedScheme(s);
		}
	}

	@Override
	public void setSchemeSelectionThreshold(double d) {
		schemeSelectionThreshold = d;
	}

}
