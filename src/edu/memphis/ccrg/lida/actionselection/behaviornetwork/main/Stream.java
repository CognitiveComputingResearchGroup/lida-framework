/*
 * BehaviorStream.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:11 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;

/**
 * Stream is a list of behaviors that may be "instantiated"
 *  Sidney D'Mello, Ryan McCall
 */
public class Stream{
	
    private String name = "blank stream";
    
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    
    private boolean instantiated = false;
    
    public Stream(String name){
        this.name = name;              
    }
    public Stream(){
    	this("no name");
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
    
    public void addBehavior(Behavior behavior){
    	behaviors.add(behavior);
    }    
    public List<Behavior> getBehaviors(){
        return behaviors;
    }
    public Behavior getBehavior(String name){
        for(Behavior b: behaviors)
            if(b.toString().compareTo(name) == 0)
                 return b;
        return null;          
    }
	public int getBehaviorCount(){
		return behaviors.size();
	}
	
}//class
