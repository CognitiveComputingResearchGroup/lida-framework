/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Node;

public interface Stream extends Serializable {

	//TOOD move to procedural memory package
	public long getId();
    public String getName();
    
    public boolean addBehavior(Behavior behavior);
	public void removeBehavior(Behavior behavior);
    public Collection<Behavior> getBehaviors();
	public int getBehaviorCount();
	
	public void addSuccessorLink(Behavior predecessor, Behavior successor, Node commonNode);
	public void removeSuccessorLink(Behavior predecessor, Behavior successor);
	public Set<Behavior> getSuccessors(Behavior behavior);
}
