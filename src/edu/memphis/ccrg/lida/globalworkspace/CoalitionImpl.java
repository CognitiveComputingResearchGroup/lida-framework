/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.framework.shared.UnmodifiableCognitiveContentStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.Linkable;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

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

	private static final Logger logger = Logger.getLogger(CoalitionImpl.class
			.getCanonicalName());
	private static int idCounter = 0;
	/*
	 * unique id
	 */
	private int id;
	/**
	 * the contents of the coalition
	 */
	protected CognitiveContentStructure broadcastContent;

	/**
	 * the {@link AttentionCodelet} that created the coalition
	 */
	protected AttentionCodelet creatingAttentionCodelet;

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
	 *            the broadcast content
	 * @param c
	 *            The {@link AttentionCodelet} that created this Coalition
	 * @see AttentionCodeletImpl
	 */
	public CoalitionImpl(CognitiveContentStructure ns, AttentionCodelet c) {
		this();
		broadcastContent = new UnmodifiableCognitiveContentStructureImpl(ns,true);
		creatingAttentionCodelet = c;
		if(creatingAttentionCodelet != null) {
			updateActivation();
		}else{
			logger.log(Level.WARNING, "Coalition received null AttentionCodelet",
					TaskManager.getCurrentTick());
		}
	}

	/**
	 * Calculates and sets the coalition's activation. This implementation uses the
	 * average total activation and total incentive salience of the broadcast content 
	 * multiplied by the attention codelet's base-level activation.
	 */
	protected void updateActivation() {
		double a = 0.0;
		
		// TODO: Need to implement using an activation strategy
		setActivation(a);
	}

	@Override
	public CognitiveContentStructure getContent() {
		return broadcastContent;
	}

	@Override
	public AttentionCodelet getCreatingAttentionCodelet() {
		return creatingAttentionCodelet;
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