/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.PamLink;

/**
 * A Link that connects a Node to a Linkable (Node or Link).
 * 
 * WARNING:	Renaming this class requires renaming values in
 * 	  configs/factoriesData.xml
 * 
 * @author Ryan J. McCall, Javier Snaider
 */
public class LinkImpl extends ActivatibleImpl implements Link {

	private static final Logger logger = Logger.getLogger(LinkImpl.class.getCanonicalName());

	public static final String factoryName = LinkImpl.class.getSimpleName();
	
	/*
	 * Source of this link, always a node.
	 */
	private Node source;
	
	/*
	 * Sink of this link, a Linkable.
	 */
	private Linkable sink;
	
	/*
	 * A custom id dependent on the source's and the sink's ids.
	 */
	private ExtendedId extendedId;
	
	/*
	 * Category of this Link.
	 */
	private LinkCategory category;
	
	/**
	 * PamLink in PAM that grounds this Link.
	 */
	protected PamLink groundingPamLink;
	
	protected Map<String, ?> parameters;
	
	public LinkImpl() {
		super();
	}
	
	public LinkImpl(Node source, Linkable sink, LinkCategory category) {
		this.source = source;
		this.sink = sink;
		this.category = category;
		updateExtendedId();
	}

	public LinkImpl(LinkImpl l) {
		sink = l.getSink();
		source = l.getSource();
		category = l.getCategory();
		groundingPamLink = l.getGroundingPamLink();
		parameters = l.parameters;
		updateExtendedId();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.parameters = params;
		init();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init()
	 */
	@Override
	public void init() {
	}
	
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/*
	 * Refreshes this Link's ExtendedId based on its category, source, and sink.
	 */
	private void updateExtendedId() {
		if(category != null && source != null && sink != null){
			logger.log(Level.FINEST, "updated extended id", LidaTaskManager.getCurrentTick());
			extendedId = new ExtendedId(category.getId(), source.getId(), sink.getExtendedId());
		}
	}

	@Override
	public ExtendedId getExtendedId() {
		return extendedId;
	}

	@Override
	public Linkable getSink() {
		return sink;
	}

	@Override
	public Node getSource() {
		return source;
	}

	@Override
	public LinkCategory getCategory() {
		return category;
	}

	@Override
	public void setSink(Linkable sink) {
		this.sink = sink;
		updateExtendedId();
	}

	@Override
	public void setSource(Node source) {
		this.source = source;
		updateExtendedId();
	}

	@Override
	public void setCategory(LinkCategory category) {
		this.category = category;
		updateExtendedId();
	}

	@Override
	public PamLink getGroundingPamLink() {
		return groundingPamLink;
	}

	@Override
	public void setGroundingPamLink(PamLink l) {
		groundingPamLink = l;
	}
	
	@Override
	public int hashCode() {
		return extendedId.hashCode();
	}
	
	/**
	 * This method compares this LinkImpl with any kind of Link.
	 * Two Links are equal if and only if they have the same id.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Link)) {
			return false;
		}
		Link other = (Link) obj;
		
		return extendedId.equals(other.getExtendedId());
	}
	
	
	@Override
	public String getLabel() {
		return category.getLabel();
	}
	
	@Override
	public String toString() {
		return getLabel() + " " + extendedId;
	}

	@Override
	public Link copy() {
		return new LinkImpl(this);
	}

}
