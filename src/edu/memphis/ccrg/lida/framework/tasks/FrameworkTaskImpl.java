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
package edu.memphis.ccrg.lida.framework.tasks;
 
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

/**
 * This class implements the FrameworkTask Interface. This class should be used as the base class for all FrameworkTasks.
 * @author Javier Snaider
 */
public abstract class FrameworkTaskImpl extends LearnableImpl implements FrameworkTask {

	private static final Logger logger= Logger.getLogger(FrameworkTaskImpl.class.getCanonicalName());

	private final static int defaultTicksPerStep = 1;
	private int ticksPerStep = defaultTicksPerStep;
	private long taskID;
	private long nextExcecutionTicksPerStep = defaultTicksPerStep;
	protected TaskStatus status = TaskStatus.WAITING;
	private Map<String, ? extends Object> parameters;
	private TaskSpawner controllingTS;
	private long scheduledTick;
	
	/**
	 * Source of unique Ids for FrameworkTaskImpls
	 */
	private static long nextTaskID;
	
	/**
	 * Creates a FrameworkTaskImpl with default ticks per step
	 */
	public FrameworkTaskImpl() {
		this(defaultTicksPerStep,null);
	}
	public FrameworkTaskImpl(int ticksPerStep) {
		this(ticksPerStep,null);
	}
	public FrameworkTaskImpl(int ticksPerStep, TaskSpawner ts) {
		taskID = nextTaskID++;
		this.controllingTS=ts;
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
	 * Overwrite the runThisFrameworkTask method.
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public FrameworkTask call() {
		nextExcecutionTicksPerStep = ticksPerStep;
		
		try{
			runThisFrameworkTask();
		}catch(Exception e){
			logger.log(Level.WARNING, "Exception " + e.toString() + " encountered in task " + this.toString(), TaskManager.getCurrentTick());
			e.printStackTrace();
		}
		
		if (controllingTS != null){ 
			controllingTS.receiveFinishedTask(this);
		}else {
			logger.log(Level.WARNING, "This task {1} doesn't have an assigned TaskSpawner",new Object[] { TaskManager.getCurrentTick(), this });
		}
			
		return this;
	}


	/**
	 * To be overridden by extending classes. Overriding method should execute a
	 * handful of statements considered to constitute a single iteration of the
	 * task. For example, a codelet might look in a buffer for some
	 * representation and make a change to it in a single iteration.
	 */
	protected abstract void runThisFrameworkTask();

	@Override
	public synchronized void setTaskStatus(TaskStatus status) {
		if (this.status != TaskStatus.CANCELED){
			this.status = status;
		}else {
			logger.log(Level.WARNING, "Cannot set task status to CANCELED", TaskManager.getCurrentTick());
		}
	}

	@Override
	public TaskStatus getTaskStatus() {
		return status;
	}

	@Override
	public long getTaskId() {
		return taskID;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.FrameworkTask#getTicksPerStep()
	 */
	@Override
	public synchronized int getTicksPerStep() {
		return ticksPerStep;
	}

	@Override
	public synchronized void setTicksPerStep(int ticks) {
		if (ticks > 0){
			ticksPerStep = ticks;
			setNextTicksPerStep(ticks);
		}
	}

	@Override
	public void stopRunning() {
		setTaskStatus(TaskStatus.CANCELED);
	}
	
	@Override
	public void init(Map<String, ?> parameters) {
		this.parameters = parameters;
		init();
	}

	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the FrameworkTask
	 */
	@Override
	public void init() {
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.FrameworkTask#getParameter(java.lang.String)
	 */
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			logger.log(Level.WARNING, "Missing parameter, check factories data or first parameter: " + name, TaskManager.getCurrentTick());
			value = defaultValue;
		}
		return value;
	}
	

	/* 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkTask#getTaskSpawner()
	 */
	@Override
	public TaskSpawner getControllingTaskSpawner() {		
		return controllingTS;
	}

	/*
	 * @see edu.memphis.ccrg.lida.framework.FrameworkTask#setControllingTaskSpawner(edu.memphis.ccrg.lida.framework.TaskSpawner)
	 */
	@Override
	public void setControllingTaskSpawner(TaskSpawner controllingTS) {
		this.controllingTS=controllingTS;		
	}
	
	@Override
	public long getNextTicksPerStep() {		
		return nextExcecutionTicksPerStep;
	}
	
	@Override
	public void setNextTicksPerStep(long lapTick) {
		this.nextExcecutionTicksPerStep = lapTick;	
	}
	
	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		//Subclasses have the option of overriding this method.
	}	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof FrameworkTaskImpl){
			return taskID == ((FrameworkTaskImpl) o).getTaskId();
		}
		return false;
	}
	@Override
	public int hashCode(){
		return (int) taskID;
	}
	
	@Override
	public abstract String toString();
	
}
