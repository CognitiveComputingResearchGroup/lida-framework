package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;

/**
 * A {@link LidaModule} that manages {@link Codelet}s
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public interface CodeletManagerModule extends LidaModule{

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

	//TODO next version
//	/**
//	 * Recycles the given codelet to be re-used later on, for quickly providing
//	 * new empty codelets.
//	 * 
//	 * @param sbCode The codelet to be recycled
//	 */
//	public void recycle(Codelet cod);

}