/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;

/**
 * An encapsulation of perceptual content and an {@link AttentionCodelet}
 * {@link Coalition} objects are created and added to the
 * {@link GlobalWorkspace} by {@link AttentionCodelet} objects.
 * {@link Coalition} must overwrite correctly {@link Object#equals(Object)} and
 * {@link Object#hashCode()} methods.
 * 
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public interface Coalition extends Activatible {

	/**
	 * Gets the content of the coalition. 
	 * @return The {@link BroadcastContent} of the coalition
	 */
	public BroadcastContent getContent();
	
	/**
	 * Sets the content of the coalition.
	 * @param c {@link BroadcastContent}
	 */
	public void setContent(BroadcastContent c);

	/**
	 * Returns the {@link AttentionCodelet} that created this coalition
	 * @return The {@link AttentionCodelet} that help form this coalition
	 */
	public AttentionCodelet getCreatingAttentionCodelet();
	
	/**
	 * Sets the {@link AttentionCodelet} that created this coalition.
	 * @param c {@link AttentionCodelet} that created this coalition
	 */
	public void setCreatingAttentionCodelet(AttentionCodelet c);

	/**
	 * Gets the id.
	 * @return the unique id of the Coalition
	 */
	public int getId();
}