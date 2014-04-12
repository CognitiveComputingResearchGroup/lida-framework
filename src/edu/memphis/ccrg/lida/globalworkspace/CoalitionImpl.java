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
import edu.memphis.ccrg.lida.framework.shared.UnmodifiableNodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;

/**
 * The default implementation of {@link Coalition}. Wraps content entering the
 * {@link GlobalWorkspace} to compete for consciousness. Extends
 * {@link ActivatibleImpl}. Contains reference to the {@link AttentionCodelet}
 * that created it.
 * 
 * @author Ryan J. McCall
 * @see AttentionCodeletImpl
 */
public class CoalitionImpl extends ActivatibleImpl implements Coalition {

	private static int idCounter = 0;
	/*
	 * unique id
	 */
	private int id;
	/**
	 * the {@link BroadcastContent} of the coalition
	 */
	protected NodeStructure broadcastContent;

	/**
	 * the {@link AttentionCodelet} that created the coalition
	 */
	protected AttentionCodelet codelet;

	/**
	 * Default constructor
	 */
	public CoalitionImpl() {
		super();
		id = idCounter++;
	}

	/**
	 * Constructs a {@link CoalitionImpl} with specified content that is being
	 * created by specified {@link AttentionCodelet}
	 * 
	 * @param ns
	 *            the {@link BroadcastContent}
	 * @param c
	 *            The {@link AttentionCodelet} that created this Coalition
	 * @see AttentionCodeletImpl
	 */
	public CoalitionImpl(NodeStructure ns, AttentionCodelet c) {
		this();
		if(ns==null || c==null){
			throw new IllegalArgumentException("Constructor argument null: " + ns + " " + c);		
		}
		setContent((BroadcastContent) ns);
		setCreatingAttentionCodelet(c);
		setCoalitionActivation();
	}

	/**
	 * Sets the coalition's activation to the average total activation and 
	 * total incentive salience of the broadcast content 
	 * multiplied by the attention codelet's base-level activation.
	 */
	protected void setCoalitionActivation() {
		double a = 0.0;
		if(broadcastContent.getLinkableCount()!=0){
			double salienceSum = 0.0;
			for (Linkable linkable: broadcastContent.getLinkables()) {
				salienceSum += linkable.getTotalActivation()+Math.abs(linkable.getTotalIncentiveSalience());
			}
			//Divide by 2 since each Linkable could contribute at most 2.
			a = codelet.getBaseLevelActivation()*salienceSum/(2*broadcastContent.getLinkableCount());
		}
		super.setActivation(a);
	}

	/**
	 * Returns an {@link UnmodifiableNodeStructureImpl} containing the broadcast
	 * content. Note that {@link UnmodifiableNodeStructureImpl} cannot be modified.
	 */
	@Override
	public BroadcastContent getContent() {
		return (BroadcastContent) broadcastContent;
	}

	@Override
	public void setContent(BroadcastContent c) {
		broadcastContent = new UnmodifiableNodeStructureImpl((NodeStructure) c,true);
	}

	@Override
	public void setCreatingAttentionCodelet(AttentionCodelet c) {
		codelet=c;
	}

	@Override
	public AttentionCodelet getCreatingAttentionCodelet() {
		return codelet;
	}

	@Override
	public int getId() {
		return id;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof CoalitionImpl) {
			CoalitionImpl c = (CoalitionImpl) o;
			return id == c.id;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return (int)id;
	}
}