package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class EpisodicBufferImpl implements EpisodicBuffer, CodeletReadable, GuiContentProvider{

    private List<NodeStructure> episodicBuffer = new ArrayList<NodeStructure>();
    private List<WorkspaceBufferListener> listeners = new ArrayList<WorkspaceBufferListener>();
	private final int episodicBufferCapacity;
	private List<Object> guiContent = new ArrayList<Object>();	
	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
    
	public EpisodicBufferImpl(int capacity){
		episodicBufferCapacity = capacity;
		episodicBuffer.add(new NodeStructureImpl());
	}

	public void addBufferListener(WorkspaceBufferListener listener) {
		listeners.add(listener);		
	}

	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
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
			listeners.get(i).receiveBufferContent(WorkspaceBufferListener.EBUFFER, copiedStruct);				
		
		guiContent.clear();
		guiContent.add(copiedStruct.getNodeCount());
		guiContent.add(copiedStruct.getLinkCount());
	}

	public List<NodeStructure> getBuffer() {
		return Collections.unmodifiableList(episodicBuffer);
	}

	public void sendGuiContent() {
		for(FrameworkGui fg: guis)
			fg.receiveGuiContent(FrameworkGui.FROM_EPISODIC_BUFFER, guiContent);
	}
	
}//class