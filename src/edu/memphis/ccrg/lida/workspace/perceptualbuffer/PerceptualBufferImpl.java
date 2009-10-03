package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class PerceptualBufferImpl implements PerceptualBuffer, PamListener{
	
	private NodeStructure perceptualBuffer = new NodeStructureImpl();	
	private Workspace workspace;

	public void addBufferListener(WorkspaceBufferListener l){
		workspace = (Workspace) l;
	}
	
	public synchronized void receiveNodeStructure(NodeStructure ns){
		perceptualBuffer.mergeWith(ns);
	}
	
	public void receiveLink(Link l) {
		//System.out.println("receive link");
		perceptualBuffer.mergeWith(l);
	}
	public void receiveNode(PamNode node) {
		//System.out.println("receive node");
		perceptualBuffer.mergeWith(node);
	}

	public NodeStructure getBufferContent() {
		return perceptualBuffer;
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
		List<NodeStructure> list = new ArrayList<NodeStructure>();
		list.add(perceptualBuffer);
		return list;
	}

	public void cueEpisodicMemory() {
		((WorkspaceBufferListener) workspace).receiveBufferContent(Module.PerceptualBuffer, perceptualBuffer);
	}

}//PerceptualBuffer