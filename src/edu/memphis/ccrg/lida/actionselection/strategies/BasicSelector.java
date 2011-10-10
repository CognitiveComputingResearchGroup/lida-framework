/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * Selector.java
 *
 * Created on December 12, 2003, 2:52 PM
 */

package edu.memphis.ccrg.lida.actionselection.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.strategies.StrategyImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;


/**
 * A Selector that chooses a behavior over threshold with maximum activation (alpha) among all behaviors.
 * @author Ryan J. McCall
 * @author Javier Snaider
 */
public class BasicSelector extends StrategyImpl implements Selector {

	private static Logger logger = Logger.getLogger(BasicSelector.class.getCanonicalName());

	public Behavior selectBehavior(Collection<Behavior> behaviors, double threshold) {
		double maxActivation = 0.0;
		List<Behavior> winners = new ArrayList<Behavior>();
		for (Behavior current : behaviors) {
			double currentActivation = current.getTotalActivation();
			if (currentActivation > threshold) {
				if (currentActivation > maxActivation) {
					winners.clear();
					winners.add(current);
					maxActivation = currentActivation;
				} else if (currentActivation == maxActivation) {
					winners.add(current);
				}
			}
		}
		Behavior winner = null;
		switch (winners.size()) {
			case 0:
				winner = null;
				break;
			case 1:
				winner = winners.get(0);
				logger.log(Level.FINER, "Winner: {1}  activation: {2}",
						new Object[]{TaskManager.getCurrentTick(),winner.getLabel(), maxActivation});
				break;
			default:
				winner = winners.get((int) (Math.random() * winners.size()));
				logger.log(Level.FINER, "Winner: {1}  activation: {2}",
						new Object[]{TaskManager.getCurrentTick(),winner.getLabel(), maxActivation});
		}
		return winner;
	}
}