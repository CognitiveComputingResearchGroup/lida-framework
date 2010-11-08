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
		ids = l.getIds();
		referencedLink = l.getReferencedLink();
		updateIds();
	}

	public LinkImpl copy() {
		return new LinkImpl(this);
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
		int hash = 31 + ((source == null) ? 0 : source.hashCode()) * 29;		   
		hash = hash * 37 + ((sink == null) ? 0 : sink.hashCode()) ;
		return hash;
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
		
		Linkable otherSource = other.getSource();
		if(otherSource instanceof Node){
			if(this.source instanceof Node){
				if(((Node) source).getId() != ((Node) otherSource).getId()){
					return false;
				}
			}else{ //Sources are different objects
				return false;
			}
		}else{ //must be dealing with a link
			if(this.source instanceof Link){
				if(source.getIds() != otherSource.getIds()){
					return false;
				}
			}else{ //Sources are different objects
				return false;
			}
		}
		
		Linkable otherSink = other.getSink();	
		if(otherSink instanceof Node){
			if(this.sink instanceof Node){
				if(((Node) sink).getId() != ((Node) otherSink).getId()){
					return false;
				}
			}else{ //Sinks are different objects
				return false;
			}
		}else{
			if(this.sink instanceof Link){
				if(sink.getIds() != otherSink.getIds()){
					return false;
				}
			}else{ //Sinks are different objects
				return false;
			}
		}
				
		return true;
	}
	
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
