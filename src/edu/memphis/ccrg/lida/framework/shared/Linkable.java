package edu.memphis.ccrg.lida.framework.shared;

import java.io.Serializable;
import java.util.Map;

/**
 * A object that can have links attached to it
 * @author Javier Snaider
 *
 */
public interface Linkable extends Serializable {

	/**
	 * Readable label
	 */
	public abstract String getLabel();
	
	/**
	 * Id in String form
	 */
	public abstract String getIds();
	
	/**
	 * 
	 * @param params
	 */
	public abstract void init(Map<String,Object> params);
}
