package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class EpisodicBufferImpl implements EpisodicBuffer, LocalAssociationListener, 
										   GuiContentProvider{

    private List<NodeStructure> episodicBuffer = new ArrayList<NodeStructure>();
    private List<WorkspaceBufferListener> listeners = new ArrayList<WorkspaceBufferListener>();
	private final int episodicBufferCapacity;
	private List<Object> guiContent = new ArrayList<Object>();	
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
    
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

	public Collection<NodeStructure> getBufferContent() {
		return Collections.unmodifiableCollection(episodicBuffer);
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					FrameworkGuiEvent.CSM, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}		
	}
	
}//class