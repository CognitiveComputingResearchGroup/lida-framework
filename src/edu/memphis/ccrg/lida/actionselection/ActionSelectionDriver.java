/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * @author Siminder Kaur, Ryan McCall
 *
 */
public class ActionSelectionDriver extends ModuleDriverImpl {

	private List<ActionSelectionTrigger> actionSelectionTriggers = new ArrayList<ActionSelectionTrigger>();
	
	/**
	 * 
	 */
	public ActionSelectionDriver(){
    	super(DEFAULT_TICKS_PER_CYCLE, ModuleName.ActionSelectionDriver);
    }
	
    /**
     * @param ticksPerCycle - run frequency
     * @param tm -
     */
    public ActionSelectionDriver(int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm, ModuleName.ActionSelectionDriver);
	}
    
	@Override
	public void runThisDriver() {
		start();
		setTaskStatus(LidaTaskStatus.FINISHED); //Runs only once
	}//method
	
	@Override
	public String toString() {
		return ModuleName.ActionSelectionDriver + "";
	}
	
	/**
	 * To register Triggers
	 * @param t a new Trigger
	 */
	public void addActionSelectionTrigger(ActionSelectionTrigger t){
		actionSelectionTriggers.add(t);
	}
	
	/**
	 * 
	 */
	public void resetTriggers() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.reset();
		}
	}
	
	/**
	 * 
	 */
	public void start() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.start();
		}
	}	
	
	/**
	 * @param behaviors
	 */
	public void newBehaviorEvent(Queue<Behavior> behaviors) {		
		for (ActionSelectionTrigger trigger : actionSelectionTriggers)
			trigger.checkForTrigger(behaviors);
	}// method
	
	public void setAssociatedModule(LidaModule module) {
	}
	
}//class
