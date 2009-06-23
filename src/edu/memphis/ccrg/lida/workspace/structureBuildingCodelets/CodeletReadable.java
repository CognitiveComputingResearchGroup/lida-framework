package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public interface CodeletReadable {

	WorkspaceContent lookForContent(NodeStructure soughtContent);

}
