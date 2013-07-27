/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks.ns;

import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.pam.ns.PamLink;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNS;

/**
 * A task to add a {@link PamLink} and its sink to the percept.
 * 
 * @author Ryan J. McCall
 * @see ExcitationTask creates this task
 * @see PropagationTask creates this task
 */
public class AddLinkToPerceptTask extends FrameworkTaskImpl {

    private final PerceptualAssociativeMemoryNS pam;
    private final Link link;

    /**
     * Default constructor
     * 
     * @param link
     *            {@link PamLink}
     * @param pam
     *            {@link PerceptualAssociativeMemoryNS}
     */
    public AddLinkToPerceptTask(Link link, PerceptualAssociativeMemoryNS pam) {
        this.pam = pam;
        this.link = link;
    }

    /**
     * Adds link's sink to the percept and tries to add the link as well then
     * finishes.
     */
    @Override
    protected void runThisFrameworkTask() {
        pam.addToPercept((Node) link.getSink());
        pam.addToPercept(link);
        cancel();
    }

}
