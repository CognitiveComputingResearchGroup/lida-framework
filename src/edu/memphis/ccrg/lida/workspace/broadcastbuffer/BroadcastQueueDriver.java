package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

public class BroadcastQueueDriver extends ModuleDriverImpl implements GuiEventProvider{

	private BroadcastQueueImpl broadcastQueue;

	public BroadcastQueueDriver(BroadcastQueueImpl bq, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		broadcastQueue = bq;
	}// method


	public void runThisDriver() {
		//bBuffer.activateCodelets();
	//	broadcastQueue.sendEvent();
	}


	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}
	

	//**************GUI***************
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}	
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveGuiEvent(evt);
	}//method

}// class