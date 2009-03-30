package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import java.util.Set;

import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class CurrentSituationalModelImpl implements CurrentSituationalModel{
	
	private NodeStructure struct;
	
	public CurrentSituationalModelImpl(){
		struct = new GraphImpl(0.0, 0.0);
	}
	
	public void addWorkspaceContent(WorkspaceContent content) {
		if(content != null){
			synchronized(this){
				struct = (GraphImpl)content.getContent();
			}
			
			Set<Node> nodes = struct.getNodes();
			if(nodes != null)
				System.out.println("in csm there are nodes " + nodes.size());
		}
	}

	public boolean hasContent(CSMContentImpl whatIwant) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public CurrentSituationalModelContent  getContent(){
		CSMContentImpl content = new CSMContentImpl(struct);
		return content;
	}

	public CSMContentImpl getContent(CSMContentImpl whatIwant) {
		// TODO Auto-generated method stub
		return null;
	}

}
