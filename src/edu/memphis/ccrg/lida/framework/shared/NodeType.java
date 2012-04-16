package edu.memphis.ccrg.lida.framework.shared;

/**
 * A marker of Node type. Different types are treated differently in {@link NodeStructure}.
 * @author Ryan J. McCall
 */
public enum NodeType {
	
	/**
	 * For {@link Node}s that represent objects
	 */
	object,
	/**
	 * For {@link Node}s that represent events
	 */
	event,
	/**
	 * For {@link Node}s that represent built-in need feelings e.g. thirst, hunger
	 */
	needFeeling,
	/**
	 * For {@link Node}s that represent interpretive (appraisal) feelings, e.g. sweet, sour
	 */
	interprativeFeeling

}
