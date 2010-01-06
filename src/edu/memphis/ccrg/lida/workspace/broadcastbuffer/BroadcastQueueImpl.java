package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

/**
 * This implementation stores incoming conscious broadcasts. There is a limit on
 * the queue's capacity.
 * 
 * @author ryanjmccall
 * 
 */
public class BroadcastQueueImpl extends LidaModuleImpl implements
		BroadcastQueue, BroadcastListener {

	private static Logger logger = Logger
			.getLogger("lida.workspace.main.Workpace");

	private Queue<NodeStructure> broadcastQueue = new ConcurrentLinkedQueue<NodeStructure>();
	private volatile int broadcastQueueCapacity;
	private double lowerActivationBound;
	private static final int DEFAULT_QUEUE_SIZE = 20;

	public BroadcastQueueImpl(int capacity) {
		super(ModuleName.BroadcastQueue);
		broadcastQueueCapacity = capacity;
		broadcastQueue.add(new NodeStructureImpl());
	}

	public BroadcastQueueImpl() {
		this(DEFAULT_QUEUE_SIZE);
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastQueue.offer((NodeStructure) bc);
		// Keep the buffer at a fixed size
		if (broadcastQueue.size() > broadcastQueueCapacity)
			broadcastQueue.poll();// remove oldest
	}

	public void learn() {
		// Not applicable
	}

	public Collection<NodeStructure> getModuleContentCollection() {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	public boolean addLink(Link l) {
		return false;
	}

	public boolean addNode(Node n) {
		return false;
	}

	public boolean deleteLink(Link l) {
		return false;
	}

	public boolean deleteNode(Node n) {
		return false;
	}

	public Object getModuleContent() {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	public void mergeIn(NodeStructure ns) {
	}

	public void decayModule(long ticks) {
		for (NodeStructure ns : broadcastQueue) {
			Collection<Node> nodes = ns.getNodes();
			for (Node n : nodes) {
				n.decay(ticks);
				if (n.getActivation() <= lowerActivationBound) {
					ns.deleteNode(n);
				}
			}
		}
	}

	public void setLowerActivationBound(double lowerActivationBound) {
		this.lowerActivationBound = lowerActivationBound;
	}

	public void addListener(ModuleListener listener) {
	}

	public void init(Properties p) {
		try {
			broadcastQueueCapacity = Integer.parseInt(p
					.getProperty("workspace.broadcastQueueCapacity"));
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error reading properties",0L);
		}
	}
}// class