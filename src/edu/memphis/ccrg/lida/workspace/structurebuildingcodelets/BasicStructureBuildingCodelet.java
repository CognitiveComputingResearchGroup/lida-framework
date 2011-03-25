package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

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
			writableBuffer.addBufferContent(readableBuffer.getBufferContent(null));
		}
		results.reportRunResults(resultMap);
		logger.log(Level.FINEST, "SB codelet " + this.toString() + " finishes one run.",
				LidaTaskManager.getCurrentTick());
	}

}
