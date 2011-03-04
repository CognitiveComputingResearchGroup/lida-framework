/**
 * 
 */
package edu.memphis.ccrg.lida.sensorymemory;

import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;

/**
 * @author Daqi
 *
 */

public class SensoryMemoryBackgroundTaskTest {

	/*
	 * Test method for {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#runThisLidaTask()}.
	 * @throws Exception e
	 */
	@Test
	public final void testRunThisLidaTask() throws Exception {
		
		MockSensory mSensory = new MockSensory();
		
	    SensoryMemoryBackgroundTask st = new SensoryMemoryBackgroundTask();
	    
	    // Testing of setAssociatedModule method.
	    st.setAssociatedModule(mSensory, 1);
	    
	    // Testing of RunThisLidaTask method.
	    st.runThisLidaTask();
	
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#setAssociatedModule(edu.memphis.ccrg.lida.framework.LidaModule, int)}.
	 * @throws Exception e
	 */
	@Test
	public final void testSetAssociatedModule() throws Exception {
		//SetAssociatedModule() be tested in testRunThisLidaTask method above with testing of 
		//RunThisLidaTask() together.
		
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
	    
		System.out.println("Step 3-1: testRunThisLidaTask() is OK");
		System.out.println("Step 3-2: testSetAssociatedModule() is OK");
		
	}

	@Override
	public ModuleName getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LidaModule getSubmodule(ModuleName name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSubModule(LidaModule lm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getModuleContent(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decayModule(long ticks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaskSpawner getAssistingTaskSpawner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Map<String, ?> parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveActuatorCommand(Object algorithm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getSensoryContent(String modality, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

}

