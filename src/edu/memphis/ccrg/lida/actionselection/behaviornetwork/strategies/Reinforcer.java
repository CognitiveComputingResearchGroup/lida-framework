/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * Reinforcer.java
 *
 * Sidney D'Mello
 * Created on January 6, 2004, 6:22 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public interface Reinforcer{
    
    public abstract void reinforce(Behavior behavior, NodeStructure currentState);
    
    public abstract double reinforcement(double fitness, double beta);
    
    public abstract ReinforcementCurve getReinforcementCurve();
    
    public abstract void setReinforcementCurve(ReinforcementCurve reinforcementCurve);

	public abstract void reinforce(Behavior winner, NodeStructure currentState,
			TaskSpawner taskSpawner);
}
