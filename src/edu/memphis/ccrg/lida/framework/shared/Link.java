/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

/**
 * A link connects two linkable objects. 
 * 
 * @author Javier Snaider, Ryan McCall
 *
 */
public interface Link extends Linkable, Activatible{
	
	public static final Link NULL_LINK=new LinkImpl(null,null,LinkCategory.None);

	public Linkable getSource();

	public Linkable getSink();
	
	public void setSource(Linkable source);

	public void setSink(Linkable sink);
	
	public LinkCategory getCategory();

	public void setCategory(LinkCategory type);

	public void setReferencedLink(Link l);

	public Link getReferencedLink();

}