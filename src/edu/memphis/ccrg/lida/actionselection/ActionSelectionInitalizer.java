/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;
 
import java.util.Map;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;

/**
 * @author ryanjmccall
 *
 */
public class ActionSelectionInitalizer implements Initializer{

	private static final Logger logger = Logger.getLogger(ActionSelectionInitalizer.class.getCanonicalName());

	/**
	 * 
	 */
	public ActionSelectionInitalizer() {
	}

	@Override
	public void initModule(Initializable module,Lida lida, Map<String, ?> params){		
		ActionSelection actionSelection =(ActionSelection)module;
	}

}
