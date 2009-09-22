package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class BroadcastQueueImpl implements BroadcastQueue, BroadcastListener{
	
	private List<NodeStructure> broadcastQueue = new ArrayList<NodeStructure>();
	private final int broadcastQueueCapacity;
	private List<FrameworkGuiEventListener> queueListeners = new ArrayList<FrameworkGuiEventListener>();

	public BroadcastQueueImpl(int capacity){
		broadcastQueueCapacity = capacity;
		broadcastQueue.add(new NodeStructureImpl());
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastQueue.add((NodeStructure) bc);		
		//Keep the buffer at a fixed size
		if(broadcastQueue.size() > broadcastQueueCapacity)
			broadcastQueue.remove(0);//remove oldest	
	}

	public NodeStructure getBufferContent() {
		return null;
	}

	public void learn() {
		//Not applicable
	}

	public Collection<NodeStructure> getContentCollection() {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

}//class