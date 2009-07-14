package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.shared.Activatible;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface StructureBuildingCodelet extends Activatible, Runnable, Stoppable{

	 public void setSoughtContent(NodeStructure content);
	 public NodeStructure getSoughtContent();
	
	 public void setCodeletAction(CodeletAction a);
	 public CodeletAction getCodeletAction();

	 public void setId(long id);
	 public long getId();
	
	 public void setAccessibleModules(List<CodeletReadable> readableBuffers, List<CodeletWritable> writableBuffers);
	 public List<CodeletReadable> getAccessibleBuffers();
	
	 public void setSleepTime(int ms);
	 public int getSleepTime();

	 /**
	  * Type is determined by what buffers are accessible to this codelet
	  * @return
	  */
	 public int getType();

	 public void clearForReuse();
	public void addFrameworkTimer(FrameworkTimer timer);

}//interface