package edu.memphis.ccrg.lida.framework;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
//TODO: Delete this and rename ModuleDriverImpl to ModuleDriver
/**
 * A ModuleDriver is a class that runs a major module of the 
 * framework such as PAM or Procedural Memory.
 * 
 * The main difference b/w it an a LidaTask is that a LidaTask
 * may not be constantly running while a ModuleDriver is always running.
 * 
 * @author Ryan J. McCall
 *
 */
public interface ModuleDriver extends Initializable,GuiEventProvider, TaskSpawner{

	public abstract ModuleName getModuleName();
	public abstract void setModuleName(ModuleName name);
	public abstract void setTaskManager(LidaTaskManager taskManager);
	public abstract void setAssociatedModule(LidaModule module);

}
