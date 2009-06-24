package edu.memphis.ccrg.lida.workspace.BroadcastBuffer;

import java.util.List;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public interface BroadcastBuffer extends BroadcastListener, CodeletReadable{

	void activateCodelets();

	List<Object> getGuiContent();

}
