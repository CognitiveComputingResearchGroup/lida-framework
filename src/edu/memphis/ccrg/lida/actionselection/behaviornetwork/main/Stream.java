/*
 * BehaviorStream.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:11 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Stream is a list of behaviors that may be "instantiated"
 *  Sidney D'Mello, Ryan McCall
 */
public class Stream{
	
    private String name = "blank stream";
    
    private ConcurrentMap<Long, Behavior> behaviors = new ConcurrentHashMap<Long, Behavior>();
    
    private boolean instantiated = false;
    
    private long id;
    
    public Stream(String name, long id){
        this.name = name;              
    }
    public Stream(long id){
    	this("no name", id);
    }
    
    public String getName(){
        return name;
    }
    public String toString(){
        return name;
    }
    
    public void instantiate(){
        instantiated = true;
    }
    public void uninstantiate(){
        instantiated = false;
    }
    public boolean isInstantiated(){
        return instantiated;
    } 
    
    public Behavior addBehavior(Behavior behavior){
    	return behaviors.put(behavior.getId(), behavior);
    }    
    public Collection<Behavior> getBehaviors(){
        return Collections.unmodifiableCollection(behaviors.values());
    }
    public Behavior getBehavior(long id){
        return behaviors.get(id);       
    }
	public int getBehaviorCount(){
		return behaviors.size();
	}
	
	public void removeBehavior(Behavior behavior) {
		behaviors.remove(behavior);
	}
	
	public long getId(){
		return id;
	}
	
}//class
