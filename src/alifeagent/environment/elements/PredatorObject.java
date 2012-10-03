/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package alifeagent.environment.elements;

import edu.memphis.ccrg.alife.elements.ALifeObjectImpl;
import edu.memphis.ccrg.alife.world.ALifeWorld;

public class PredatorObject extends ALifeObjectImpl {

	@Override
	public void updateState(ALifeWorld world) {
		world.performOperation("move", this, null);	
		world.performOperation("attack", this, null,"agent");		
	}

}
