package edu.memphis.ccrg.lida.declarativememory;

import edu.memphis.ccrg.lida.framework.ModuleListener;

public interface DeclarativeMemoryListener extends ModuleListener{
	
	public abstract void receivenDMContent(DeclarativeMemoryContent association);

}
