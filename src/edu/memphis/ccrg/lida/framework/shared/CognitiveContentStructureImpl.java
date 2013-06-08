/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;

/**
 * @author Sean Kugele
 *
 */
public class CognitiveContentStructureImpl implements CognitiveContentStructure {

    @Override
    public void init(Map<String, ?> parameters) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> T getParam(String name, T defaultValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsParameter(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, ?> getParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContent addContent(CognitiveContent content) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<CognitiveContent> addContent(Collection<CognitiveContent> content) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeContent(CognitiveContent content) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean containsContent(CognitiveContent content) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void mergeWith(CognitiveContentStructure cs) {
        // TODO Auto-generated method stub

    }

    @Override
    public CognitiveContentStructure copy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContent getReducedRepresentation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void decay(long ticks) {
        // TODO Auto-generated method stub

    }

}
