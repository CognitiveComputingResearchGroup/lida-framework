/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.pam.ns.PamListener;

public class MockPamListener implements PamListener {
	public NodeStructure ns;
	public Node n;
	public Link l;

	@Override
	public void receivePercept(NodeStructure ns) {
		this.ns = ns;
	}

	@Override
	public void receivePercept(Node n) {
		this.n = n;
	}

	@Override
	public void receivePercept(Link l) {
		this.l = l;
	}

}
