package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.ModuleListener;


public interface SensoryMemoryListener extends ModuleListener{

	public void receiveSensoryMemoryContent(Object content);
}
