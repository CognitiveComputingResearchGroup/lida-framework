package edu.memphis.ccrg.lida.environment;

import java.util.ArrayList;

import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class TestEnvironment implements Environment, Runnable, Stoppable, ActionSelectionListener {

	private ArrayList<EnvironmentListener> listeners = new ArrayList<EnvironmentListener>();
	private boolean actionHasChanged = false;
	private ActionContent currentBehavior = null;
	private boolean keepRunning = true;	
	private FrameworkTimer timer;
	private long threadID;
	
	public TestEnvironment(FrameworkTimer timer) {
		// TODO Auto-generated constructor stub
	}

	public void receiveBehaviorContent(ActionContent content) {
		// TODO Auto-generated method stub

	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void stopRunning() {
		// TODO Auto-generated method stub
		
	}

	public void addEnvironmentListener(EnvironmentListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void getNewEnvironment() {
		// TODO Auto-generated method stub
		
	}

}
