package edu.memphis.ccrg.lida.framework.gui.commands;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;

public abstract class GenericCommandImpl implements Command {
	
	private Map<String,Object> parameters=new HashMap<String,Object>();
	protected Object result;

	public abstract void execute(Lida lida);

	public Object getParameter(String name) {
		return parameters.get(name);
	}

	public Object getResult() {
		return result;
	}

	public void setParameter(String name, Object value) {
		parameters.put(name, value);
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

}
