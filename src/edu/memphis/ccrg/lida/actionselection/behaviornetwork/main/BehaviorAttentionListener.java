package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Node;

public interface BehaviorAttentionListener {
	
	public void receiveBehaviorAttentionContent(Set<Node> addSet, Set<Node> deleteSet);

}
