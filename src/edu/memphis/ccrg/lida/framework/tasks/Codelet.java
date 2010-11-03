/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.tasks;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.Learnable;

/**
 * 
 * @author Javier Snaider
 * 
 */
public interface Codelet extends LidaTask, Learnable{
	
	/**
	 * @param module - module related to this codelet
	 */
	public abstract void setAssociatedModule(CodeletModuleUsage usage, LidaModule module);
	
}
