package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * A listener of local associations must be able to receive a NodeStructure association.
 * @author ryanjmccall
 *
 */
public interface LocalAssociationListener extends ModuleListener{
	
	public abstract void receiveLocalAssociation(NodeStructure association);

}
