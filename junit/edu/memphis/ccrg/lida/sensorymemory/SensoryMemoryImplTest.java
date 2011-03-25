/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.sensorymemory;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * This class is the JUnit test for <code>SensoryMemoryImpl</code> class.
 * @author Rodrigo Silva-Lugo
 */
public class SensoryMemoryImplTest {

    /**
     * 
     */
    public SensoryMemoryImplTest() {
        //sensoryMemory = new ;
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of addListener method, of class SensoryMemoryImpl.
     */
    @Test
    public void testAddListener() {
        final int NUM_LIS_TO_ADD = 1;
        System.out.println("addListener");
        final MockSensoryMemoryListener SM_LISTENER
                = new MockSensoryMemoryListener();
        MockSensoryMemoryImpl instance = new MockSensoryMemoryImpl();
        final int NUM_LIS_BEFORE = instance.getListeners().size();
        // Pre: instance has 0 listeners.
        assertEquals(NUM_LIS_BEFORE, 0);
        instance.addListener(SM_LISTENER);
        final int NUM_LIS_AFTER = instance.getListeners().size();
        // Pos: instance has 1 SM_LISTENER, same as added.
        assertEquals(NUM_LIS_AFTER, NUM_LIS_BEFORE + NUM_LIS_TO_ADD);
        assertEquals(SM_LISTENER, instance.getListeners().get(0));
        // TODO test adding 2 listeners, one for SMM, and one for PAM
    }

    /**
     * Test of addSensoryMemoryListener method, of class SensoryMemoryImpl.
     */
    @Test
    public void testAddSensoryMemoryListener() {
        final int NUM_LIS_TO_ADD = 1;
        System.out.println("addListener");
        final MockSensoryMemoryListener SM_LISTENER
                = new MockSensoryMemoryListener();
        MockSensoryMemoryImpl instance = new MockSensoryMemoryImpl();
        final int NUM_LIS_BEFORE = instance.getListeners().size();
        // Pre: instance has 0 listeners.
        assertEquals(NUM_LIS_BEFORE, 0);
        instance.addSensoryMemoryListener(SM_LISTENER);
        final int NUM_LIS_AFTER = instance.getListeners().size();
        // Pos: instance has 1 SM_LISTENER, same as added.
        assertEquals(NUM_LIS_AFTER, NUM_LIS_BEFORE + NUM_LIS_TO_ADD);
        assertEquals(SM_LISTENER, instance.getListeners().get(0));
        // TODO test adding 2 listeners, one for SMM, and one for PAM
    }

    /**
     * Test of setAssociatedModule method, of class SensoryMemoryImpl.
     */
    @Test
    public void testSetAssociatedModule() {
        // Pre: instance has 0 associated modules.
        // Pos: instance has 1 associated module, same as added.
        // Test for SMM.
        System.out.println("setAssociatedModule");
        LidaModule module = null;
        String moduleUsage = ModuleUsage.NOT_SPECIFIED;
        SensoryMemoryImpl instance = new MockSensoryMemoryImpl();
        instance.setAssociatedModule(module, moduleUsage);
        // TODO review the generated test code and remove the default call to fail.
    }

    //HEY!! You don't need to test this method.  ~Ryan
//    /**
//     * Test of decayModule method, of class SensoryMemoryImpl.
//     */
//    @Test
//    public void testDecayModule() {
//        // Pre: instance has activation A.
//        // Pos: instance has activation A', A' < A.
//        System.out.println("decayModule");
//        long ticks = 0L;
//        SensoryMemoryImpl instance = new MockSensoryMemoryImpl();
//        Object SMContent = instance.getModuleContent();
//        instance.decayModule(ticks);
//        // TODO review the generated test code and remove the default call to fail.
//    }

    /**
     *
     */
    public class MockSensoryMemoryImpl extends SensoryMemoryImpl {

        public List<SensoryMemoryListener> getListeners() {
            return listeners;
        }

        @Override
        public void init() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void runSensors() {
            throw new UnsupportedOperationException("Not supported yet.");
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
		public Object getModuleContent(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getSensoryContent(String modality,
				Map<String, Object> params) {
			// TODO Auto-generated method stub
			return null;
		}
    }

    /**
     *
     */
    public class MockSensoryMemoryListener implements SensoryMemoryListener {

        /**
         *
         * @param content
         */
        @Override
        public void receiveSensoryMemoryContent(Object content) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    public class MockEnvironmentImpl implements Environment {

        @Override
        public void resetState() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void processAction(Object algorithm) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ModuleName getModuleName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setModuleName(ModuleName moduleName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public LidaModule getSubmodule(ModuleName name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void addSubModule(LidaModule lm) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getModuleContent(Object... params) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void decayModule(long ticks) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void addListener(ModuleListener listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setAssistingTaskSpawner(TaskSpawner ts) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TaskSpawner getAssistingTaskSpawner() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setAssociatedModule(LidaModule module, String moduleUsage) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void init(Map<String, ?> parameters) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void init() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getParam(String name, Object defaultValue) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

		@Override
		public Object getState(Map<String, ?> params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public LidaModule getSubmodule(String name) {
			// TODO Auto-generated method stub
			return null;
		}

    }
}