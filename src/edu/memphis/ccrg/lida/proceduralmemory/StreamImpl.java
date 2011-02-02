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
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * TODO add to procedural memory!
 */
public class StreamImpl implements Stream{
	
	private static final Logger logger = Logger.getLogger(StreamImpl.class.getCanonicalName());
	
    private String name = "blank stream";
    
    private long id;
    
    private Set<Behavior> behaviors;  

    /**
     * @param name .
     * @param id .
     */
    public StreamImpl(String name, long id){
        this.name = name;    
        this.id = id;
        behaviors = new HashSet<Behavior>();
    }
    
    /**
     * @param id .
     */
    public StreamImpl(long id){
    	this("no name", id);
    }
    
    @Override
	public boolean addBehavior(Behavior behavior){
    	return behaviors.add(behavior);
    }
	@Override
	public void removeBehavior(Behavior behavior) {
		logger.log(Level.FINER, "Removing " + behavior + " from " + this, 
					LidaTaskManager.getCurrentTick());
		behaviors.remove(behavior);
	}
    @Override
	public Collection<Behavior> getBehaviors(){
        return Collections.unmodifiableCollection(behaviors);
    }
	@Override
	public int getBehaviorCount(){
		return behaviors.size();
	}
	
	@Override
	public long getId(){
		return id;
	}
    @Override
	public String getLabel(){
        return name;
    }
	
}
