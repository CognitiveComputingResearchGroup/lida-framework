/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

/**
 * @author Javier Snaider
 *
 */
public class DynamicNodeStructure extends NodeStructureImpl {

	/**
	 * 
	 */
	public DynamicNodeStructure() {
	}

	/**
	 * @param defaultNode
	 * @param defaultLink
	 */
	public DynamicNodeStructure(String defaultNode, String defaultLink) {
		super(defaultNode, defaultLink);
	}

	/**
	 * @param oldGraph
	 */
	public DynamicNodeStructure(NodeStructure oldGraph) {
		//super(oldGraph);
	}

}
