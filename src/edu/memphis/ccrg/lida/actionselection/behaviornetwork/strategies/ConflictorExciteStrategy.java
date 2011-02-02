package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.strategies.StrategyImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class ConflictorExciteStrategy extends StrategyImpl implements BehaviorExciteStrategy {

	private static final Logger logger = Logger.getLogger(ConflictorExciteStrategy.class.getCanonicalName());
	
	private final double DEFAULT_EXCITATION_FACTOR = 0.6;
	
	private double conflictorExcitationFactor = DEFAULT_EXCITATION_FACTOR;
	
	@Override
	public void init(Map<String, ?> parameters) {
		conflictorExcitationFactor = (Double) parameters.get("factor");
	}
	
	@Override
	public void exciteBehavior(Behavior sourceBehavior,
			Behavior recipientBehavior) {
		double inhibitionAmount = -(sourceBehavior.getActivation() * conflictorExcitationFactor)
		/ sourceBehavior.getContextSize();
		recipientBehavior.excite(inhibitionAmount);
		logger.log(Level.FINEST, sourceBehavior.getLabel() + " inhibits " + 
					recipientBehavior.getLabel() + " amount " + inhibitionAmount,
					LidaTaskManager.getCurrentTick());

	}

}
