package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferListener;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, PamListener, GuiEventProvider{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<WorkspaceBufferListener> csmListeners = new ArrayList<WorkspaceBufferListener>();

	//Implementations of CurrentSituationalModel interface
	public void addBufferListener(WorkspaceBufferListener l){
		csmListeners.add(l);
	}
	/**
	 * Sends content to listeners, e.g. TEM, DM
	 */
	public void sendCsmContent(){
		for(WorkspaceBufferListener l: csmListeners)
			l.receiveBufferContent(Module.CurrentSituationalModel, (WorkspaceContent) model);
	}//method

	//Implementions of PamListener interface
	public void receiveLink(Link l) {
		// TODO Auto-generated method stub
		
	}
	public void receiveNode(PamNode node) {
		// TODO Auto-generated method stub
		
	}
	public void receiveNodeStructure(NodeStructure ns) {
		// TODO Auto-generated method stub
		
	}
	
	//GuiEventProvider implementations
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}	
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveGuiEvent(evt);
	}//method
	
	//CodeletAccessible interface
	public boolean addLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean addNode(Node n) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean deleteLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean deleteNode(Node n) {
		// TODO Auto-generated method stub
		return false;
	}
	public NodeStructure getModuleContent() {
		return model;
	}
	public Collection<NodeStructure> getModuleContentCollection() {
		List<NodeStructure> list = new ArrayList<NodeStructure>();
		list.add(model);
		return Collections.unmodifiableCollection(list);
	}
	public void mergeIn(NodeStructure ns) {
		// TODO Auto-generated method stub
		
	}

}//class