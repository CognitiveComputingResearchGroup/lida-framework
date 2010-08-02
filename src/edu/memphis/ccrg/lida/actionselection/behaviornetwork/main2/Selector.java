/*
 * Selector.java
 *
 * Created on December 12, 2003, 2:52 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2;

import java.util.*;
import java.util.logging.Logger;

/**
 * Selector iterates and chooses competitor with max alpha
 *
 */
public class Selector 
{    
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    private final static double TIE_BREAKER = 0.5;
    
    private LinkedList competitors;
    
    public Selector() 
    {
        competitors = new LinkedList();        
    }
    
    public void addCompetitor(Behavior behavior) throws NullPointerException
    {        
        if(behavior != null)  
        {
            competitors.add(behavior);                 
            logger.info("SELECTOR : ADDING Behavior " + behavior + 
                            " with " + behavior.getAlpha());
        }
        else
            throw new NullPointerException();
    }    
    
    public void removeCompetitor(Behavior behavior)
    {                
        if(behavior != null)
        {
            competitors.remove(behavior);            
            logger.info("SELECTOR : REMOVING Behavior " + behavior + 
                            " with " + behavior.getAlpha());
        }
        else
            throw new NullPointerException();                
    }        
    
    public Behavior evaluateAbsoluteWinner()
    {
        Behavior winner = null;
        
        logger.info("SELECTOR : SELECTING " + countCompetitors());
        
        if (!competitors.isEmpty())
        {        
            winner = (Behavior)competitors.getFirst();
        
            ListIterator li = competitors.listIterator(1);
            while(li.hasNext())
            {                
                Behavior current = (Behavior)li.next();                
                if(current.getAlpha() > winner.getAlpha()) 
                {                    
                    winner = current;            
                }
            
                else if(current.getAlpha() == winner.getAlpha())
                {   
                    logger.info("SELECTOR : TIE " + current.getName() + 
                                    " and " + winner.getName() + 
                                    " with " + winner.getAlpha());
                    
                    if(Math.random() >= TIE_BREAKER)
                        winner = current;                    
                }                
            }            
        }        
        if(winner != null)
            logger.info("SELECTOR : WINNER " + winner.getName() + 
                            " with " + winner.getAlpha());
        return winner;
    }
    
    public int countCompetitors()
    {
        return competitors.size();
    }
    
    public void reset()
    {        
        logger.info("SELECTOR : RESET");
        competitors.clear();
    }
    
    public Behavior evaluateWheelOfFortuneWinner()
    {
        Behavior winner = null;
        
        return winner;       
    }
}
