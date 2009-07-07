package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkGuiProvider;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class CurrentSituationalModelImpl implements CurrentSituationalModel, FrameworkGuiProvider{
	
	private NodeStructure model = new NodeStructureImpl();
	private List<CSMListener> csmListeners = new ArrayList<CSMListener>();

	public void addCSMListener(CSMListener l){
		csmListeners.add(l);
	}
	
	public void sendCSMContent(){
		for(CSMListener l: csmListeners)
			l.receiveCSMContent((NodeStructure) model);
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

	public void addFrameworkGui(FrameworkGui listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendGuiContent() {
		// TODO Auto-generated method stub
		
	}

}//class