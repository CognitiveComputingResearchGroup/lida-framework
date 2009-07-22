package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public interface PerceptualBuffer extends PAMListener{

	void addBufferListener(WorkspaceBufferListener workspace);

}
