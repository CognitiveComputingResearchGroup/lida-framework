package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class PerceptualBufferImpl implements PerceptualBuffer, PamListener, 
											 GuiContentProvider{
	
	private NodeStructure perceptualBuffer = new NodeStructureImpl();	
//	private List<NodeStructure> perceptBuffer = new ArrayList<NodeStructure>();
	private List<WorkspaceBufferListener> pbListeners = new ArrayList<WorkspaceBufferListener>();
//	private final int PERCEPT_BUFFER_CAPACITY;
	private List<Object> guiContent = new ArrayList<Object>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	
//	public PerceptualBufferImpl(int capacity){
//		PERCEPT_BUFFER_CAPACITY = capacity;
//		perceptBuffer.add(pamContent);
//	}
	
	public void addBufferListener(WorkspaceBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receiveNodeStructure(NodeStructure ns){
		perceptualBuffer.mergeWith(ns);
//		perceptBuffer.add(ns);		
//		//Keep the buffer at a fixed size
//		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
//			perceptBuffer.remove(0);//remove oldest	
	}
	
	public void receiveLink(Link l) {
		perceptualBuffer.mergeWith(l);
	}
	public void receiveNode(PamNode node) {
		perceptualBuffer.mergeWith(node);
	}

	public NodeStructure getBufferContent() {
		return perceptualBuffer;
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					Module.perceptualBuffer, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}		
	}

	public Collection<NodeStructure> getContentCollection() {
		List<NodeStructure> list = new ArrayList<NodeStructure>();
		list.add(perceptualBuffer);
		return list;
	}

}//PerceptualBuffer