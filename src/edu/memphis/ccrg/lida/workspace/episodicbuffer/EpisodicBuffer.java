package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public interface EpisodicBuffer extends LocalAssociationListener{

	void addBufferListener(WorkspaceBufferListener workspace);

}
