package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class CurrentSituationalModelImpl implements CurrentSituationalModel{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<CSMListener> csmListeners = new ArrayList<CSMListener>();

	public void addCSMListener(CSMListener l){
		csmListeners.add(l);
	}
	
	public void sendCSMContent(){
		for(CSMListener l: csmListeners)
			l.receiveCSMContent((WorkspaceContent) model);
	}//method
	
	public synchronized void addWorkspaceContent(WorkspaceContent content) {
		model.mergeNodeStructure((NodeStructure) content);	
	}//method

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

	public List<Object> getGuiContent() {
		List<Object> guiContent = new ArrayList<Object>();
		guiContent.add(0, model.getNodeCount());
		guiContent.add(1, model.getLinkCount());
		return guiContent;
	}//method

}//class