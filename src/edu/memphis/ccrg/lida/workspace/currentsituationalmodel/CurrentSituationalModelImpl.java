package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletWritable;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, PamListener, CodeletWritable{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<WorkspaceBufferListener> csmListeners = new ArrayList<WorkspaceBufferListener>();

	public void addBufferListener(WorkspaceBufferListener l){
		csmListeners.add(l);
	}
	/**
	 * Sends content to listeners, e.g. TEM, DM
	 */
	public void sendCSMContent(){
		for(WorkspaceBufferListener l: csmListeners)
			l.receiveBufferContent(Module.CurrentSituationalModel, model);
	}//method
	
	/**
	 * Called by Workspace.  Codelets are typically adding the content.
	 */
	public synchronized void addCodeletContent(Module m, NodeStructure ns) {
		model.mergeWith(ns);
	}
	
	public NodeStructure getModel(){
		//TODO: this needs to be more sophisticated
		return new NodeStructureImpl(model);
	}
	public NodeStructure getCSMContent(){
		//TODO: this needs to be more sophisticated
		return new NodeStructureImpl(model);
	}

	public NodeStructure getBufferContent() {
		return model;
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
		list.add(model);
		return Collections.unmodifiableCollection(list);
	}

	public void receiveLink(Link l) {
		// TODO Auto-generated method stub
		
	}

	public void receiveNode(PamNode node) {
		// TODO Auto-generated method stub
		
	}

	public void receiveNodeStructure(NodeStructure ns) {
		// TODO Auto-generated method stub
		
	}

}//class