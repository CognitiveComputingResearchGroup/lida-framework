package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class CodeletActivatingContextImpl {	
	private NodeStructure nodeContext = new NodeStructureImpl();
	
	public CodeletActivatingContextImpl(){
		//Not all codelets will have contexts!
	}
	
	public CodeletActivatingContextImpl(NodeStructure ns){
		nodeContext = ns;
	}
	
	/**
	 * Iterate over the node context and return 
	 * a double.
	 * 
	 * @param whatIsPresent
	 * @return the proportion of the context
	 * that appears in whatIsPresent
	 */
	public double matchContext(NodeStructure whatIsPresent){
		int numMatches = 0;		
		//TODO: Implement	
		
		return 0.0;
	}//matchContext	
	
}//class Context

