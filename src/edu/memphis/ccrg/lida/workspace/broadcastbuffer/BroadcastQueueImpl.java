package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class BroadcastQueueImpl implements BroadcastQueue, GuiContentProvider{
	
	private List<NodeStructure> broadcastQueue = new ArrayList<NodeStructure>();
	private final int broadcastQueueCapacity;

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

	public List<NodeStructure> getBuffer(int i) {
		return Collections.unmodifiableList(broadcastQueue);
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendEvent() {
		// TODO Auto-generated method stub
		
	}

}//class