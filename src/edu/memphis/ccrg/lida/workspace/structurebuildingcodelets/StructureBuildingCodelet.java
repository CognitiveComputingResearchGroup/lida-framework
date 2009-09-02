package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface StructureBuildingCodelet extends LidaTask{

	 public void setSoughtContent(NodeStructure content);
	 public NodeStructure getSoughtContent();
	
	 public void setCodeletAction(CodeletAction a);
	 public CodeletAction getCodeletAction();

	 public void addReadableBuffer(NodeStructure buffer);
	 public List<Collection<NodeStructure>> getReadableBuffers();
	 public void addWritableModule(CodeletWritable module);
	 public List<CodeletWritable> getWriteableBuffers();
	
	 /**
	  * Type is determined by what buffers are accessible to this codelet
	  * @return
	  */
	 public int getType();

}//interface