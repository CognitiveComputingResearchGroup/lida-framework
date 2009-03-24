package edu.memphis.ccrg.lida.workspace.csm;

import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.workspace.sbCodelets.WorkspaceContent;

public class CurrentSituationalModelImpl implements CurrentSituationalModel{
	
	private Percept p;
	
	public CurrentSituationalModelImpl(){

	}
	
	public void addWorkspaceContent(WorkspaceContent content) {
		if(content != null){
			synchronized(this){
				p = (Percept)content.getContent();
			}
		}
	}

	public boolean hasContent(CSMContentImpl whatIwant) {
		// TODO Auto-generated method stub
		return false;
	}

	public CSMContentImpl getContent(CSMContentImpl whatIwant) {
		// TODO Auto-generated method stub
		return null;
	}

}
