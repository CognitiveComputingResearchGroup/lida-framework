package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.episodicmemory.LocalAssociationListener;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class MockLocalAssocListener implements LocalAssociationListener {

	public NodeStructure ns;
	@Override
	public void receiveLocalAssociation(NodeStructure association) {
		ns=association;

	}

}
