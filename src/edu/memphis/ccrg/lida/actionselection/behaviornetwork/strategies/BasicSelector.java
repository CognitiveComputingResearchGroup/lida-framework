/*
 * Selector.java
 *
 * Created on December 12, 2003, 2:52 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * Selector iterates and chooses competitor with max alpha
 *
 */
public class BasicSelector implements Selector{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    private final double TIE_BREAKER = 0.5;
    
    private List<Behavior> competitors = new ArrayList<Behavior>();
    
    public BasicSelector() {       
    }
    
    public void addCompetitor(Behavior behavior){        
        if(behavior != null)  {
            competitors.add(behavior);                 
            logger.info("SELECTOR : ADDING Behavior " + behavior + 
                            " with " + behavior.getTotalActivation());
        }
    }    
    
    public void removeCompetitor(Behavior behavior){                
        if(behavior != null){
            competitors.remove(behavior);            
            logger.info("SELECTOR : REMOVING Behavior " + behavior + 
                            " with " + behavior.getTotalActivation());
        }
              
    }        
    
    public Behavior selectBehavior(){
        logger.info("SELECTING " + getNumberOfCompetitors());
        double maxActivation = 0.0;
        Behavior winner = null;
        for(Behavior current: competitors){ 
        	double currentActivation = current.getTotalActivation();
            if(currentActivation > maxActivation) {                    
                winner = current;
                maxActivation = currentActivation;
            }else if(currentActivation == maxActivation && (Math.random() >= TIE_BREAKER)){   
                winner = current;                                    
            }            
        }        
        if(winner != null)
            logger.info("Winner: " + winner.toString() + ", alpha: " + winner.getTotalActivation());
        competitors.clear();
        return winner;
    }
    
    public int getNumberOfCompetitors(){
        return competitors.size();
    }

}
