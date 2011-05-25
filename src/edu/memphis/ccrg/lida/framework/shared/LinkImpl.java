/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
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
	
	protected Map<String, ?> params;
	
	/**
	 * Default constructor
	 */
	public LinkImpl() {
		super();
	}
	
	/**
	 * @param source source {@link Node}
	 * @param sink sink {@link Linkable}
	 * @param category link's {@link LinkCategory}
	 */
	public LinkImpl(Node source, Linkable sink, LinkCategory category) {
		this.source = source;
		if(source.equals(sink)){
			throw new IllegalArgumentException("Cannot create a link with the same source and sink.");
		}
		if(sink.getExtendedId().isComplexLink()){
			throw new IllegalArgumentException("Sink cannot be a complex link. Must be a node or simple link.");
		}
		this.sink = sink;
		this.category = category;
		updateExtendedId();
	}

	/**
	 * Copy constructor
	 * @param l source {@link LinkImpl}
	 */
	public LinkImpl(LinkImpl l) {
		sink = l.getSink();
		source = l.getSource();
		category = l.getCategory();
		groundingPamLink = l.getGroundingPamLink();
		params = l.params;
		updateExtendedId();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModule#init(java.util.Properties)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.params = params;
		init();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModule#init()
	 */
	@Override
	public void init() {
	}
	
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (params != null) {
			value = params.get(name);
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
			logger.log(Level.FINEST, "updated extended id", TaskManager.getCurrentTick());
			extendedId = new ExtendedId(source.getId(), sink.getExtendedId(), category.getId());
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
		if(sink == null){
			logger.log(Level.WARNING, "Cannot set sink to null", TaskManager.getCurrentTick());
			return;
		}
		
		if(sink.equals(source)){
			logger.log(Level.WARNING, "Cannot set sink to same Linkable as source", TaskManager.getCurrentTick());
			throw new IllegalArgumentException("Cannot create a link with the same source and sink.");
		}
		if(sink.getExtendedId().isComplexLink()){
			throw new IllegalArgumentException("Sink cannot be a complex link. Must be a node or simple link.");
		}
		this.sink = sink;
		updateExtendedId();
	}

	@Override
	public void setSource(Node source) {
		if(source == null){
			logger.log(Level.WARNING, "Cannot set source to null", TaskManager.getCurrentTick());
			return;
		}
		if(source.equals(sink)){
			logger.log(Level.WARNING, "Cannot set source to same Linkable as sink", TaskManager.getCurrentTick());
			throw new IllegalArgumentException("Cannot create a link with the same source and sink.");
		}
		
		this.source = source;
		updateExtendedId();
	}

	@Override
	public void setCategory(LinkCategory category) {
		if(category == null){
			logger.log(Level.WARNING, "Cannot set category to null", TaskManager.getCurrentTick());
			return;
		}
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
	public boolean isSimpleLink(){
		return extendedId.isSimpleLink();
	}

	/**
	 * Updates the values of this LinkImpl based on the passed in Link.  
	 * Link must be a LinkImpl.
	 * Does not copy superclass attributes, e.g. ActivatibleImpl, only those of this class.
	 */
	@Override
	public void updateLinkValues(Link link) {
		if(link instanceof LinkImpl){
			LinkImpl other = (LinkImpl) link;
			this.extendedId = other.extendedId;
			this.category = other.category;
			this.groundingPamLink = other.groundingPamLink;
			this.params = other.params;
		}
	}

}
