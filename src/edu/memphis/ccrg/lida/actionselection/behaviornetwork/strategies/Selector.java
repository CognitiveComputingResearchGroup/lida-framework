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
public class Selector{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    private final double TIE_BREAKER = 0.5;
    
    private List<Behavior> competitors = new ArrayList<Behavior>();
    
    public Selector() {       
    }
    
    public void addCompetitor(Behavior behavior){        
        if(behavior != null)  {
            competitors.add(behavior);                 
            logger.info("SELECTOR : ADDING Behavior " + behavior + 
                            " with " + behavior.getAlpha());
        }
    }    
    
    public void removeCompetitor(Behavior behavior){                
        if(behavior != null){
            competitors.remove(behavior);            
            logger.info("SELECTOR : REMOVING Behavior " + behavior + 
                            " with " + behavior.getAlpha());
        }
              
    }        
    
    public Behavior evaluateAbsoluteWinner(){
        Behavior winner = null;
        double maxAlpha = 0.0;
        
        logger.info("SELECTOR : SELECTING " + getNumberOfCompetitors());
        
        for(Behavior current: competitors){       
            if(current.getAlpha() > maxAlpha) {                    
                winner = current;
                maxAlpha = current.getAlpha();
            }else if(current.getAlpha() == maxAlpha && (Math.random() >= TIE_BREAKER)){   
                winner = current;                                    
            }            
        }        
        if(winner != null)
            logger.info("Winner: " + winner.toString() + ", alpha: " + winner.getAlpha());
        competitors.clear();
        return winner;
    }
    
    public int getNumberOfCompetitors(){
        return competitors.size();
    }

}
