package edu.memphis.ccrg.lida.framework.shared;

/**
 * Used to differentiate kinds of nodes.  Not sure how useful this will turn out to be... 
 * I've commented the ideas behind these node classes
 * 
 * @author Ryan J. McCall
 *
 */
public enum NodeClass {
	
	/**
	 * Instance or object nodes are typically objects present in the environment.
	 * Grounded in the stimuli
	 */
	instance,
	
	/**
	 * Nodes representing a category.  Have instance nodes and feature nodes as children.
	 */
	category,
	
	/**
	 * Action nodes have role links
	 */
	action,
	
	/**
	 * Feature nodes represent a perceptual feature
	 */
	feature,
	
	/**
	 * Feeling nodes send a bit of extra activation to their neighbors
	 */
	feeling, 
	
	/**
	 * Just-made nodes may not have a class distinction yet.
	 */
	none

}//enum
