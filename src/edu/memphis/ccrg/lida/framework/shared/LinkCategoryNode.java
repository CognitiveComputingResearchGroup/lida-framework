/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * @author Javier Snaider and Ryan J. McCall
 *
 */
public class LinkCategoryNode extends PamNodeImpl implements LinkCategory {

	public static LinkCategory NONE = (LinkCategory) LidaElementFactory.getInstance().getNode("LinkCategoryNode", "None");
	public static LinkCategory CHILD = (LinkCategory) LidaElementFactory.getInstance().getNode("LinkCategoryNode", "Child");
	public static LinkCategory GROUNDING = (LinkCategory) LidaElementFactory.getInstance().getNode("LinkCategoryNode", "Grounding");
	public static LinkCategory PARENT = (LinkCategory) LidaElementFactory.getInstance().getNode("LinkCategoryNode", "Parent");
	public static LinkCategory LATERAL = (LinkCategory) LidaElementFactory.getInstance().getNode("LinkCategoryNode", "Lateral");

}
