package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class MockPamListener implements PamListener {
	public NodeStructure ns;
	@Override
	public void receivePercept(NodeStructure ns) {
		this.ns = ns;
	}

}
