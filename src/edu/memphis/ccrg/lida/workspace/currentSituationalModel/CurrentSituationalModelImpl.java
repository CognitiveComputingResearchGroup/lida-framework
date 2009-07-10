package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGui;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, GuiContentProvider{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<WorkspaceBufferListener> csmListeners = new ArrayList<WorkspaceBufferListener>();
	//
	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
	private List<Object> guiContent = new ArrayList<Object>();

	public void addBufferListener(WorkspaceBufferListener l){
		csmListeners.add(l);
	}

	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
	}
	
	/**
	 * Send content to listeners, e.g. TEM, DM
	 */
	public void sendCSMContent(){
		for(WorkspaceBufferListener l: csmListeners)
			l.receiveBufferContent(WorkspaceBufferListener.CSM, model);
	}//method
	
	/**
	 * Called by Workspace.  Codelets are typically adding the content.
	 */
	public synchronized void addWorkspaceContent(NodeStructure content) {
		model.mergeNodeStructure((NodeStructure) content);	
	}//method
	
	public void addCodeletContent(NodeStructure ns) {
		// TODO Auto-generated method stub
		
	}
	
	public NodeStructure getCSMContent(){
		//TODO: this needs to be more sophisticated
		return new NodeStructureImpl(model);
	}

	public void sendGuiContent() {
		guiContent.clear();
		guiContent.add(model.getNodeCount());
		guiContent.add(model.getLinkCount());
		for(FrameworkGui g: guis)
			g.receiveGuiContent(FrameworkGui.FROM_CSM, guiContent);
	}

	public List<NodeStructure> getBuffer() {
		List<NodeStructure> list = new ArrayList<NodeStructure>();
		list.add(model);
		return list;
	}

}//class