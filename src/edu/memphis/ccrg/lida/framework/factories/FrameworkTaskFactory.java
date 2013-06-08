/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.FrameworkTaskDef;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;

/**
 * The interface for a FrameworkTaskFactory. A FrameworkTaskFactory supplies
 * objects implementing the {@link FrameworkTask} interface. The details of the
 * object creation may be parameterized based on details supplied by the caller. 
 * <br><br>
 * Classes implementing the FrameworkTaskFactory interface must implement the
 * {@link InitializableFactory}.
 * 
 * @author Sean Kugele
 * 
 */
public interface FrameworkTaskFactory extends InitializableFactory {

    /**
     * Returns a new {@link FrameworkTask} of the requested type that has the
     * default parameters for that type.
     * 
     * @param type
     *            type of FrameworkTask
     * @return the new {@link FrameworkTask}
     */
    public FrameworkTask getFrameworkTask(String type);

    /**
     * Returns a new {@link FrameworkTask} of the requested type parameterized
     * by the specified parameters.
     * 
     * @param type
     *            type of FrameworkTask
     * @param params
     *            optional parameters to be set in object's init method
     * @return the new {@link FrameworkTask}
     */
    public FrameworkTask getFrameworkTask(String type, Map<String, ? extends Object> params);

    /**
     * Returns a new {@link FrameworkTask} having specified attributes and
     * module associations.
     * 
     * @param taskType
     *            type of FrameworkTask
     * @param params
     *            optional parameters to be set in object's init method
     * @param modules
     *            map of modules for association.
     * 
     * @return the new {@link FrameworkTask}
     */
    public FrameworkTask getFrameworkTask(String taskType,
            Map<String, ? extends Object> params, Map<ModuleName, FrameworkModule> modules);

    /**
     * Adds the {@link FrameworkTask} type.
     * 
     * @param taskDef
     *            {@link FrameworkTaskDef}
     */
    public void addFrameworkTaskType(FrameworkTaskDef taskDef);

    /**
     * Returns whether this factory contains specified {@link FrameworkTask}
     * type.
     * 
     * @param type
     *            String
     * @return true if factory contains type or false if not
     */
    public boolean containsTaskType(String type);
}