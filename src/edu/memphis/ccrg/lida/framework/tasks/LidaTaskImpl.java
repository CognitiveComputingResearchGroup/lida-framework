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
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

/**
 * This class implements the LidaTask Interface. This class should be used as the base class for all LidaTasks.
 * @author Javier Snaider
 */
public abstract class LidaTaskImpl extends LearnableImpl implements LidaTask {

	private static final Logger logger= Logger.getLogger(LidaTaskImpl.class.getCanonicalName());

	private final static int defaultTicksPerStep = 1;
	private int ticksPerStep = defaultTicksPerStep;
	private long taskID;
	private long nextExcecutionTicksPerStep = defaultTicksPerStep;
	protected LidaTaskStatus status = LidaTaskStatus.WAITING;
	private Map<String, ? extends Object> parameters;
	private TaskSpawner ts;
	private long scheduledTick;
	
	/**
	 * Source of unique Ids for LidaTaskImpls
	 */
	private static long nextTaskID;
	
	/**
	 * Creates a LidaTaskImpl with default ticks per step
	 */
	public LidaTaskImpl() {
		this(defaultTicksPerStep,null);
	}
	public LidaTaskImpl(int ticksPerStep) {
		this(ticksPerStep,null);
	}
	public LidaTaskImpl(int ticksPerStep,TaskSpawner ts) {
		taskID = nextTaskID++;
		this.ts=ts;
		setTicksPerStep(ticksPerStep);
	}
	
	@Override
	public long getScheduledTick() {
		return scheduledTick;
	}

	@Override
	public void setScheduledTick(long scheduledTick) {
		this.scheduledTick = scheduledTick;
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
			logger.log(Level.WARNING, "Exception " + e.toString() + " encountered in task " + this.toString(), LidaTaskManager.getCurrentTick());
			e.printStackTrace();
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
	 */
	protected abstract void runThisLidaTask();

	@Override
	public void setTaskStatus(LidaTaskStatus status) {
		if (this.status != LidaTaskStatus.CANCELED)
			this.status = status;
		else
			logger.log(Level.WARNING, "Cannot set task status to CANCELED", LidaTaskManager.getCurrentTick());
	}

	@Override
	public LidaTaskStatus getStatus() {
		return status;
	}

	@Override
	public long getTaskId() {
		return taskID;
	}

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

	@Override
	public void setTicksPerStep(int ticks) {
		if (ticks > 0){
			ticksPerStep = ticks;
			setNextTicksPerStep(ticks);
		}
	}

	@Override
	public void stopRunning() {
		setTaskStatus(LidaTaskStatus.CANCELED);
	}
	
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
	
	@Override
	public void setNextTicksPerStep(long lapTick) {
		this.nextExcecutionTicksPerStep=lapTick;	
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, String moduleUsage) {
	}	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof LidaTaskImpl){
			return taskID == ((LidaTaskImpl) o).getTaskId();
		}
		return false;
	}
	@Override
	public int hashCode(){
		return (int) taskID;
	}
	
}
