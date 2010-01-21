package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class BasicSchemeActivationBehavior implements SchemeActivationBehavior {
	
	ProceduralMemory pm;
	private double schemeSelectionThreshold = 0.6;
	
	public BasicSchemeActivationBehavior(ProceduralMemory pm){
		this.pm = pm;
	}

	public void activateSchemesWithBroadcast(NodeStructure broadcast, Map<Linkable, Queue<Scheme>> schemeMap) {
		Collection<Node> nodes = broadcast.getNodes();
		Collection<Link> links = broadcast.getLinks();	
		for(Node n: nodes)
			auxActivateSchemes(n, schemeMap);
		for(Link l: links)
			auxActivateSchemes(l, schemeMap);

	}
	
	public void auxActivateSchemes(Linkable l, Map<Linkable, Queue<Scheme>> schemeMap){
		if(schemeMap.containsKey(l)){
			Queue<Scheme> schemes = schemeMap.get(l);
			for(Scheme s: schemes){
				int contextCount = s.getContext().getNodeCount();
				s.excite(1.0 / (contextCount * 1.0));
				if(s.getActivation() > schemeSelectionThreshold){
					//Copy?
					pm.sendInstantiatedScheme(s);
				}	
			}//for
		}
	}//method

	public void setSchemeSelectionThreshold(double d) {
		schemeSelectionThreshold = d;
	}

}
