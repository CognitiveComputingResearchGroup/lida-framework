/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;

/**
 * 
 * @author Ryan McCall, Javier Snaider
 */
public class LinkImpl extends ActivatibleImpl implements Link {

	private Linkable sink;
	private Linkable source;
	private String ids;
	private LinkCategory category;
	private Link referencedLink = null;

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
		ids = l.getIds();
		referencedLink = l.getReferencedLink();
		updateIds();
	}

	public LinkImpl() {
	}

	public LinkImpl copy() {
		return new LinkImpl(this);
	}

	/**
	 * This method compares this LinkImp with any kind of Link. Two Links are
	 * equals if and only if their sources and sinks are equals and are Links of
	 * the same category.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Link)) {
			return false;
		}
		Link other = (Link) obj;

		if (other.getCategory() != category) {
			return false;
		}

		if (ids.equals(other.getIds())) {
			return true;
		}
		return false;
	}

	public String getIds() {
		return ids;
	}

	public String getLabel() {
		return "Link: " + ids;
	}

	public Linkable getSink() {
		return sink;
	}

	public Linkable getSource() {
		return source;
	}

	public LinkCategory getCategory() {
		return category;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + ((source == null) ? 0 : source.hashCode())
				+ ((sink == null) ? 0 : sink.hashCode());
		hash = hash * 31 + (category == null ? 0 : category.hashCode());
		return hash;
	}

//	public void setIds(String id) {
//		//The ids can not be set
//	}
	
	public String getId(){
		return ids;
	}

	public void setSink(Linkable sink) {
		this.sink = sink;
		updateIds();
	}

	public void setSource(Linkable source) {
		this.source = source;
		updateIds();
	}

	public void setCategory(LinkCategory category) {
		this.category = category;
		updateIds();
	}

	@Override
	public String toString() {
		return getLabel();
	}

	private void updateIds() {
		ids = "L(" + ((source!=null)?source.getIds():"") + ":" + ((sink!=null)?sink.getIds():"") + ":" + category + ")";
	}

	public Link getReferencedLink() {
		return referencedLink;
	}

	public void setReferencedLink(Link l) {
		referencedLink = l;
	}
	
	public void init(Map<String, Object> params) {
	}
	

}
