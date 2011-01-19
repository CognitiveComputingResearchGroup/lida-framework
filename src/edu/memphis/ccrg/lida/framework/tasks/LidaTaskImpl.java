/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;

/**
 * This class implements the LidaTask Interface. This class should be used as the base class for all LidaTasks.
 * @author Javier Snaider
 */
public abstract class LidaTaskImpl extends ActivatibleImpl implements LidaTask {

	private static final Logger logger= Logger.getLogger(LidaTaskImpl.class.getCanonicalName());

	private static int defaultTicksPerStep = 1;
	private long taskID;
	private int ticksPerStep = defaultTicksPerStep;
	private long nextExcecutionTicksPerStep = defaultTicksPerStep;
	protected LidaTaskStatus status = LidaTaskStatus.WAITING;
	private Map<String, ? extends Object> parameters;
	private TaskSpawner ts;
	private long scheduledTick;
	private static long nextTaskID;
	
	public LidaTaskImpl() {
		this(defaultTicksPerStep,null);
	}
	public LidaTaskImpl(int ticksForCycle) {
		this(ticksForCycle,null);
	}
	public LidaTaskImpl(int ticksForCycle,TaskSpawner ts) {
		taskID = nextTaskID++;
		this.ts=ts;
		setTicksPerStep(ticksForCycle);
	}
	
	/**
	 * @return the scheduledTick
	 */
	@Override
	public long getScheduledTick() {
		return scheduledTick;
	}
	/**
	 * @param scheduledTick the scheduledTick to set
	 */
	@Override
	public void setScheduledTick(long scheduledTick) {
		this.scheduledTick = scheduledTick;
	}
	
	/**
	 * @return the defaultTicksPerStep
	 */
	public static int getDefaultTicksPerStep() {
		return defaultTicksPerStep;
	}
	
	
	/**
	 * Sets the default number of ticks that is used in the constructor without this parameter.
	 * @param defaultTicksPerStep default number of ticks
	 */
	public static void setDefaultTicksPerStep(int defaultTicksPerStep) {
		if (defaultTicksPerStep > 0) {
			LidaTaskImpl.defaultTicksPerStep = defaultTicksPerStep;
		}else{
			logger.log(Level.WARNING, 
					"Cannot set default ticks per step to a negative value or 0. Default ticks per step was not changed.", LidaTaskManager.getCurrentTick());
		}
	}

	/** 
	 * This method is not supposed to be called directly nor overwritten.
	 * Overwrite the runThisLidaTask method.
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public LidaTask call() {
		nextExcecutionTicksPerStep=ticksPerStep;
		
		try{
			runThisLidaTask();
		}catch(Exception e){
			e.printStackTrace();
			logger.log(Level.WARNING, "Exception " + e.toString() + " encountered in task " + this.toString(), LidaTaskManager.getCurrentTick());
		}
		
		if (ts != null) 
			ts.receiveFinishedTask(this);
		else 
			logger.log(Level.WARNING, "This task {1} doesn't have an assigned TaskSpawner",new Object[] { LidaTaskManager.getCurrentTick(), this });
		
		return this;
	}


	/**
	 * To be overridden by extending classes. Overriding method should execute a
	 * handful of statements considered to constitute a single iteration of the
	 * task. For example, a codelet might look in a buffer for some
	 * representation and make a change to it in a single iteration.
	 * 
	 */
	protected abstract void runThisLidaTask();

	/**
	 * Sets the task status. Intended to be called by {@link #runThisLidaTask()} 
	 * Cannot use this method to cancel the task, instead use {@link #stopRunning()} to 
	 * cancel the task.       
	 * @param status the new task status 
	 */
	@Override
	public void setTaskStatus(LidaTaskStatus status) {
		if (this.status != LidaTaskStatus.CANCELED)
			this.status = status;
		else
			logger.log(Level.WARNING, "Cannot set task status to CANCELED", LidaTaskManager.getCurrentTick());
	}

	/**
	 * Returns status
	 * @return current LidaTask status
	 */
	@Override
	public LidaTaskStatus getStatus() {
		return status;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#getTaskId()
	 */
	@Override
	public long getTaskId() {
		return taskID;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#setTaskID(long)
	 */
	@Override
	public void setTaskID(long id) {
		taskID = id;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#getTicksPerStep()
	 */
	@Override
	public int getTicksPerStep() {
		return ticksPerStep;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#setTicksPerStep(int)
	 */
	@Override
	public void setTicksPerStep(int ticks) {
		if (ticks > 0){
			ticksPerStep = ticks;
			setNextTicksPerStep(ticks);
		}
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#stopRunning()
	 */
	@Override
	public void stopRunning() {
		setTaskStatus(LidaTaskStatus.CANCELED);
	}
	
	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#init(Map)
	 */
	@Override
	public void init(Map<String, ?> parameters) {
		this.parameters = parameters;
		init();
	}

	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the LidaTask
	 */
	@Override
	public void init() {
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getParameter(java.lang.String)
	 */
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			logger.log(Level.WARNING, "Missing parameter, check factories data or first parameter: " + name, LidaTaskManager.getCurrentTick());
			value = defaultValue;
		}
		return value;
	}
	

	/* 
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getTaskSpawner()
	 */
	@Override
	public TaskSpawner getControllingTaskSpawner() {		
		return ts;
	}

	/*
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#setControllingTaskSpawner(edu.memphis.ccrg.lida.framework.TaskSpawner)
	 */
	@Override
	public void setControllingTaskSpawner(TaskSpawner ts) {
		this.ts=ts;		
	}
	
	@Override
	public long getNextTicksPerStep() {		
		return nextExcecutionTicksPerStep;
	}
	
	/**
	 * For just the next execution of this task, sets the number of ticks in the future when this task will be run
	 */
	@Override
	public void setNextTicksPerStep(long lapTick) {
		this.nextExcecutionTicksPerStep=lapTick;	
	}
	
	/**
	 * Sets an associated LidaModule. Each extending class can implement its own version.
	 * @param module the module to be associated.
     * @param moduleUsage how module will be used @see ModuleUsage
	 */
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
	}	
	
}
