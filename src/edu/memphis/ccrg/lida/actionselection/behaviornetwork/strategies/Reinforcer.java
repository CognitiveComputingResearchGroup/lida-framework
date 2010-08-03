/*
 * Reinforcer.java
 *
 * Sidney D'Mello
 * Created on January 6, 2004, 6:22 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.*;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.ExpectationCodelet;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class Reinforcer{
	
    private ReinforcementCurve curve = new SigmoidCurve(0.5, 0.5);
            
    public Reinforcer() {
    }
    
    public void reinforce(Behavior behavior, NodeStructure currentState){
    	List<ExpectationCodelet> expectationCodelets = behavior.getExpectationCodelets();
        if(!expectationCodelets.isEmpty()){
	        double fitness = 0;
	        for(ExpectationCodelet codelet: expectationCodelets){
	        	codelet.execute(currentState);
	            fitness += codelet.getPerformance();
	            behavior.reinforce(reinforcement(fitness, behavior.getBeta()));   
	        } 
        }
    }
    
    protected double reinforcement(double fitness, double beta){
        double reinforcement = 0;
        
        double oy = beta;        
        double ox = curve.getXIntercept(oy);
        double nx = ox + fitness;
        double ny = curve.getYIntercept(nx);
        
        reinforcement = ny;
                
        return reinforcement;
        
        //return curve.getYIntercept(curve.getXIntercept(beta) + fitness);
    }
    
    public ReinforcementCurve getReinforcementCurve(){
        return curve;
    }
    public void setReinforcementCurve(ReinforcementCurve reinforcementCurve){
        curve = reinforcementCurve;
    }
}
