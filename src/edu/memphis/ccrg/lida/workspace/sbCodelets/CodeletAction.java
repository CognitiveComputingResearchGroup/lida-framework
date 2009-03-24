package edu.memphis.ccrg.lida.workspace.sbCodelets;

import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public class CodeletAction{

	public WorkspaceContent getResultOfAction(WorkspaceContent content) {
		WorkspaceContent result = null;
		if(content != null){
			NodeStructure struct = (NodeStructure)content.getContent();
			if(struct != null){
				//System.out.println(struct.getNodes().size());
			}
			
		}
		return result;
	}

}
