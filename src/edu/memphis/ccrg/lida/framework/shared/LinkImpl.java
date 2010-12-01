/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;

import edu.memphis.ccrg.lida.pam.PamLink;

/**
 * 
 * @author Ryan McCall, Javier Snaider
 */
public class LinkImpl extends ActivatibleImpl implements Link {

	private Linkable sink;
	private Linkable source;
	private ExtendedId id;
	
	private LinkCategory category;
	protected PamLink groundingPamLink;
	
	protected Map<String, ?> parameters;
	
	public LinkImpl() {
	}

	public LinkImpl(Linkable source, Linkable sink, LinkCategory category) {
		this.source = source;
		this.sink = sink;
		this.category = category;
		
		updateIds();
	}

	public LinkImpl(Link l) {
		sink = l.getSink();
		source = l.getSource();
		category = l.getCategory();
		id = l.getExtendedId();
		groundingPamLink = l.getGroundingPamLink();
		updateIds();
	}

	public LinkImpl copy() {
		return new LinkImpl(this);
	}

	@Override
	public ExtendedId getExtendedId() {
		return id;
	}
	
	@Override
	public String getLabel() {
		return "Link: " + id;
	}

	@Override
	public Linkable getSink() {
		return sink;
	}

	@Override
	public Linkable getSource() {
		return source;
	}

	@Override
	public LinkCategory getCategory() {
		return category;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	/**
	 * This method compares this LinkImpl with any kind of Link.
	 * Two Links are
	 * equals if and only if they have the same id.
	 * 
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
	public void setSink(Linkable sink) {
		this.sink = sink;
		updateIds();
	}

	@Override
	public void setSource(Linkable source) {
		this.source = source;
		updateIds();
	}

	@Override
	public void setCategory(LinkCategory category) {
		this.category = category;
		updateIds();
	}

	@Override
	public String toString() {
		return getLabel();
	}

	private void updateIds() {
		//ids = "L(" + ((source!=null)?source.getIds():"") + ":" + ((sink!=null)?sink.getIds():"") + ":" + category + ")";
		//TODO Source should always be a node
		if(category!=null && source !=null && sink != null){
			id = new ExtendedId(category.ordinal(), ((Node) source).getId(), sink.getExtendedId());
		}
	}

	@Override
	public PamLink getGroundingPamLink() {
		return groundingPamLink;
	}

	@Override
	public void setGroundingPamLink(PamLink l) {
		groundingPamLink = l;
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

}
