package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.pam.PAMListener;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class PerceptualBufferImpl implements PerceptualBuffer, PAMListener, 
											 GuiContentProvider{
	
	private NodeStructure pamContent = new NodeStructureImpl();	
	private List<NodeStructure> perceptBuffer = new ArrayList<NodeStructure>();
	private List<WorkspaceBufferListener> pbListeners = new ArrayList<WorkspaceBufferListener>();
	private final int PERCEPT_BUFFER_CAPACITY;
	private List<Object> guiContent = new ArrayList<Object>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	
	public PerceptualBufferImpl(int capacity){
		PERCEPT_BUFFER_CAPACITY = capacity;
		perceptBuffer.add(pamContent);
	}
	
	public void addBufferListener(WorkspaceBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receivePamContent(NodeStructure ns){
		perceptBuffer.add(ns);		
		//Keep the buffer at a fixed size
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);//remove oldest	
	}

	public Collection<NodeStructure> getBufferContent() {
		return Collections.unmodifiableCollection(perceptBuffer);
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
}//PerceptualBuffer