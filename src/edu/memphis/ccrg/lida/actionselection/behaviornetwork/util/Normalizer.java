/*
 * Normalizer.java
 *
 * Sidney D'Mello
 * Created on February 28, 2004, 3:48 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Stream;

/**
 * This class basically performs statistics on a list of streams
 * @author Sidney D'Mello, ryanjmccall
 *
 */
public class Normalizer{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Normalizer");
    private Queue<Stream> streams;
    
    private int count;
    private double sum;
    private double min;
    private double max;        
    private double average;
    
    public Normalizer(Queue<Stream> streams){        
        this.streams = streams;
        reset();            
    }
    

    public void scan() throws NullPointerException
    {                                
        reset();

        if(!streams.isEmpty()){
            Stream stream = (Stream)streams.peek();
            if(!stream.getBehaviors().isEmpty())
            {
                min = ((BehaviorImpl)stream.getBehaviors().get(0)).getAlpha();
                max = min;
            }
        }
                
        for(Stream s: streams){
            for(BehaviorImpl behavior: s.getBehaviors()){   
            	count ++;
                double activation = behavior.getAlpha();
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
    
    public void normalize(double pi)
    {
        report("\nNORMALIZER : ACTIVATION LEVELS BEFORE NORMALIZATION");
        
        double n_sum = pi * count;
        
        for(Stream s: streams){
            for(BehaviorImpl behavior: s.getBehaviors()){   
            	
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
    
    public void reset(){                
        count = 0;
        sum = 0;
        min = 0;
        max = 0;
        average = 0;        
    }
    
    public int getBehaviorCount(){
        return count;
    }
    public double getTotalActivation(){
        return sum;
    }
    public double getMinimumActivation(){
        return min;
    }
    public double getMaximumActivation(){
        return max;
    }    
    
    public double getAverageActivation(){
        return average;
    }    
 
    private void report(String header){
        logger.info(header);
        for(Stream s: streams)
            for(BehaviorImpl behavior: s.getBehaviors()) 
                logger.info("\t" + behavior.toString() + "\t" + behavior.getAlpha());
    }//method

}//class


