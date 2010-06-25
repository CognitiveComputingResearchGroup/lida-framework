package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener{
	
	private static Logger logger = Logger.getLogger("lida.actionselection.ActionSelectionImpl");
	
	private double selectionThreshold = 0.95;
	
	public ActionSelectionImpl( ) {
		super(ModuleName.ActionSelection);
		// TODO Auto-generated constructor stub
	}

	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void receiveScheme(Scheme s) {
		if(s.getActivation() > selectionThreshold){
			sendAction(s.getSchemeActionId());
			logger.log(Level.FINE, "Selected action: " + s.getSchemeActionId(), LidaTaskManager.getActualTick());
		}
	}
	
	public void sendAction(long schemeActionId){
		for(ActionSelectionListener l: listeners)
			l.receiveActionId(schemeActionId);
	}

	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener){
			addActionSelectionListener((ActionSelectionListener)listener);
		}
	}

}//class
