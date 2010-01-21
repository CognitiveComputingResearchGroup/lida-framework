package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Map;
import java.util.Queue;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface SchemeActivationBehavior {
	
	public void activateSchemesWithBroadcast(NodeStructure broadcast, Map<Linkable, Queue<Scheme>> schemeMap);
	
	public void setSchemeSelectionThreshold(double d);

}
