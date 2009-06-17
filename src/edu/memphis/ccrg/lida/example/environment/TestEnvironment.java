package edu.memphis.ccrg.lida.example.environment;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class TestEnvironment implements Environment, Runnable, Stoppable, ActionSelectionListener {

	private FrameworkTimer timer;
	private long threadID;
	private List<EnvironmentListener> listeners;
	private boolean keepRunning = true;	
	private boolean actionHasChanged = false;
	private ActionContent actionContent = null;
	private TestEnvironmentContent environContent = new TestEnvironmentContent();
	
	public TestEnvironment(FrameworkTimer timer) {
		this.timer = timer;
		listeners = new ArrayList<EnvironmentListener>();
	}
	
	public void addEnvironmentListener(EnvironmentListener listener) {
		listeners.add(listener);		
	}
	
	public synchronized void receiveBehaviorContent(ActionContent action){
		actionContent = action;		
		actionHasChanged = true;
	}

	public void run() {
		Integer latestAction = 0;
		while(keepRunning){
			try{Thread.sleep(50);}catch(Exception e){}			
			timer.checkForStartPause();
			
			environContent.setContent(-1);
			for(int i = 0; i < listeners.size(); i++)
				(listeners.get(i)).receiveEnvironmentContent(environContent);
			
			if(actionHasChanged){
				latestAction = (Integer)actionContent.getContent();
				synchronized(this){
					actionHasChanged = false;
				}
				if(!latestAction.equals(null)){
					//handleAction(currentAction);
				}
			}//if actionHasChanged	
			
		}//while
		
	}//method

	public void stopRunning() {
		keepRunning = false;
	}

	public void resetEnvironment() {
		// TODO Auto-generated method stub
		//reset environ
	}//method

}//class
