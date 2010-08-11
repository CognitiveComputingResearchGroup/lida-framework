/*
 * Selector.java
 *
 * Created on December 12, 2003, 2:52 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;

/**
 * Selector iterates and chooses competitor with max alpha
 *
 */
public class Selector{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    private final double TIE_BREAKER = 0.5;
    
    private List<BehaviorImpl> competitors = new ArrayList<BehaviorImpl>();
    
    public Selector() {       
    }
    
    public void addCompetitor(BehaviorImpl behavior){        
        if(behavior != null)  {
            competitors.add(behavior);                 
            logger.info("SELECTOR : ADDING Behavior " + behavior + 
                            " with " + behavior.getAlpha());
        }
    }    
    
    public void removeCompetitor(BehaviorImpl behavior){                
        if(behavior != null){
            competitors.remove(behavior);            
            logger.info("SELECTOR : REMOVING Behavior " + behavior + 
                            " with " + behavior.getAlpha());
        }
              
    }        
    
    public BehaviorImpl evaluateAbsoluteWinner(){
        BehaviorImpl winner = null;
        double maxAlpha = 0.0;
        
        logger.info("SELECTOR : SELECTING " + getNumberOfCompetitors());
        
        for(BehaviorImpl current: competitors){       
            if(current.getAlpha() > maxAlpha) {                    
                winner = current;
                maxAlpha = current.getAlpha();
            }else if(current.getAlpha() == maxAlpha && (Math.random() >= TIE_BREAKER)){   
                winner = current;                                    
            }            
        }        
        if(winner != null)
            logger.info("Winner: " + winner.toString() + ", alpha: " + winner.getAlpha());
        return winner;
    }
    
    public int getNumberOfCompetitors(){
        return competitors.size();
    }
    
    public void reset(){        
        logger.info("SELECTOR : RESET");
        competitors.clear();
    }

}
