/*
 * Reinforcer.java
 *
 * Sidney D'Mello
 * Created on January 6, 2004, 6:22 PM
 */

package edu.memphis.ccrg.lida.actionselection2.engine;

import java.util.*;

import edu.memphis.ccrg.lida.actionselection2.util.ExpectationCodelet;

public class Reinforcer 
{    
    private ReinforcementCurve curve;
            
    public Reinforcer() 
    {
        //curve = new SigmoidCurve(Net.
    }
    
    public void reinforce(Behavior behavior) throws NullPointerException
    {                
        if(behavior != null)
        {
            if(!behavior.getExpectationCodelets().isEmpty())
            {
                double fitness = 0;
                Iterator li = (Iterator)behavior.getExpectationCodelets().iterator();
                while(li.hasNext())
                {

                    ExpectationCodelet expectationCodelet = (ExpectationCodelet)li.next();
                    expectationCodelet.execute();
                        
                    fitness += expectationCodelet.getPerformance();

                    behavior.reinforce(reinforcement(fitness, behavior.getBeta()));
                }
            }
        }
        else
            throw new NullPointerException();         
    }    
    
    protected double reinforcement(double fitness, double beta)
    {
        double reinforcement = 0;
        
        double oy = beta;        
        double ox = curve.getXIntercept(oy);
        double nx = ox + fitness;
        double ny = curve.getYIntercept(nx);
        
        reinforcement = ny;
                
        return reinforcement;
        
        //return curve.getYIntercept(curve.getXIntercept(beta) + fitness);
    }
    
    public ReinforcementCurve getReinforcementCurve()
    {
        return curve;
    }
    
    public void setReinforcementCurve(ReinforcementCurve reinforcementCurve)
    {
        if(reinforcementCurve != null)
            this.curve = reinforcementCurve;
        else
            throw new NullPointerException();
    }
}
