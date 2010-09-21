/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

//TODO have this implement excite strategy and use it as the LearnableActivatible's (parent of behavior) excite strategy
public class BasicReinforcer implements Reinforcer{

	private ReinforcementCurve curve = new SigmoidReinforcementCurve(0.5, 0.5);
	
    public void reinforce(Behavior behavior, NodeStructure currentState, TaskSpawner ts){
//    	List<AttentionCodelet> expectationCodelets = behavior.getAttentionCodelets();
//        if(!expectationCodelets.isEmpty()){
//	        double fitness = 0;
//	        for(AttentionCodelet codelet: expectationCodelets){
//	        	((ExpectationCodelet) codelet).setCurrentState(currentState);
//	        	ts.addTask(codelet);
//	            fitness += ((ExpectationCodelet) codelet).getPerformance();
	          //  behavior.reinforceBaseLevelActivation(reinforcement(fitness, behavior.getBaseLevelActivation()));   
//	        } 
//        }
    }
    
    public double reinforcement(double fitness, double beta){
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

	@Override
	public void reinforce(Behavior behavior, NodeStructure currentState) {
		// TODO Auto-generated method stub
		
	}
	
}
