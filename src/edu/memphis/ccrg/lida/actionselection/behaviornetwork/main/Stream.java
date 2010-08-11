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
	
    private String name;
    
    private List<BehaviorImpl> behaviors = new ArrayList<BehaviorImpl>();
    
    private boolean instantiated = false;
    
    public Stream(String name){
        if(name != null)
        	name = Math.random()+"";              
    }
    public Stream(){
    	this(null);
    }
    
    public void instantiate(){
        instantiated = true;
    }
    
    public void uninstantiate(){
        instantiated = false;
    }
    
    public void addBehavior(BehaviorImpl behavior){
    	behaviors.add(behavior);
    }    
    
    public String getName(){
        return name;
    }
    
    public List<BehaviorImpl> getBehaviors(){
        return behaviors;
    }
    
    public boolean isInstantiated()
    {
        return instantiated;
    }
    
    public void setBehaviors(List<BehaviorImpl> behaviors){
        this.behaviors = behaviors;       
    }
    
    public BehaviorImpl getBehavior(String name){
        for(BehaviorImpl b: behaviors)
            if(b.toString().compareTo(name) == 0)
                 return b;
        return null;          
    }

    public String toString(){
        return name;
    }
}
