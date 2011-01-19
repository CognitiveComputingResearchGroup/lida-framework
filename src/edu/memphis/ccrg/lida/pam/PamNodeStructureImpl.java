/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * A node structure that supports activation passing between linkables.
 * @author Ryan J McCall
 */
public class PamNodeStructureImpl extends NodeStructureImpl implements PamNodeStructure{
	
	private static final Logger logger = Logger.getLogger(PamNodeStructureImpl.class.getCanonicalName());

	/**
	 * If a node is below this threshold after being decayed it is deleted
	 */
	private double nodeRemovalThreshold = 0.01;
	
	public PamNodeStructureImpl(){
		super("PamNodeImpl", "PamLinkImpl");
	}

	public PamNodeStructureImpl(String defaultPamNode, String defaultLink) {
		super(defaultPamNode, defaultLink);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PamNodeStructure#setNodesExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)
	 */
	@Override
	public void setNodesExciteStrategy(ExciteStrategy behavior) {
    	for(Node n: getNodes()){
    		n.setExciteStrategy(behavior);
    	}
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PamNodeStructure#setNodesDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)
	 */
	@Override
	public void setNodesDecayStrategy(DecayStrategy strategy) {
    	for(Node n: getNodes())
    		n.setDecayStrategy(strategy);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PamNodeStructure#getParentsAndConnectingLinksOf(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public Map<PamNode, PamLink> getParentsAndConnectingLinksOf(Node n){
		Map<PamNode, PamLink> results = new HashMap<PamNode, PamLink>();
		Set<Link> candidateLinks = getLinkableMap().get(n);
		if(candidateLinks != null){
			for(Link l: candidateLinks){
				Linkable sink = l.getSink();//Sinks are "higher than" node n, i.e. the parents of this node. 
				if(!sink.equals(n)){
					results.put((PamNode) sink, (PamLink) l);	
				}
			}//for
		}
		return results;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PamNodeStructure#decayLinkables(long)
	 */
	@Override
	public void decayLinkables(long ticks){
		logger.log(Level.FINE,"Decaying the Pam NodeStructure",LidaTaskManager.getCurrentTick());
		for(Linkable l: getLinkables()){
			Learnable la = (Learnable) l;
			la.decay(ticks);
			la.decayBaseLevelActivation(ticks);
			if(la.getTotalActivation() < nodeRemovalThreshold){
				removeLinkable(l);
			}
		}
	}

}