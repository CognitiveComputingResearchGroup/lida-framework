package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.strategies.StrategyImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Strategy that determines how a successor is excited.
 * @author Ryan J. McCall
 *
 */
public class SuccessorExciteStrategy extends StrategyImpl implements BehaviorExciteStrategy {
	
	private static final Logger logger = Logger.getLogger(SuccessorExciteStrategy.class.getCanonicalName());
	
	private final double DEFAULT_EXCITATION_FACTOR = 0.6;
	
	private double successorExcitationFactor = DEFAULT_EXCITATION_FACTOR;
	
	@Override
	public void init(Map<String, ?> parameters) {
		successorExcitationFactor = (Double) parameters.get("factor");
	}

	@Override
	public void exciteBehavior(Behavior sourceBehavior, Behavior recipientBehavior) {
		double amount = sourceBehavior.getActivation() * successorExcitationFactor
						/ recipientBehavior.getUnsatisfiedContextCount();
		recipientBehavior.excite(amount);
		logger.log(Level.FINEST, sourceBehavior.getLabel() + " sends " + amount + 
				" to " + recipientBehavior.getLabel(), LidaTaskManager.getCurrentTick());
//		double amount = behavior.getActivation() * successorExcitationFactor
//		/ successor.getUnsatisfiedContextCount();
//successor.excite(amount);
	}

}
