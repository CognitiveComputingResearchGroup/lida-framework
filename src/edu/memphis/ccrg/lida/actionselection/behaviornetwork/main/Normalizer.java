/*
 * Normalizer.java
 *
 * Sidney D'Mello
 * Created on February 28, 2004, 3:48 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Logger;

public class Normalizer 
{         
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Normalizer");
    private LinkedList streams;
    
    private int count;
    private double sum;
    private double min;
    private double max;        
    private double average;
    
        
    public Normalizer(LinkedList streams) 
    {        
        if(streams != null)
        {
            this.streams = streams;
            reset();
        }
        else
            throw new NullPointerException();                
    }
    

    public void scan() throws NullPointerException
    {                                
        reset();

        if(!streams.isEmpty())
        {
            Stream stream = (Stream)streams.getFirst();
            if(!stream.getBehaviors().isEmpty())
            {
                min = ((Behavior)stream.getBehaviors().getFirst()).getAlpha();
                max = min;
            }
        }
                
        Iterator i = streams.iterator();        
        while(i.hasNext())
        {
            Iterator j = ((Stream)i.next()).getBehaviors().iterator();
            while(j.hasNext())
            {
                Behavior behavior = (Behavior)j.next();                                  
                double activation = behavior.getAlpha();

                count ++;
                sum += activation;
                
                if(min > activation)
                    min = activation;

                if(max < activation)
                    max = activation;                
            }
        }        
        if(count != 0)
            average = sum / count;                
        
        logger.info("SCAN: ");
        logger.info("\t#Behaviors:          " + getBehaviorCount());
        logger.info("\tMin Activation:      " + getMinimumActivation());
        logger.info("\tMax Activation:      " + getMaximumActivation());
        logger.info("\tTotal Activation:    " + getTotalActivation());
        logger.info("\tAverage Activation:  " + getAverageActivation());
        logger.info("");                
    }
    
    public void normalize()
    {
        report("\nNORMALIZER : ACTIVATION LEVELS BEFORE NORMALIZATION");
        
        double n_sum = BehaviorNetworkImpl.getPi() * count;
        
        Iterator i = streams.iterator();        
        while(i.hasNext())
        {
            Iterator j = ((Stream)i.next()).getBehaviors().iterator();
            while(j.hasNext())
            {
                Behavior behavior = (Behavior)j.next();                                
                double activation = behavior.getAlpha();
                
                double strength = activation / sum;
                double n_activation = strength * n_sum;
                
                behavior.decay(n_activation);
                /*
                double change = n_activation - activation;                
                
                if(change > 0)
                    behavior.excite(change);
                else if (change < 0)
                    behavior.inhibit(change);
                 */
            }
        }
        report("\nNORMALIZER : ACTIVATION LEVELS AFTER NORMALIZATION");
    }
    
    public void reset()
    {                
        count = 0;
        sum = 0;
        min = 0;
        max = 0;
        average = 0;        
    }
    
    public int getBehaviorCount()
    {
        return count;
    }
    
    public double getTotalActivation()
    {
        return sum;
    }
    
    public double getMinimumActivation()
    {
        return min;
    }
    
    public double getMaximumActivation()
    {
        return max;
    }    
    
    public double getAverageActivation()
    {
        return average;
    }    
    
    private void report(String header)
    {
        logger.info(header);
        Iterator i = streams.iterator();        
        while(i.hasNext())
        {
            Iterator j = ((Stream)i.next()).getBehaviors().iterator();
            while(j.hasNext())
            {
                Behavior behavior = (Behavior)j.next();
                logger.info("\t" + behavior.getName() + "\t" + behavior.getAlpha());
            }
        }
    }
}
