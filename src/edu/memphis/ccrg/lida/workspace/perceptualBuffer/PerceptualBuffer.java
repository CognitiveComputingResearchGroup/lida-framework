package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public interface PerceptualBuffer extends PAMListener, CodeletReadable{

	void activateCodelets();

}
