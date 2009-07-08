package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, GuiContentProvider{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<WorkspaceBufferListener> csmListeners = new ArrayList<WorkspaceBufferListener>();
	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
	private List<Object> guiContent = new ArrayList<Object>();

	public void addBufferListener(WorkspaceBufferListener l){
		csmListeners.add(l);
	}

	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
	}
	
	public void sendCSMContent(){
		guiContent.clear();
		guiContent.add(model.getNodeCount());
		guiContent.add(model.getLinkCount());	
		
		for(WorkspaceBufferListener l: csmListeners)
			l.receiveBufferContent(WorkspaceBufferListener.CSM, model);
	}//method
	
	public synchronized void addWorkspaceContent(NodeStructure content) {
		model.mergeNodeStructure((NodeStructure) content);	
	}//method

	/**
	 * Abstracting this, put into a attention codelet.
	 */
	public boolean hasContent(NodeStructure objective) {
		Collection<Node> nodes = objective.getNodes();
		Collection<Link> links = objective.getLinks();
		for(Node n: nodes)
			if(!model.hasNode(n))
				return false;
		
		for(Link l: links)
			if(!model.hasLink(l))	
				return false;
		
		return true;
	}//method
	
	public NodeStructure getCSMContent(){
		//TODO: this needs to be more sophisticated
		return new NodeStructureImpl(model);
	}

	public void sendGuiContent() {
		for(FrameworkGui g: guis)
			g.receiveGuiContent(FrameworkGui.FROM_CSM, guiContent);
	}

}//class