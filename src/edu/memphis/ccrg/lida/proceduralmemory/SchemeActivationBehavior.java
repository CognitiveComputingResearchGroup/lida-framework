package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface SchemeActivationBehavior {
	
	public void activateSchemesWithBroadcast(NodeStructure broadcast, Map<Object, Set<Scheme>> schemeMap);
	
	public void setSchemeSelectionThreshold(double d);

}
