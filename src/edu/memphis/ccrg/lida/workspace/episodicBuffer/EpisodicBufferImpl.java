package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class EpisodicBufferImpl implements EpisodicBuffer, GuiContentProvider{

    private List<NodeStructure> episodicBuffer = new ArrayList<NodeStructure>();
    private List<WorkspaceBufferListener> listeners = new ArrayList<WorkspaceBufferListener>();
	private final int episodicBufferCapacity;
    
	public EpisodicBufferImpl(int capacity){
		episodicBufferCapacity = capacity;
		episodicBuffer.add(new NodeStructureImpl());
	}

	public void addBufferListener(WorkspaceBufferListener listener) {
		listeners.add(listener);		
	}

	public synchronized void receiveLocalAssociation(NodeStructure association){
		episodicBuffer.add(association);
		//Keep the buffer at a fixed size
		if(episodicBuffer.size() > episodicBufferCapacity)
			episodicBuffer.remove(0);//remove oldest	
	}

	public List<NodeStructure> getBuffer(int i) {
		return Collections.unmodifiableList(episodicBuffer);
	}
	
}//class