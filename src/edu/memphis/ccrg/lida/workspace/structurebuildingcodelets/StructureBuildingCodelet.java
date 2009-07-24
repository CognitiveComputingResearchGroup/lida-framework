package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.List;
import java.util.concurrent.Callable;

import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.shared.Activatible;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface StructureBuildingCodelet extends Activatible, Callable<Object>, LidaTask{

	 public void setSoughtContent(NodeStructure content);
	 public NodeStructure getSoughtContent();
	
	 public void setCodeletAction(CodeletAction a);
	 public CodeletAction getCodeletAction();

	 public void setId(long id);
	 public long getId();
	
	 public void addReadableBuffer(List<NodeStructure> buffer);
	 public List<List<NodeStructure>> getReadableBuffers();
	 public void addWritableModule(CodeletWritable module);
	 public List<CodeletWritable> getWriteableBuffers();
	
	 public void setSleepTime(int ms);
	 public int getSleepTime();

	 /**
	  * Type is determined by what buffers are accessible to this codelet
	  * @return
	  */
	 public int getType();

	 public void clearForReuse();
	 public void addFrameworkTimer(FrameworkTaskManager timer);

}//interface