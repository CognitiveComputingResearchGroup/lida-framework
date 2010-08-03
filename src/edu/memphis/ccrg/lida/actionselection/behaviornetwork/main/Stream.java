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
 *
 */
public class Stream{
	
    private String name;
    
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    
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
    
    public void addBehavior(Behavior behavior)throws NullPointerException
    {
        if(behavior != null)
        {
            if(!behaviors.contains(behavior))
                behaviors.add(behavior);
        }
        else
            throw new NullPointerException();        
    }    
    
    public String getName(){
        return name;
    }
    
    public List<Behavior> getBehaviors(){
        return behaviors;
    }
    
    public boolean isInstantiated()
    {
        return instantiated;
    }
    
    public void setBehaviors(LinkedList<Behavior> behaviors){
        if(behaviors != null)
            this.behaviors = behaviors;       
    }
    
    public Behavior getBehavior(String name){
        for(Behavior b: behaviors)
            if(b.getName().compareTo(name) == 0)
                 return b;
        return null;          
    }
    
    public void reset(){
    	for(Behavior b: behaviors)
    		b.reset();
    }
    
    public String toString(){
        return name;
    }
}
