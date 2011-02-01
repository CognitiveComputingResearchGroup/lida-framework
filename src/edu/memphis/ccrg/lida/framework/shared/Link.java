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
 * A link connects two Linkable objects. 
 * 
 * @author Javier Snaider, Ryan J. McCall
 */
public interface Link extends Linkable, Activatible{
	
	/**
	 * One end of the link which provides activation to the sink.  
	 * @return source linkable
	 */
	public Node getSource();

	/**
	 * One end of the link which receives activation from the source.
	 * @return sink linkable
	 */
	public Linkable getSink();
	
	/**
	 * Set source linkable.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(Node source);

	/**
	 * Set sink linkable.
	 * 
	 * @param sink
	 *            the new sink
	 */
	public void setSink(Linkable sink);
	
	/**
	 * Get LinkCategory of this link.
	 * 
	 * @return the category
	 */
	public LinkCategory getCategory();

	/**
	 * Set LinkCategory.
	 * 
	 * @param type
	 *            the new category
	 */
	public void setCategory(LinkCategory type);
	
	/**
	 * Returns factoryLinkType.
	 * 
	 * @return name of the Link type in the NodeFactory
	 */
	public String getFactoryLinkType();

	/**
	 * Set the grounding PamLink for this link.
	 * 
	 * @param l
	 *            the new grounding pam link
	 */
	public void setGroundingPamLink(PamLink l);

	/**
	 * Get the grounding PamLink for this link.
	 * 
	 * @return the grounding pam link
	 */
	public PamLink getGroundingPamLink();

}