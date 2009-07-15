package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class PerceptualBufferImpl implements PerceptualBuffer, GuiContentProvider{
		
	private List<NodeStructure> perceptBuffer = new ArrayList<NodeStructure>();
	private List<WorkspaceBufferListener> pbListeners = new ArrayList<WorkspaceBufferListener>();
	private final int PERCEPT_BUFFER_CAPACITY;
	
	public PerceptualBufferImpl(int capacity){
		PERCEPT_BUFFER_CAPACITY = capacity;
		perceptBuffer.add(new NodeStructureImpl());
	}
	
	public void addBufferListener(WorkspaceBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receivePAMContent(NodeStructure ns){
		perceptBuffer.add(ns);		
		//Keep the buffer at a fixed size
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);//remove oldest	
	}

	public List<NodeStructure> getBuffer(int i) {
		return Collections.unmodifiableList(perceptBuffer);
	}

}//PerceptualBuffer