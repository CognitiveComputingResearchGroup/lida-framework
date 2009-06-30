package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class EpisodicBufferImpl implements EpisodicBuffer, CodeletReadable, GuiContentProvider{

    private List<NodeStructure> episodicBuffer = new ArrayList<NodeStructure>();
    private List<EpisodicBufferListener> listeners = new ArrayList<EpisodicBufferListener>();
	private final int episodicBufferCapacity;
	private List<Object> guiContent = new ArrayList<Object>();	
    
	public EpisodicBufferImpl(int capacity){
		episodicBufferCapacity = capacity;
		episodicBuffer.add(new NodeStructureImpl());
	}

	public void addEBufferListener(EpisodicBufferListener listener) {
		listeners.add(listener);		
	}
	
	public synchronized void receiveLocalAssociation(NodeStructure association){
		episodicBuffer.add(association);
		//Keep the buffer at a fixed size
		if(episodicBuffer.size() > episodicBufferCapacity)
			episodicBuffer.remove(0);//remove oldest	
	}
	
	public void activateCodelets() {
		NodeStructureImpl copiedStruct = new NodeStructureImpl((NodeStructure) episodicBuffer.get(0));
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).receiveEpisodicBufferContent(copiedStruct);				
		
		guiContent.clear();
		guiContent.add(copiedStruct.getNodeCount());
		guiContent.add(copiedStruct.getLinkCount());
	}

	public NodeStructure lookForContent(NodeStructure objective) {
		NodeStructureImpl result = new NodeStructureImpl();
		synchronized(this){
			for(NodeStructure content: episodicBuffer){
				Collection<Node> nodes = content.getNodes();					
				for(Node n: nodes)
					result.addNode(n);				
			}//for each struct in queue
		}//synchronized
		return result;
	}

	public List<Object> getGuiContent() {
		return guiContent;
	}
	
}//class