/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * Coalitions. It supports triggers that are in charge to trigger the new
 * broadcast. Triggers should implement Trigger interface. This class maintains
 * a list of broadcastListeners. These are the modules (classes) that need to
 * receive broadcast content.
 * 
 * @author Javier Snaider
 * 
 */
public class GlobalWorkspaceImpl implements GlobalWorkspace, TriggerListener,
		GuiContentProvider {
	private Set<Coalition> coalitions = new HashSet<Coalition>();
	private List<BroadcastTrigger> broadcastTriggers = new ArrayList<BroadcastTrigger>();
	private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private Boolean broadcastStarted = false;
	List<Object> guiContent = new ArrayList<Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @seelida.globalworkspace.GlobalWorkspace#addBroadcastListener(edu.memphis.
	 * ccrg. globalworkspace.BroadcastListener)
	 */
	public void addBroadcastListener(BroadcastListener bl) {
		broadcastListeners.add(bl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#addTrigger(edu.memphis
	 * .ccrg.globalworkspace. Trigger)
	 */
	public void addBroadcastTrigger(BroadcastTrigger t) {
		broadcastTriggers.add(t);
	}

	public void start() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.start();
		}

	}// method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#putCoalition(edu.memphis
	 * .ccrg.globalworkspace .Coalition)
	 */
	public synchronized boolean addCoalition(Coalition coalition) {
		if (coalitions.add(coalition)) {
			newCoalitionEvent();
			return true;
		} else {
			return false;
		}
	}// method

	private void newCoalitionEvent() {
		for (BroadcastTrigger trigger : broadcastTriggers)
			trigger.checkForTrigger(coalitions);
	}// method

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
		}// synch
	}// method

	private void sendBroadcast() {
		Coalition coal;
		synchronized (this) {
			coal = chooseCoalition();
			if (coal != null) {
				coalitions.remove(coal);
			}
		}
		if (coal != null) {
			NodeStructure copy = new NodeStructureImpl((NodeStructure) coal
					.getContent());
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast((BroadcastContent) copy);
			}
			sendEvent();
		}

		// TODO: No attention codelet is going
		// to be able to add a new coalition while the decaying
		// process is working. The decaying process can be slow.
		// So, a better solution maybe to create an aux collection
		// before to decay and iterate over this aux collection.
		// The add of the references to this aux collection does be
		// synchronized but the decay iteration doesn't. On the other
		// hand, we need to delete coalitions from the set when their
		// activation is 0. So, for now, I suggest to use your solution,
		// but copy this comment in the code and add a note please.
		synchronized (this) {
			for (Coalition c : coalitions) {
				c.decay();
			}
		}
		resetTriggers();
		synchronized (broadcastStarted) {
			broadcastStarted = false;
		}
	}

	private Coalition chooseCoalition() {
		Coalition chosenCoal = null;
		for (Coalition c : coalitions) {
			if (chosenCoal == null
					|| c.getActivation() > chosenCoal.getActivation()) {
				chosenCoal = c;
			}
		}// for
		return chosenCoal;
	}// method

	private void resetTriggers() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.reset();
		}
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		if (!guis.isEmpty()) {
			guiContent.clear();
			guiContent.add(coalitions.size());
			guiContent.add(-1);
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					Module.globalWorkspace, "coalitions",
					guiContent);
			for (FrameworkGuiEventListener fg : guis)
				fg.receiveGuiEvent(event);
		}
	}

}// class
