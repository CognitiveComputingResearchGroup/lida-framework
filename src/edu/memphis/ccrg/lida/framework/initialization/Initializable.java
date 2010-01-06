package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.LidaModule;

public interface Initializable {
	/**
	 * This method initializes the module using properties from LIDA Properties File
	 * @param lidaProperties
	 */
	public abstract void init (Properties lidaProperties);
	public abstract void setAssociatedModule(LidaModule module);
}
