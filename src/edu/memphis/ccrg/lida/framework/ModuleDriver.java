package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

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
public interface ModuleDriver extends TaskSpawner, GuiEventProvider{

}
