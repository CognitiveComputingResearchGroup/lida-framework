/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.gui.events.TaskCountEvent;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

//TODO: make a TaskSpawner
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
public class GlobalWorkspaceImpl extends ModuleDriverImpl implements GlobalWorkspace,
											GuiEventProvider {
	
	private Logger logger = Logger.getLogger("lida.globalworkspace.GlobalWorkspaceImpl");

	public GlobalWorkspaceImpl(int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		// TODO Auto-generated constructor stub
	}

	private Queue<Coalition> coalitions = new ConcurrentLinkedQueue<Coalition>();
	private List<BroadcastTrigger> broadcastTriggers = new ArrayList<BroadcastTrigger>();
	private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private AtomicBoolean broadcastStarted = new AtomicBoolean(false);

	/*
	 * (non-Javadoc)
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
	public boolean addCoalition(Coalition coalition) {
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
			if (broadcastStarted.compareAndSet(false, true)) {
				sendBroadcast();
			}
	}// method

	private void sendBroadcast() {
		Coalition coal;
			coal = chooseCoalition();
			if (coal != null) {
				coalitions.remove(coal);
			}
		
		if (coal != null) {
			NodeStructure copy = new NodeStructureImpl((NodeStructure) coal
					.getContent());
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast((BroadcastContent) copy);
			}
			FrameworkGuiEvent ge = new TaskCountEvent(ModuleType.GlobalWorkspace, coalitions.size()+"");
			sendEvent(ge);
		}
		logger.log(Level.FINE,"Broadcast Performed at tick: {0}",LidaTaskManager.getActualTick());

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

		decay();
		resetTriggers();
		broadcastStarted.set(false);
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

	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener fg : guis)
			fg.receiveGuiEvent(evt);
	}

	public ModuleType getModuleType() {
		return ModuleType.GlobalWorkspace;
	}

	public void init(Properties lidaProperties) {
		// TODO Auto-generated method stub
		
	}

	public void decay(){
		for (Coalition c : coalitions) {
			c.decay();
			if (c.getActivation()<=0.0){
				coalitions.remove(c);
			}
	}
	}

	@Override
	protected void runThisDriver() {
		start();
		setTaskStatus(LidaTaskStatus.FINISHED); //Runs only once
	}

	public Object getModuleContent() {
		return Collections.unmodifiableCollection(coalitions);
	}

	public LidaModule getSubmodule(ModuleType type) {
		return null;
	}

}// class
