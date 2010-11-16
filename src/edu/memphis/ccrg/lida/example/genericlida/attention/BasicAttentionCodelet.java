/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.example.genericlida.attention;

import edu.memphis.ccrg.lida.attention.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;

public class BasicAttentionCodelet extends AttentionCodeletImpl {

	public BasicAttentionCodelet(){
		super();
	}
	
	private static final double THRESHOLD = 0.5;

	protected void runThisLidaTask() {
		NodeStructure ns = new NodeStructureImpl();
		NodeStructure csmContent = (NodeStructure) currentSituationalModel.getModuleContent();
		for (Node n : csmContent.getNodes()) {
			if (n.getActivation()>=THRESHOLD){
				ns.addNode(n);
			}
		}
		
		if (ns != null){
			globalWorkspace.addCoalition(new CoalitionImpl(ns,
					getActivation()));
		}
	}
}
