/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.CognitiveContent;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;

/**
 * @author Sean Kugele
 * 
 */
public interface ElementFactory extends Factory {

    /**
     * @param type
     * @return
     */
    public CognitiveContent getCognitiveContent(String type);

    /**
     * @param type
     * @param params
     * @return
     */
    public CognitiveContent getCognitiveContent(String type,
            Map<String, ?> params);

    /**
     * @param type
     * @return
     */
    public CognitiveContentStructure getCognitiveContentStructure(String type);

    /**
     * @param type
     * @param params
     * @return
     */
    public CognitiveContentStructure getCognitiveContentStructure(String type,
            Map<String, ?> params);

}
