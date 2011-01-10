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
 * @author Ryan McCall, Javier Snaider
 */
public class LinkImpl extends ActivatibleImpl implements Link {

	private static Logger logger = Logger.getLogger(LinkImpl.class.getCanonicalName());
	
	/**
	 * Source of this link, always a node.
	 */
	private Node source;
	
	/**
	 * Sink of this link, a Linkable.
	 */
	private Linkable sink;
	
	/**
	 * A custom id dependent on the source's and the sink's ids.
	 */
	private ExtendedId id;
	
	/**
	 * Category of this Link.
	 */
	private LinkCategory category;
	
	/**
	 * PamLink in PAM that grounds this Link.
	 */
	protected PamLink groundingPamLink;
	
	protected Map<String, ?> parameters;
	
	public LinkImpl() {
	}

	public LinkImpl(Node source, Linkable sink, LinkCategory category) {
		this.source = source;
		this.sink = sink;
		this.category = category;
		
		updateExtendedId();
	}

	public LinkImpl(Link l) {
		sink = l.getSink();
		source = l.getSource();
		category = l.getCategory();
		id = l.getExtendedId();
		groundingPamLink = l.getGroundingPamLink();
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
			id = new ExtendedId(category.getId(), ((Node) source).getId(), sink.getExtendedId());
		}
	}

	@Override
	public ExtendedId getExtendedId() {
		return id;
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
		logger.log(Level.FINEST, "Set grounding pam link " + l.toString(), LidaTaskManager.getActualTick());
		groundingPamLink = l;
	}
	
	@Override
	public String getFactoryLinkType() {
		return LinkImpl.class.getSimpleName();
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
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
		
		return id.equals(other.getExtendedId());
	}
	
	
	@Override
	public String getLabel() {
		return "Link: "+category.getLabel()+" " + id;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}

}
