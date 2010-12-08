/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.pam.PamLink;

/**
 * A link connects two linkable objects. 
 * 
 * @author Javier Snaider, Ryan McCall
 *
 */
public interface Link extends Linkable, Activatible{
	
	public static final Link NULL_LINK=new LinkImpl(null,null,LinkCategory.None);

	/**
	 * One end of the link which provides activation to the sink.  
	 * @return source linkable
	 */
	public Linkable getSource();

	/**
	 * One end of the link which receives activation from the source.
	 * @return sink linkable
	 */
	public Linkable getSink();
	
	/**
	 * Set source linkable
	 */
	public void setSource(Linkable source);

	/**
	 * Set sink linkable
	 */
	public void setSink(Linkable sink);
	
	/**
	 * Get LinkCategory of this link
	 */
	public LinkCategory getCategory();

	/**
	 * Set LinkCategory
	 */
	public void setCategory(LinkCategory type);

	/**
	 * Set the grounding PamLink for this link.
	 */
	public void setGroundingPamLink(PamLink l);

	/**
	 * Get the grounding PamLink for this link.
	 */
	public PamLink getGroundingPamLink();

}