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
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Default implementation of {@link PamNodeStructure}.
 * A node structure that supports activation passing between linkables.
 * @author Ryan J McCall
 */
public class PamNodeStructureImpl extends NodeStructureImpl implements PamNodeStructure{
	
	private static final Logger logger = Logger.getLogger(PamNodeStructureImpl.class.getCanonicalName());
	
	/**
	 * Instantiates a new {@link PamNodeStructureImpl} using {@link PamNodeImpl} and {@link PamLinkImpl}
	 */
	public PamNodeStructureImpl(){
		super(PamNodeImpl.class.getSimpleName(), PamLinkImpl.class.getSimpleName());
	}

	/**
	 * Instantiates a new {@link PamNodeStructureImpl} with specified Node and Link type. 
	 * 
	 * @param defaultPamNode
	 *            the default pam node
	 * @param defaultLink
	 *            the default link
	 */
	public PamNodeStructureImpl(String defaultPamNode, String defaultLink) {
		super(defaultPamNode, defaultLink);
	}
	
	/**
	 * Copy constructor
	 * @param pns source {@link PamNodeStructure}
	 */
	public PamNodeStructureImpl(PamNodeStructure pns){
		super(pns);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PamNodeStructure#getParentsAndConnectingLinksOf(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public Map<PamNode, PamLink> getParentsWithLinks(PamNode n){
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
	public void decayNodeStructure(long ticks){
		logger.log(Level.FINE,"Decaying the Pam NodeStructure",LidaTaskManager.getCurrentTick());
		for(Linkable linkable: getLinkables()){
			Learnable learnable = (Learnable) linkable;
			learnable.decay(ticks);
			learnable.decayBaseLevelActivation(ticks);
			if(learnable.isRemovable()){
				removeLinkable(linkable);
			}
		}
	}
}