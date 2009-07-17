package edu.memphis.ccrg.lida.workspace.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class WorkspaceBuffersImpl implements PAMListener, LocalAssociationListener, 
											 BroadcastListener, WorkspaceBuffers{

	private final int perceptBufferCapacity;
	private final int episodicBufferCapacity;
	private final int broadcastQueueCapacity;
	//
	private List<NodeStructure> perceptualBuffer = new ArrayList<NodeStructure>();
	private List<NodeStructure> episodicBuffer = new ArrayList<NodeStructure>();
	private List<NodeStructure> broadcastQueue = new ArrayList<NodeStructure>();
	//
    private List<WorkspaceBufferListener> pBufferListeners = new ArrayList<WorkspaceBufferListener>();
    private List<WorkspaceBufferListener> eBufferListeners = new ArrayList<WorkspaceBufferListener>();

	public WorkspaceBuffersImpl(int pBufferCapacity, int eBufferCapacity, int bQueueCapacity){
		perceptBufferCapacity = pBufferCapacity;
		episodicBufferCapacity = eBufferCapacity;
		broadcastQueueCapacity = bQueueCapacity;
		perceptualBuffer.add(new NodeStructureImpl());
		episodicBuffer.add(new NodeStructureImpl());
		broadcastQueue.add(new NodeStructureImpl());
	}
	
	public void addPerceptualBufferListener(WorkspaceBufferListener l){
		pBufferListeners.add(l);
	}
	
	public void addEpisodicBufferListener(WorkspaceBufferListener l) {
		eBufferListeners.add(l);		
	}
	public void receivePAMContent(NodeStructure percept) {
		perceptualBuffer.add(percept);		
		//Keep the buffer at a fixed size
		if(perceptualBuffer.size() > perceptBufferCapacity)
			perceptualBuffer.remove(0);//remove oldest	
	}
	
	public void receiveLocalAssociation(NodeStructure localAssociation) {
		episodicBuffer.add(localAssociation);
		//Keep the buffer at a fixed size
		if(episodicBuffer.size() > episodicBufferCapacity)
			episodicBuffer.remove(0);//remove oldest	
	}

	public void receiveBroadcast(BroadcastContent bc) {
		broadcastQueue.add((NodeStructure) bc);		
		//Keep the buffer at a fixed size
		if(broadcastQueue.size() > broadcastQueueCapacity)
			broadcastQueue.remove(0);//remove oldest	
	}

	public List<NodeStructure> getBroadcastQueue() {
		return Collections.unmodifiableList(broadcastQueue);
	}

	public List<NodeStructure> getEpisodicBuffer() {
		return Collections.unmodifiableList(episodicBuffer);
	}

	public List<NodeStructure> getPerceptualBuffer() {
		return Collections.unmodifiableList(perceptualBuffer);
	}

}//class
