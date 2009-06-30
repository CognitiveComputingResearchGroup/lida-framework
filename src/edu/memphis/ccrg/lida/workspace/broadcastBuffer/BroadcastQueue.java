package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import java.util.List;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public interface BroadcastQueue extends BroadcastListener, CodeletReadable{

	void activateCodelets();

	List<Object> getGuiContent();

}
