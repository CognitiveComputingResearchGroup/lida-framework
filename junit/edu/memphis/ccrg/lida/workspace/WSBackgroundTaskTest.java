package edu.memphis.ccrg.lida.workspace;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

import edu.memphis.ccrg.lida.workspace.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class WSBackgroundTaskTest {

	@Test
	public final void testRunThisLidaTask() throws Exception{
		Workspace wModule = new TmpClass();
		WSBackgroundTask wst = new WSBackgroundTask();
		
		Field field1 = wst.getClass().getDeclaredField("workspace");
		field1.setAccessible(true);
		
		field1.set(wst, wModule);
		
		//NullPointerException happened here.
		//TODO Judge that in the method cue() of class WSBackgroundTask, 
		//whether perceptualBuffer need to be checked for NULL.
		wst.runThisLidaTask();
		
		System.out.println("Step 4-1: testRunThisLidaTask() is OK");

	}

	@Test
	public final void testInit() throws Exception {
		
		WSBackgroundTask wst = new WSBackgroundTask();
		
		Field field1 = wst.getClass().getDeclaredField("workspace");
		Field field2 = wst.getClass().getDeclaredField("actThreshold");
		Field field3 = wst.getClass().getDeclaredField("cueFrequency");
		field1.setAccessible(true);
		field2.setAccessible(true);
		field3.setAccessible(true);

		// Initialize with default value
		wst.init();
		
		System.out.println("Step 4-2: testInitis reusult is: ");
		System.out.println("The actThreshold is " + field2.get(wst));
		System.out.println("The cueFrequency is " + field3.get(wst));

		// Initialize with assigned vale
		Map<String, Object> mapParas = new HashMap();
		mapParas.put("workspace.actThreshold", 0.5);
		mapParas.put("workspace.cueFrequency", 2);
		wst.init(mapParas);
		
		wst.init();
		System.out.println("Step 4-2: testInitis reusult is: ");
		System.out.println("The actThreshold is " + field2.get(wst));
		System.out.println("The cueFrequency is " + field3.get(wst));

	}

	@Test
	public final void testSetAssociatedModule() throws Exception {
		
		Workspace wModule = new TmpClass();
		LidaModule lModule = new TmpClass2();
		WSBackgroundTask wst = new WSBackgroundTask();
		
		Field field1 = wst.getClass().getDeclaredField("workspace");
		field1.setAccessible(true);

		// Variable workspace will be assigned cause of passing a parameter that is type of Workspace
		field1.set(wst, null);
		wst.setAssociatedModule(wModule, 0);
		if (field1.get(wst) == null) 
			System.out.println("Step 4-3: testSetAssociatedModule() is OK (1/3)");
		else 
			System.out.println("Step 4-3: testSetAssociatedModule() is NG (1/3)");
		
		// Variable workspace will be assigned cause of passing a parameter that is type of Workspace
		// and set module name to Workspace
		field1.set(wst, null);
		wModule.setModuleName(ModuleName.Workspace);
		wst.setAssociatedModule(wModule, 0);
		if (field1.get(wst) instanceof Workspace) 
			System.out.println("Step 4-3: testSetAssociatedModule() is OK (2/3)");
		else 
			System.out.println("Step 4-3: testSetAssociatedModule() is NG (2/3)");

		// Variable workspace will not be assigned cause of doing not pass a parameter that is type of SensoryMemory
		field1.set(wst, null);
		wst.setAssociatedModule(lModule, 0);
		if (field1.get(wst) == null) 
			System.out.println("Step 4-3: testSetAssociatedModule() is OK (3/3)");
		else 
			System.out.println("Step 4-3: testSetAssociatedModule() is NG (3/3)");

	}

	@Test
	public final void testToString() {
		WSBackgroundTask wst = new WSBackgroundTask();
		String sAns = "WSBackgroundTask";
		
		if (wst.toString().equals(sAns))
			System.out.println("Step 4-4: testToString() is OK");
		else
			System.out.println("Step 4-4: testToString() is NG");

	}

}

//Define a temporal class of implementing Class Workspace for test
class TmpClass implements Workspace {
	
	private ModuleName moduleName;

	@Override
	public ModuleName getModuleName() {
		// TODO Auto-generated method stub
		//return null;
		return moduleName;
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		// TODO Auto-generated method stub
		this.moduleName = moduleName;
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
	public void addCueListener(CueListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWorkspaceListener(WorkspaceListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cueEpisodicMemories(NodeStructure ns) {
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
