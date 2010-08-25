package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.List;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.ExpectationCodelet;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

//TODO have this implement excite strategy and use it as the LearnableActivatible's (parent of behavior) excite strategy
public class BasicReinforcementStrategy implements ReinforcementStrategy{

	private ReinforcementCurve curve = new SigmoidCurve(0.5, 0.5);
	
    public void reinforce(Behavior behavior, NodeStructure currentState, TaskSpawner ts){
    	List<ExpectationCodelet> expectationCodelets = behavior.getExpectationCodelets();
        if(!expectationCodelets.isEmpty()){
	        double fitness = 0;
	        for(ExpectationCodelet codelet: expectationCodelets){
	        	codelet.setCurrentState(currentState);
	        	ts.addTask(codelet);
	            fitness += codelet.getPerformance();
	            behavior.reinforceBaseLevelActivation(reinforcement(fitness, behavior.getBaseLevelActivation()));   
	        } 
        }
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
