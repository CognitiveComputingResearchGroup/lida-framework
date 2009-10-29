package edu.memphis.ccrg.lida.framework;

import java.util.Properties;

public interface LidaModule {

	public abstract void init (Properties lidaProperties);
	public abstract ModuleType getModuleType();
}
