/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * Coalitions. It supports triggers that are in charge to trigger the new
 * broadcast. Triggers should implement Trigger interface. This class maintains a
 * list of broadcastListeners. These are the modules (classes) that need to
 * receive broadcast content.
 * 
 * @author Javier Snaider
 * 
 */
public class GlobalWorkspaceImpl implements GlobalWorkspace, TriggerListener{
	private Set<Coalition> coalitions = new HashSet<Coalition>();
	private List<BroadcastTrigger> broadcastTriggers = new ArrayList<BroadcastTrigger>();
	private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
	private Boolean broadcastStarted = false;
	private FrameworkGui flowGui;
	
	public GlobalWorkspaceImpl(){}
	
	public GlobalWorkspaceImpl(FrameworkGui gui){
		flowGui = gui;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seelida.globalworkspace.GlobalWorkspace#addBroadcastListener(edu.memphis.ccrg.
	 * globalworkspace.BroadcastListener)
	 */
	public void addBroadcastListener(BroadcastListener bl) {
		broadcastListeners.add(bl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#addTrigger(edu.memphis.ccrg.globalworkspace.
	 * Trigger)
	 */
	public void addBroadcastTrigger(BroadcastTrigger t) {
		broadcastTriggers.add(t);
	}
	
	public void start() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.start();
		}

	}//method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#putCoalition(edu.memphis.ccrg.globalworkspace
	 * .Coalition)
	 */
	public synchronized boolean addCoalition(Coalition coalition) {		
		if(coalitions.add(coalition)){
			newCoalitionEvent();
			return true;
		}else{
			return false;
		}
	}//method

	private void newCoalitionEvent() {
		for (BroadcastTrigger trigger : broadcastTriggers) 
			trigger.checkForTrigger(coalitions);
	}//method
	

	/**
	 * This method realizes the broadcast. First it chooses the winner
	 * coalition. Then, all registered BroadcastListeners receive a reference
	 * SEEEEE to the coalition content.
	 * 
	 * this method is supposed to be called from Triggers. All triggers are
	 * reseted, reset() method is invoked on each of them. The coalition pool is
	 * cleared.
	 * 
	 */
	public void triggerBroadcast() {
		synchronized (broadcastStarted) {
			if (!broadcastStarted) {
				broadcastStarted = true;
				sendBroadcast();
			}
		}//synch
	}//method

	private void sendBroadcast() {
		//System.out.println("send called");
		Coalition coal;
		synchronized (this) {
			coal = chooseCoalition();
			if (coal != null) {
				coalitions.remove(coal);
			}
		}
		if (coal != null) {
			BroadcastContent content = coal.getContent();
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast(content);
			}
		}
		for(Coalition c:coalitions){
			c.decay();
		}
		resetTriggers();
		synchronized (broadcastStarted) {
			broadcastStarted = false;
		}
	}
	
	private Coalition chooseCoalition() {
		Coalition chosenCoal = null;
		for (Coalition c : coalitions) {
			if (chosenCoal == null || c.getActivation() > chosenCoal.getActivation()) {
				chosenCoal = c;
			}
		}//for
		return chosenCoal;
	}//method

	private void resetTriggers() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.reset();
		}
	}

}//class
