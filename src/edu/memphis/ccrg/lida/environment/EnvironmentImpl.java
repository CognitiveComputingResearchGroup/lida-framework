package edu.memphis.ccrg.lida.environment;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

/**
 * 
 * @author ryanjmccall
 *
 */
public abstract class EnvironmentImpl extends ModuleDriverImpl implements Environment, GuiEventProvider{
	
	private List<FrameworkGuiEventListener> listeners = new ArrayList<FrameworkGuiEventListener>();

	/**
	 * 
	 * @param ticksPerCycle
	 */
	public EnvironmentImpl(int ticksPerCycle){
		super(ticksPerCycle, ModuleName.Environment);
	}
	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener){
		listeners.add(listener);
	}

	/**
	 * A Gui Event provider may want to send different kinds of events at different
	 * times, so the event to be sent is passed as a parameter.
	 * @param evt
	 */
	@Override
	public void sendEventToGui(FrameworkGuiEvent evt){
		for(FrameworkGuiEventListener l: listeners)
			l.receiveGuiEvent(evt);
	}
	
	/**
	 * Does not need to be implemented.  Environment does not need to send any data.
	 * If you want to send data to the GUI then
	 * register instances of @FrameworkGuiEventListener to your environment
	 * 
	 */
	@Override
	public void addListener(ModuleListener listener){}
	/**
	 * Does not need to be implemented
	 */
	@Override
	public void addSubModule(LidaModule lm) { 	}
	
	/**
	 * Does not need to be implemented
	 */
	public LidaModule getSubmodule(ModuleName type) {
		return null;
	}
	/**
	 * Does not need to be implemented. Not applicable
	 */
	public void decayModule(long ticks){}
	
}//class