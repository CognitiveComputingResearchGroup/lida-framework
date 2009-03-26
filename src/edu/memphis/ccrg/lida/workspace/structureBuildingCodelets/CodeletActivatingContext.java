package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.HashSet;
import java.util.Set;
import edu.memphis.ccrg.lida.perception.interfaces.PamNode;

public class CodeletActivatingContext {	
	private Set<PamNode> nodeContext = new HashSet<PamNode>();
	
	public CodeletActivatingContext(){
		//Not all codelets will have contexts!
	}
	
	public CodeletActivatingContext(Set<PamNode> nodes){
		nodeContext = nodes;
	}
	
	/**
	 * Iterate over the node context and return 
	 * a double.
	 * 
	 * @param whatIsPresent
	 * @return the proportion of the context
	 * that appears in whatIsPresent
	 */
	public double matchContext(Set<PamNode> whatIsPresent){
		int numMatches = 0;		
		for(PamNode n: nodeContext)
			if(whatIsPresent.contains(n))
				numMatches++;		
		
		return numMatches / nodeContext.size();
	}//matchContext	
	
}//class Context

