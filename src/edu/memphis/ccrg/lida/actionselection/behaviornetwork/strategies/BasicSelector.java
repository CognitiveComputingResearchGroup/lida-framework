/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * Selector.java
 *
 * Created on December 12, 2003, 2:52 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * Selector iterates and chooses competitor with max alpha
 *
 */
public class BasicSelector implements Selector{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    private final double TIE_BREAKER = 0.5;
    
//    stochastic in behavior net + drives to explore novel things
    // have a parameter which at 1.0 gives deterministic action selection.  
    // If 0.0 then completely random
    
    public BasicSelector() {       
    }
    
    public Behavior selectSingleBehavior(Collection<Behavior> candidateBehaviors, double candidateThreshold){
        double maxActivation = 0.0;
        Behavior winner = null;
        for(Behavior current: candidateBehaviors){
    		double currentActivation = current.getTotalActivation();
    		if(currentActivation >= candidateThreshold){
                if(currentActivation > maxActivation) {                    
                    winner = current;
                    maxActivation = currentActivation;
                }else if(currentActivation == maxActivation){   
                	if(Math.random() >= TIE_BREAKER)
                		winner = current;                                    
                }
    		}
    	}
        if(winner != null)
            logger.log(Level.FINE, "Winner: " + winner.getLabel() + ", activ: " + maxActivation);
        return winner;
    }
 
}
