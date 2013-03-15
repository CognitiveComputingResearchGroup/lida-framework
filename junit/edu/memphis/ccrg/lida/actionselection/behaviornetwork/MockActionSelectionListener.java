package edu.memphis.ccrg.lida.actionselection.behaviornetwork;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;

public class MockActionSelectionListener implements ActionSelectionListener {

	public Action action;

	@Override
	public void receiveAction(Action a) {
		action = a;
	}

}
