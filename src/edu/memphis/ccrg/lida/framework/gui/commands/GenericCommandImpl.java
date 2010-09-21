/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.commands;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;

public abstract class GenericCommandImpl implements Command {
	
	private Map<String,Object> parameters=new HashMap<String,Object>();
	protected Object result;

	public abstract void execute(Lida lida);

	public Object getParameter(String name) {
		Object res=null;
		if (parameters!=null){
			res=parameters.get(name);
		}
		return res;
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
