/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * Node-based implementation of LinkCategory
 * @author Javier Snaider and Ryan J. McCall
 */
public class LinkCategoryNode extends PamNodeImpl implements LinkCategory {
	
	private static final String factoryName = LinkCategoryNode.class.getSimpleName();

	public static LinkCategory NONE = (LinkCategory) LidaElementFactory.getInstance().getNode(factoryName, "None");
	public static LinkCategory CHILD = (LinkCategory) LidaElementFactory.getInstance().getNode(factoryName, "Child");
	public static LinkCategory GROUNDING = (LinkCategory) LidaElementFactory.getInstance().getNode(factoryName, "Grounding");
	public static LinkCategory PARENT = (LinkCategory) LidaElementFactory.getInstance().getNode(factoryName, "Parent");
	public static LinkCategory LATERAL = (LinkCategory) LidaElementFactory.getInstance().getNode(factoryName, "Lateral");

}
