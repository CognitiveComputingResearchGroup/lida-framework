/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * @author Javier Snaider and Ryan McCall
 *
 */
public class LinkCategoryNode extends PamNodeImpl implements LinkCategory {

	public static LinkCategory NONE = (LinkCategory) NodeFactory.getInstance().getNode("LinkCategoryNode", "None");
	public static LinkCategory CHILD = (LinkCategory) NodeFactory.getInstance().getNode("LinkCategoryNode", "Child");
	public static LinkCategory GROUNDING = (LinkCategory) NodeFactory.getInstance().getNode("LinkCategoryNode", "Grounding");
	public static LinkCategory PARENT = (LinkCategory) NodeFactory.getInstance().getNode("LinkCategoryNode", "Parent");
	public static LinkCategory LATERAL = (LinkCategory) NodeFactory.getInstance().getNode("LinkCategoryNode", "Lateral");

}
