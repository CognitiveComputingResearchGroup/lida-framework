/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.sensorymemory;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;

/**
 * @author Daqi
 * 
 */

public class SensoryMemoryBackgroundTaskTest {

	/*
	 * Test method for {@link
	 * edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask
	 * #runThisFrameworkTask()}.
	 * 
	 * @throws Exception e
	 */
	@Test
	public final void testRunThisTask() throws Exception {

		MockSensory mSensory = new MockSensory();

		SensoryMemoryBackgroundTask st = new SensoryMemoryBackgroundTask();

		// Testing of setAssociatedModule method.
		st.setAssociatedModule(mSensory, ModuleUsage.NOT_SPECIFIED);

		// Testing of RunThisFrameworkTask method.
		mSensory.setFlag(false);
		st.runThisFrameworkTask();
		assertTrue(
				"Problem with class SensoryMemoryBackgroundTask for runThisTask()",
				mSensory.getFlag() == true);

	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#setAssociatedModule(edu.memphis.ccrg.lida.framework.FrameworkModule, String)}
	 * .
	 * 
	 * @throws Exception
	 *             e
	 */
	@Test
	public final void testSetAssociatedModule() throws Exception {
		// SetAssociatedModule() be tested in testRunThisFrameworkTask method
		// above with testing of
		// RunThisFrameworkTask() together.

	}

}

// Define a temporal class of implementing Class sensoryMemory for test
class MockSensory extends FrameworkModuleImpl implements SensoryMemory,
		SensoryMotorMemoryListener {

	private boolean flag = false;

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean getFlag() {
		return flag;
	}

	@Override
	public void runSensors() {
		flag = true;

	}

	@Override
	public Object getModuleContent(Object... params) {
		// not implemented
		return null;
	}

	@Override
	public void decayModule(long ticks) {
		// not implemented

	}

	@Override
	public void receiveActuatorCommand(Object algorithm) {
		// not implemented

	}

	@Override
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		// not implemented

	}

	@Override
	public Object getSensoryContent(String modality, Map<String, Object> params) {
		// not implemented
		return null;
	}

}
