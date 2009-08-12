package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletAccessible;

public interface EpisodicBuffer extends CodeletAccessible{

	void addBufferListener(WorkspaceBufferListener workspace);

}
