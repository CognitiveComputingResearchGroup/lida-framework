/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;

public class CoalitionImpl extends ActivatibleImpl implements Coalition{
	
	private NodeStructure struct;	
	private double attentionCodeletActivation;

	public CoalitionImpl(NodeStructure content, double attnCodeActiv){
		struct = content;
		attentionCodeletActivation = attnCodeActiv;
		updateActivation();
	}

	private void updateActivation() {
		double sum = 0.0;
		for(Node n: struct.getNodes())
			sum += n.getActivation();
		for(Link l: struct.getLinks())
			sum += l.getActivation();
		setActivation((attentionCodeletActivation * sum) / (struct.getNodeCount() + struct.getLinkCount()));
	}

	@Override
	public BroadcastContent getContent() {
		return (BroadcastContent) struct;
	}
	
}//class