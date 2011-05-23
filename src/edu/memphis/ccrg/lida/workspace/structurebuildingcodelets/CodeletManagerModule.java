package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;

/**
 * A {@link FrameworkModule} that manages {@link Codelet}s
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public interface CodeletManagerModule extends FrameworkModule{

	/**
	 * Returns a new {@link Codelet} of the default type of the Manager
	 * @param params Optional parameters or null
	 * @return new default {@link Codelet} 
	 */
	public Codelet getDefaultCodelet(Map<String, Object> params);
	
	/**
	 * Returns a new {@link Codelet} of the default type of the Manager
	 * @return new default {@link Codelet}
	 */
	public Codelet getDefaultCodelet();

	/**
	 * Returns a new {@link Codelet} of the specified type or null if this type is 
	 * not supported.
	 * @param type Codelet type
	 * @see ElementFactory
	 * @return new Codelet of specified type or null if type is not supported
	 */
	public Codelet getCodelet(String type);
	
	/**
	 * Returns a new {@link Codelet} of the specified type or null if this type is 
	 * not supported.
	 * @param type Codelet type
	 * @param params Optional parameters or null
	 * @return new Codelet of specified type or null if type is not supported
	 */
	public Codelet getCodelet(String type, Map<String, Object> params);
	
	/**
	 * Adds specified {@link Codelet} to this module to be run.
	 * @param cod Codelet to be added to run
	 */
	public void addCodelet(Codelet cod);
	
	
	/**
	 * Sets default codelet type used by this Manager.
	 * @param type Codelet type
	 * @see ElementFactory
	 */
	public void setDefaultCodeletType(String type);

}