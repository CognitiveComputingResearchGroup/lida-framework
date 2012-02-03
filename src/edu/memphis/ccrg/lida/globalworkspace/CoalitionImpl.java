/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.UnmodifiableNodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;

//TODO Make Coalition a Factory element.  then we can change the way a coalition calculates its activation
// and the type of content that it has.
/**
 * The default implementation of {@link Coalition}.  Wraps content entering the 
 * {@link GlobalWorkspace} to compete for consciousness. Extends {@link ActivatibleImpl}.
 * Contains reference to the {@link AttentionCodelet} that created it.
 * 
 * @author Ryan J. McCall
 * @see AttentionCodeletImpl
 */
public class CoalitionImpl extends ActivatibleImpl implements Coalition {

	private static long idCounter = 0;
	private long id;
	protected BroadcastContent content;
	protected double attentionCodeletActivation;
	protected AttentionCodelet attentionCodelet;
	
    public CoalitionImpl(){
    	super();
		id = idCounter++;
    }

    /**
     * Constructs a {@link CoalitionImpl} with specified content and sets activation to be equal to
     * the normalized sum of the activation of the {@link Linkable}s in the {@link NodeStructure}
     * times the activation of the creating {@link AttentionCodelet}
     * @param content conscious content
     * @param activation activation of creating attention codelet
     * @param codelet The {@link AttentionCodelet} that created this Coalition
     * @see AttentionCodeletImpl
     */
    public CoalitionImpl(NodeStructure content, double activation, AttentionCodelet codelet) {
    	this();
        this.content = (BroadcastContent) new UnmodifiableNodeStructureImpl(content,true);
        attentionCodeletActivation = activation;
        attentionCodelet = codelet;
        updateActivation();
    }

    /*
     * Calculates coalition's activation based on BroadcastContent and the attention codelet's activation
     */
    protected void updateActivation() {
    	//TODO fully encapsulate content!
        double sum = 0.0;
        NodeStructure ns = (NodeStructure) content;
        for (Linkable lnk : ns.getLinkables()) {
            sum += lnk.getActivation();
        }
        int contentSize = ns.getLinkableCount();
        if(contentSize != 0){
        	setActivation(attentionCodeletActivation * sum / contentSize);
        }
    }

    @Override
    public BroadcastContent getContent() {
        return content;
    }

    @Override
    public AttentionCodelet getCreatingAttentionCodelet() {
        return attentionCodelet;
    }

    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o){
    	if(o instanceof CoalitionImpl){
    		CoalitionImpl c = (CoalitionImpl) o;
    		return id == c.id;
    	}
    	return false;
    }
    
    @Override
    public int hashCode(){
    	return (int) id;
    }
    
}