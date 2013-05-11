/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.initialization.Initializable;

/**
 * A CognitiveContentStructure contains a collection of {@link CognitiveContent}
 * . It is used as the main conceptual representation among LIDA modules.
 * 
 * @author Sean Kugele
 * @author Javier Snaider
 * 
 */
public interface CognitiveContentStructure extends Initializable {

    /**
     * Adds {@link CognitiveContent} to this CognitiveContentStructure.
     * 
     * @param content
     *            the {@link CognitiveContent} to add
     * @return the added {@link CognitiveContent}
     */
    public CognitiveContent addContent(CognitiveContent content);

    /**
     * Adds the {@link CognitiveContent} contained in the supplied Collection to
     * this CognitiveContentStructure.
     * 
     * @param content
     *            a Collection containing the {@link CognitiveContent} to add
     * @return a Collection containing the added {@link CognitiveContent}
     */
    public Collection<CognitiveContent> addContent(Collection<CognitiveContent> content);

    /**
     * Removes the specified {@link CognitiveContent} if present.
     * 
     * @param content
     *            {@link CognitiveContent} to remove.
     */
    public void removeContent(CognitiveContent content);

    /**
     * Returns whether this CognitiveContentStructure contains the specified
     * {@link CognitiveContent}.
     * 
     * @param content
     *            {@link CognitiveContent} to be checked for.
     * @return true if contains {@link CognitiveContent} with the same id
     */
    public boolean containsContent(CognitiveContent content);

    /**
     * Merges specified CognitiveContentStructure into this one.
     * 
     * @param cs
     *            CognitiveContentStructure
     */
    public void mergeWith(CognitiveContentStructure cs);

    /**
     * Returns a deep copy of this CognitiveContentStructure.
     * 
     * @return CognitiveContentStructure
     */
    public CognitiveContentStructure copy();

    /**
     * Returns the reduced representation of this CognitiveContentStructure.
     * 
     * @return the {@link CognitiveContent} that represents a reduced
     *         representation of this CognitiveContentStructure
     */
    public CognitiveContent getReducedRepresentation();

    /**
     * Decays the {@link CognitiveContent} contained in this
     * {@link CognitiveContentStructure}.
     * 
     * @param ticks
     *            the number of ticks to decay for.
     */
    public void decay(long ticks);

}