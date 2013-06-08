/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

/**
 * @author Sean Kugele
 * @param <T>
 *            type of the factory to be initialized
 * 
 */
public interface FactoryInitializer<T extends InitializableFactory> {

    /**
     * Initializes the factory.
     * 
     * If the factory has dependencies on other factories, then those factories
     * should be initialized prior to calling this method.
     */
    public void init();

}
