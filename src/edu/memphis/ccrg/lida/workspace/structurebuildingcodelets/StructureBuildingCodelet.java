package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface StructureBuildingCodelet extends LidaTask{

	 public void setSoughtContent(NodeStructure content);
	 public NodeStructure getSoughtContent();
	
	 public void setCodeletAction(CodeletAction a);
	 public CodeletAction getCodeletAction();

	 public void addReadableBuffer(List<NodeStructure> buffer);
	 public List<List<NodeStructure>> getReadableBuffers();
	 public void addWritableModule(CodeletWritable module);
	 public List<CodeletWritable> getWriteableBuffers();
	
	 /**
	  * Type is determined by what buffers are accessible to this codelet
	  * @return
	  */
	 public int getType();

	 public void addFrameworkTimer(LidaTaskManager timer);

}//interface