package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface CodeletAction {

	public void performAction(List<NodeStructure> buffer, CodeletWritable destination);
	
}
  