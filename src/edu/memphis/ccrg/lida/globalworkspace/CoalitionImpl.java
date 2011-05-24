/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;

//TODO Make Coalition a Factory element.  then we can change the way a coalition calculates its activation
// and the type of content that it has.
/**
 * Default implementation of {@link Coalition}.  Wraps content entering the 
 * {@link GlobalWorkspace} to compete for consciousness. Extends {@link ActivatibleImpl}.
 * Contains reference to the {@link AttentionCodelet} that created it.
 */
public class CoalitionImpl extends ActivatibleImpl implements Coalition{
	
	private BroadcastContent content;	
	private double attentionCodeletActivation;
	private AttentionCodelet attentionCodelet;

	/**
	 * Constructs a coalition with content and sets activation to be equal to 
	 * the normalized sum of the activation of the {@link Linkable}s in the {@link NodeStructure}
	 * times the activation of the creating {@link AttentionCodelet}
	 * @param content conscious content
	 * @param activation activation of creating attention codelet
	 * @param codelet The {@link AttentionCodelet} that created this Coalition
	 * @see AttentionCodeletImpl
	 */
	public CoalitionImpl(NodeStructure content, double activation,AttentionCodelet codelet){
		this.content = (BroadcastContent) content;
		attentionCodeletActivation = activation;
		attentionCodelet = codelet;
		updateActivation();
	}

	/*
	 * calculates coalition's activation based on BroadcastContent attention codelet activation
	 */
	private void updateActivation() {
		double sum = 0.0;
		NodeStructure ns = (NodeStructure) content;
		for(Linkable lnk: ns.getLinkables()){
			sum += lnk.getActivation();
		}
		setActivation(attentionCodeletActivation * sum / ns.getLinkableCount());
	}

	@Override
	public BroadcastContent getContent() {
		return content;
	}
	
	@Override
	public AttentionCodelet getAttentionCodelet(){		
		return attentionCodelet;
	}
	
}