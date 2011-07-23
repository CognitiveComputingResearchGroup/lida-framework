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
 * Coaltions are created and added to the {@link GlobalWorkspace} by
 * {@link AttentionCodelet} objects. {@link Coalition} must overwrite correctly
 * {@link Object#equals(Object)} and {@link Object#hashCode()} methods.
 * 
 * @author Javier Snaider
 * @author ryanjmccall
 */
public interface Coalition extends Activatible {

	/**
	 * Returns the content of the coalition
	 * 
	 * @return The Content of the coalition
	 */
	public BroadcastContent getContent();

	/**
	 * Returns the attention codelet that creates this coalition
	 * 
	 * @return The attention codelet which creates this coalition
	 */
	public AttentionCodelet getCreatingAttentionCodelet();

	/**
	 * Returns id
	 * 
	 * @return unique id
	 */
	public long getId();
}
