package edu.memphis.ccrg.lida.framework;

public interface Lida extends LidaModule{

	/**
	 * @return the LidaTaskManager
	 */
	public abstract LidaTaskManager getTaskManager();

	/**
	 * @param driver The ModuleDriver to add.
	 */
	public abstract void addModuleDriver(ModuleDriver driver);

	/**
	 * @param name The module name. 
	 * @return the ModuleDriver
	 */
	public abstract ModuleDriver getModuleDriver(ModuleName name);

	public abstract void start();

}