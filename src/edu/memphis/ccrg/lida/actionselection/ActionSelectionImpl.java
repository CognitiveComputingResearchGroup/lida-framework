package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * Rudimentary action selection that selects all behaviors sent to it which are
 * above the selectionThreshold.  Only selects an action every 'selectionFrequency' number of 
 * cycles it is run
 * 
 * @author Ryan J McCall
 *
 */
public class ActionSelectionImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener{
	
	private static Logger logger = Logger.getLogger("lida.actionselection.ActionSelectionImpl");
	
	private double selectionThreshold = 0.95;
	
	private int selectionFrequency = 100, coolDownCounter = 0;
	
	public ActionSelectionImpl( ) {
		super(ModuleName.ActionSelection);
		
	}

	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void receiveBehavior(Behavior b) {
		if(b.getActivation() > selectionThreshold){
			if(coolDownCounter == selectionFrequency){
				sendAction(b.getSchemeActionId());
				coolDownCounter = 0;
			}else
				coolDownCounter++;
			
			logger.log(Level.FINE, "Selected action: " + b.getSchemeActionId(), LidaTaskManager.getActualTick());
		}
	}
	
	@Override
	public void selectAction() {
	}
	
	public void sendAction(long schemeActionId){
		for(ActionSelectionListener l: listeners)
			l.receiveActionId(schemeActionId);
	}

	public Object getModuleContent() {
		return null;
	}
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener){
			addActionSelectionListener((ActionSelectionListener)listener);
		}
	}

	@Override
	public void sendAction() {		
	}


}//class
