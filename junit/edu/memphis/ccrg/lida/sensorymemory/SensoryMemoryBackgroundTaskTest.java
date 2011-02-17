/**
 * 
 */
package edu.memphis.ccrg.lida.sensorymemory;

import java.lang.reflect.Field;
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
		
	    SensoryMemoryBackgroundTask st = new SensoryMemoryBackgroundTask();
	    SensoryMemoryBackgroundTask st2 = new SensoryMemoryBackgroundTask();
	    
	    //If initialized sm
		Field field1 = st.getClass().getDeclaredField("sm");
		field1.setAccessible(true);
		TmpClass tc = new TmpClass();
		field1.set(st, tc);
		st.runThisLidaTask();
		
	    //If does not initialized sm
		//FIXCODE This sentence should cause NullPointerException error,
		//because there is an uninitialized error of variable sm
	    st2.runThisLidaTask();
	    
		System.out.println("Step 3-1: testRunThisLidaTask() is OK");
	
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryBackgroundTask#setAssociatedModule(edu.memphis.ccrg.lida.framework.LidaModule, int)}.
	 * @throws Exception e
	 */
	@Test
	public final void testSetAssociatedModule() throws Exception {
		
		SensoryMemory sModule = new TmpClass();
		LidaModule lModule = new TmpClass2();
		SensoryMemoryBackgroundTask st = new SensoryMemoryBackgroundTask();
		
		Field field1 = st.getClass().getDeclaredField("sm");
		field1.setAccessible(true);

		// Variable sm will be assigned cause of passing a parameter that is type of SensoryMemory
		field1.set(st, null);
		st.setAssociatedModule(sModule, 0);
		if (field1.get(st) instanceof SensoryMemory) 
			System.out.println("Step 3-2: testSetAssociatedModule() is OK (1/2)");
		else 
			System.out.println("Step 3-2: testSetAssociatedModule() is NG (1/2)");

		// Variable sm will not be assigned cause of doing not pass a parameter that is type of SensoryMemory
		field1.set(st, null);
		st.setAssociatedModule(lModule, 0);
		if (field1.get(st) == null) 
			System.out.println("Step 3-2: testSetAssociatedModule() is OK (2/2)");
		else 
			System.out.println("Step 3-2: testSetAssociatedModule() is NG (2/2)");
		
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
class TmpClass implements SensoryMemory, SensoryMotorMemoryListener {

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
	public void receiveExecutingAlgorithm(Object algorithm) {
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
	public void runSensors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		// TODO Auto-generated method stub
		
	}

}

//Define a temporal class of implementing Class LidaModule for test
class TmpClass2 implements LidaModule {

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
	
}
