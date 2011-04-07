package edu.memphis.ccrg.lida.framework.tasks;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

/**
 * Abstract implementation of {@link Codelet}
 * @author Ryan J. McCall
 *
 */
public abstract class CodeletImpl extends LidaTaskImpl implements Codelet {

	protected NodeStructure soughtContent;
	
	public CodeletImpl() {
		soughtContent = new NodeStructureImpl();
	}

	/**
	 * @return the sought content
	 */
	@Override
	public NodeStructure getSoughtContent() {
		return soughtContent;
	}

	/**
	 * @param content
	 *            sought content
	 */
	@Override
	public void setSoughtContent(NodeStructure content) {
		soughtContent = content;
	}

}
