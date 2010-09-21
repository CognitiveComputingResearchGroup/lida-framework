/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * Factory for Lida objects
 * @author Javier Snaider
 *
 */
public interface LidaFactory {

	public abstract Lida getLida(Properties properties);

}