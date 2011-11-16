package edu.memphis.ccrg.lida.framework.shared;

/**
 * A marker of Node type. Different types are treated differently in {@link NodeStructure}.
 * @author Ryan J. McCall
 */
public enum NodeType {
	
	/**
	 * For {@link Node} that represent objects
	 */
	object,
	/**
	 * For {@link Node} that represent events
	 */
	event,
	/**
	 * For {@link Node} that represent urge feelings e.g. thirst
	 */
	urgeFeeling,
	/**
	 * For {@link Node} that represent general feelings, e.g. depressed
	 */
	generalFeeling

}
