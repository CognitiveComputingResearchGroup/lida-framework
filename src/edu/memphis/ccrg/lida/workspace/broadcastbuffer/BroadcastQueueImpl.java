package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

/**
 * This implementation stores incoming conscious broadcasts.  There is a limit on the 
 * queue's capacity.
 * 
 * @author ryanjmccall
 *
 */
public class BroadcastQueueImpl extends LidaModuleImpl implements BroadcastQueue, BroadcastListener{
	
	private List<NodeStructure> broadcastQueue = new ArrayList<NodeStructure>();
	private final int broadcastQueueCapacity;
	private List<FrameworkGuiEventListener> queueListeners = new ArrayList<FrameworkGuiEventListener>();

	public BroadcastQueueImpl(int capacity){
		super(ModuleName.BroadcastQueue);
		broadcastQueueCapacity = capacity;
		broadcastQueue.add(new NodeStructureImpl());
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastQueue.add((NodeStructure) bc);		
		//Keep the buffer at a fixed size
		if(broadcastQueue.size() > broadcastQueueCapacity)
			broadcastQueue.remove(0);//remove oldest	
	}

	public void learn() {
		//Not applicable
	}

	public Collection<NodeStructure> getModuleContentCollection() {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	public boolean addLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addNode(Node n) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteNode(Node n) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getModuleContent() {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	public void mergeIn(NodeStructure ns) {
		// TODO Auto-generated method stub
		
	}

	public void decayNodes(double lowerActivationBound) {
		// TODO Auto-generated method stub
		
	}

}//class