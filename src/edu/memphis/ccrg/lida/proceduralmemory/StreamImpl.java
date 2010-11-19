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

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * TODO add to procedural memory!
 */
public class StreamImpl implements Stream{
	
//	private static Logger logger = Logger.getLogger("lida.actionselection.behaviornetwork.main");
	
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
    
    public boolean addBehavior(Behavior behavior){
    	return behaviors.add(behavior);
    }
	public void removeBehavior(Behavior behavior) {
		behaviors.remove(behavior);
	}
    public Collection<Behavior> getBehaviors(){
        return Collections.unmodifiableCollection(behaviors);
    }
	public int getBehaviorCount(){
		return behaviors.size();
	}
	
	public long getId(){
		return id;
	}
    public String getName(){
        return name;
    }
	
}//class
