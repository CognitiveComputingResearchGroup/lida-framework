/*
 * Reinforcer.java
 *
 * Sidney D'Mello
 * Created on January 6, 2004, 6:22 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public interface ReinforcementStrategy{
    
    public abstract void reinforce(Behavior behavior, NodeStructure currentState);
    
    public abstract double reinforcement(double fitness, double beta);
    
    public abstract ReinforcementCurve getReinforcementCurve();
    
    public abstract void setReinforcementCurve(ReinforcementCurve reinforcementCurve);

	public abstract void reinforce(Behavior winner, NodeStructure currentState,
			TaskSpawner taskSpawner);
}
