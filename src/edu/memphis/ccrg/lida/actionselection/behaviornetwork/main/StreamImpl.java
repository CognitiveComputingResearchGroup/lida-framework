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
public class StreamImpl implements Stream{
	
    private String name = "blank stream";
    
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    
    private Map<Behavior, List<Behavior>> successorLinks = new HashMap<Behavior, List<Behavior>>();   
    
    private boolean instantiated = false;
    
    private long id;
    
    public StreamImpl(String name, long id){
        this.name = name;              
    }
    public StreamImpl(long id){
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
    
    public boolean addBehavior(Behavior behavior){
    	return behaviors.add(behavior);
    }    
    public Collection<Behavior> getBehaviors(){
        return Collections.unmodifiableCollection(behaviors);
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
	public int size() {
		return behaviors.size();
	}
	
	public void addSuccessorLink(Behavior predecessor, Behavior successor){
		List<Behavior> values = successorLinks.get(predecessor);
		if(values == null){
			values = new ArrayList<Behavior>();
			successorLinks.put(predecessor, values);
		}
		values.add(successor);
	}
	
	public List<Behavior> getSuccessors(Behavior b){
		return successorLinks.get(b);
	}
	
}//class
