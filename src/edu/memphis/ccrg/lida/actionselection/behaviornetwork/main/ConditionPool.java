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
 * @author UofM
 *
 */
public class ConditionPool implements BroadcastListener {

	private static final Logger logger = Logger.getLogger(ConditionPool.class
			.getCanonicalName());

	private Map<ExtendedId,Condition> buffer = new HashMap<ExtendedId,Condition>();

	/**
	 * Gets the condition map
	 * @return the {@link Map} of conditions
	 */
	public Map<ExtendedId, Condition> getConditionMap() {
		return buffer;
	}
	
	/**
	 * Gets conditions
	 * @return the {@link Condition}s in this {@link ConditionPool}
	 */
	public Collection<Condition> getConditions(){
		return buffer.values();
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(Map<ExtendedId, Condition> conditions) {
		this.buffer = conditions;
	}

	public void addCondition (ExtendedId id,Condition c){
		buffer.put(id,c);
		logger.log(Level.FINE,"Condition "+id+ " added.");
	}
	
	public Condition getCondition(ExtendedId id){
		return buffer.get(id);
	}

	public void decay(int ticks){
		for (Condition c: buffer.values()){
			c.decay(ticks);
		}
	}

	@Override
	public void learn(Coalition coalition) {
	}

	@Override
	public void receiveBroadcast(Coalition coalition) {
		NodeStructure ns = (NodeStructure) coalition.getContent();
		for (Node n: ns.getNodes()){
			Condition c = buffer.get(n.getExtendedId());
			if(c != null){
				c.setActivation(n.getActivation());
			}else{
				buffer.put(n.getExtendedId(), (Condition) n);
			}
		}		
	}
}
