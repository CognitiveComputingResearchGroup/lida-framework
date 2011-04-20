/**
 * 
 */
package edu.memphis.ccrg.lida.sensorymemory;

import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;

/**
 * @author Daqi
 *
 */

public class SensoryMemoryBackgroundTaskTest {

	/*
	 * Test method for {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#runThisFrameworkTask()}.
	 * @throws Exception e
	 */
	@Test
	public final void testRunThisTask() throws Exception {
		
		MockSensory mSensory = new MockSensory();
		
	    SensoryMemoryBackgroundTask st = new SensoryMemoryBackgroundTask();
	    
	    // Testing of setAssociatedModule method.
	    st.setAssociatedModule(mSensory, ModuleUsage.NOT_SPECIFIED);
	    
	    // Testing of RunThisFrameworkTask method.
	    st.runThisFrameworkTask();
	
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#setAssociatedModule(edu.memphis.ccrg.lida.framework.FrameworkModule, String)}.
	 * @throws Exception e
	 */
	@Test
	public final void testSetAssociatedModule() throws Exception {
		//SetAssociatedModule() be tested in testRunThisFrameworkTask method above with testing of 
		//RunThisFrameworkTask() together.
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#toString()}.
	 */
	@Test
	public final void testToString() {
		
		SensoryMemoryBackgroundTask st = new SensoryMemoryBackgroundTask();
		String sAns = "SensoryMemoryBackgroundTask background task";
		
		if (st.toString().equals(sAns))
			System.out.println("Step 3-3: testToString() is OK");
		else
			System.out.println("Step 3-3: testToString() is NG");

	}
}

// Define a temporal class of implementing Class sensoryMemory for test
class MockSensory implements SensoryMemory, SensoryMotorMemoryListener {
	
	@Override
	public void runSensors() {
		System.out.println("This is mock sensor.");
	    
		System.out.println("Step 3-1: testRunThisFrameworkTask() is OK");
		System.out.println("Step 3-2: testSetAssociatedModule() is OK");
		
	}

	@Override
	public ModuleName getModuleName() {
		// not implemented
		return null;
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		// not implemented
		
	}

	@Override
	public FrameworkModule getSubmodule(ModuleName name) {
		// not implemented
		return null;
	}

	@Override
	public void addSubModule(FrameworkModule lm) {
		// not implemented
		
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
	public void addListener(ModuleListener listener) {
		// not implemented
		
	}

	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts) {
		// not implemented
		
	}

	@Override
	public TaskSpawner getAssistingTaskSpawner() {
		// not implemented
		return null;
	}

	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		// not implemented
		
	}

	@Override
	public void init(Map<String, ?> parameters) {
		// not implemented
		
	}

	@Override
	public void init() {
		// not implemented
		
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		// not implemented
		return null;
	}

	@Override
	public void receiveActuatorCommand(Object algorithm) {
		// not implemented
		
	}

	@Override
	public Object getState() {
		// not implemented
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// not implemented
		return false;
	}

	@Override
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		// not implemented
		
	}

	@Override
	public FrameworkModule getSubmodule(String name) {
		// not implemented
		return null;
	}

	@Override
	public Object getSensoryContent(String modality, Map<String, Object> params) {
		// not implemented
		return null;
	}

}

