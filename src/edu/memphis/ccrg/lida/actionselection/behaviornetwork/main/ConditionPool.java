package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * TODO
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public class ConditionPool implements BroadcastListener {

	private static final Logger logger = Logger.getLogger(ConditionPool.class
			.getCanonicalName());

	private Map<ExtendedId,Condition> conditionPool = new HashMap<ExtendedId,Condition>();

	/**
	 * Gets the condition map
	 * @return the {@link Map} of conditions
	 */
	public Map<ExtendedId, Condition> getConditionMap() {
		return conditionPool;
	}
	
	/**
	 * Gets conditions
	 * @return the {@link Condition}s in this {@link ConditionPool}
	 */
	public Collection<Condition> getConditions(){
		return conditionPool.values();
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(Map<ExtendedId, Condition> conditions) {
		this.conditionPool = conditions;
	}

	public void addCondition (ExtendedId id,Condition c){
		conditionPool.put(id,c);
		logger.log(Level.FINE,"Condition "+id+ " added.");
	}
	
	public Condition getCondition(ExtendedId id){
		return conditionPool.get(id);
	}

	public void decay(int ticks){
		for (Condition c: conditionPool.values()){
			c.decay(ticks);
		}
	}

	@Override
	public void receiveBroadcast(Coalition coalition) {
		NodeStructure ns = (NodeStructure) coalition.getContent();
		for (Node n: ns.getNodes()){
			Condition c = conditionPool.get(n.getExtendedId());
			if(c != null){
				c.setActivation(n.getActivation());
			}else{
				conditionPool.put(n.getExtendedId(), (Condition) n);
			}
		}		
	}

	@Override
	public void learn(Coalition coalition) {
	}
}
