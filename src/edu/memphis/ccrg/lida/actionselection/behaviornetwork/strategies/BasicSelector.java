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
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Stream;

/**
 * Selector iterates and chooses competitor with max alpha
 *
 */
public class BasicSelector implements Selector{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    private final double TIE_BREAKER = 0.5;
    
    public BasicSelector() {       
    }
    
    public Behavior selectBehavior(Collection<Stream> candidates){
        double maxActivation = 0.0;
        Behavior winner = null;
        for(Stream s: candidates){
        	for(Behavior current: s.getBehaviors()){
        		double currentActivation = current.getTotalActivation();
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
