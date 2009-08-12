package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletAccessible;

public interface PerceptualBuffer extends CodeletAccessible{

	void addBufferListener(WorkspaceBufferListener workspace);

}
