package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * A strategy for choosing which behavior to execute
 * @author ryanjmccall
 *
 */
public interface Selector {

	/**
	 * Add a behavior to be considered
	 * @param b
	 */
	public abstract void addCompetitor(Behavior b);

	/**
	 * Select a single behavior as the current winner
	 * @return
	 */
	public abstract Behavior selectBehavior();

}
