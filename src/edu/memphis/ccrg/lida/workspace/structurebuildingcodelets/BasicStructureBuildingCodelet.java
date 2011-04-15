package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Default implementation of {@link StructureBuildingCodelet}.  Checks for sought content
 * in all accessible {@link WorkspaceBuffer}s and adds all buffer content to the Current Situational Model. 
 * @author Ryan J. McCall
 *
 */
public class BasicStructureBuildingCodelet extends StructureBuildingCodeletImpl {
	
	private static Logger logger = Logger.getLogger(BasicStructureBuildingCodelet.class.getCanonicalName());
	private Map<String, Object> resultMap;
	
	public BasicStructureBuildingCodelet(){
		super();
		resultMap = new HashMap<String, Object>();
	}
	
	@Override
	protected void runThisLidaTask(){	
		logger.log(Level.FINEST, "SB codelet " + this.toString() + " being run.", 
				LidaTaskManager.getCurrentTick());
		for(WorkspaceBuffer readableBuffer: readableBuffers){
			if(hasSoughtContent(readableBuffer)){
				writableBuffer.addBufferContent((WorkspaceContent) retrieveWorkspaceContent(readableBuffer));
			}
			writableBuffer.addBufferContent(readableBuffer.getBufferContent(null));
		}
		results.reportRunResults(resultMap);
		logger.log(Level.FINEST, "SB codelet " + this.toString() + " finishes one run.",
				LidaTaskManager.getCurrentTick());
	}

	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		return buffer.getBufferContent(null);
	}

	@Override
	public boolean hasSoughtContent(WorkspaceBuffer buffer) {
		NodeStructure ns = (NodeStructure) buffer.getBufferContent(null);
		for(Linkable ln: soughtContent.getLinkables()){
			if(!ns.containsLinkable(ln)){
				return false;
			}
		}
		logger.log(Level.FINEST, "SBcodelet " + this.toString() + " found sought content", LidaTaskManager.getCurrentTick());
		return true;
	}

}
