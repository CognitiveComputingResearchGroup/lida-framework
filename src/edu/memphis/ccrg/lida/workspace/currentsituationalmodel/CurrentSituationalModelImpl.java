package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, GuiContentProvider{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<WorkspaceBufferListener> csmListeners = new ArrayList<WorkspaceBufferListener>();
	//
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<Object> guiContent = new ArrayList<Object>();

	public void addBufferListener(WorkspaceBufferListener l){
		csmListeners.add(l);
	}
	
	public synchronized void addCodeletContent(NodeStructure ns) {
		model.mergeWith(ns);
	}
	
	public NodeStructure getModel(){
		//TODO: this needs to be more sophisticated
		return new NodeStructureImpl(model);
	}
	
	/**
	 * Send content to listeners, e.g. TEM, DM
	 */
	public void sendCSMContent(){
		for(WorkspaceBufferListener l: csmListeners)
			l.receiveBufferContent(Module.currentSituationalModel, model);
	}//method
	
	/**
	 * Called by Workspace.  Codelets are typically adding the content.
	 */
	public synchronized void addWorkspaceContent(NodeStructure content) {
		model.mergeWith((NodeStructure) content);	
	}//method
	

	public NodeStructure getCSMContent(){
		//TODO: this needs to be more sophisticated
		return new NodeStructureImpl(model);
	}

	public NodeStructure getBufferContent() {
		return model;
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		guiContent.clear();
		guiContent.add(model.getNodeCount());
		guiContent.add(model.getLinkCount());
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(Module.currentSituationalModel, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}		
	}//method

	public Collection<NodeStructure> getContentCollection() {
		List<NodeStructure> list = new ArrayList<NodeStructure>();
		list.add(model);
		return Collections.unmodifiableCollection(list);
	}

}//class