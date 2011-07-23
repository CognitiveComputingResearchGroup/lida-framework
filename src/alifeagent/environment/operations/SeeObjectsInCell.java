/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package alifeagent.environment.operations;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.opreations.WorldOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;

public class SeeObjectsInCell implements WorldOperation {

	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
			ALifeObject[] object, Object... params) {
		int x = (Integer) params[0];
		int y = (Integer) params[1];
		return world.getCell(x, y).getObjects();
	}
}
