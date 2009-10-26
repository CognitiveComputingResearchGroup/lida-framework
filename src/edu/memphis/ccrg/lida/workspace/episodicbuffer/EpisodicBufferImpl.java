package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferListener;

public class EpisodicBufferImpl extends WorkspaceBufferImpl 
								implements EpisodicBuffer, 
										   LocalAssociationListener, 
										   GuiEventProvider{
	public EpisodicBufferImpl(int capacity){
		super(capacity);
	}

	//EpisodicBuffer interface
    private List<WorkspaceBufferListener> listeners = new ArrayList<WorkspaceBufferListener>();
  
	public void addBufferListener(WorkspaceBufferListener listener) {
		listeners.add(listener);		
	}

	//LocalAssociationListener interface
	public synchronized void receiveLocalAssociation(NodeStructure association){
		buffer.add(association);
		//Keep the buffer at a fixed size
		if(buffer.size() > capacity)
			buffer.remove(0);//remove oldest	
	}

	//GuiEventProvider
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}	
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveGuiEvent(evt);
	}//method

}//class