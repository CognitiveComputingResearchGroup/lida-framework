package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class EpisodicBufferImpl implements EpisodicBuffer, LocalAssociationListener, 
										   GuiEventProvider{

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

	public NodeStructure getBufferContent() {
		return null;
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

	public Collection<NodeStructure> getContentCollection() {
		return Collections.unmodifiableCollection(episodicBuffer);
	}
	
}//class