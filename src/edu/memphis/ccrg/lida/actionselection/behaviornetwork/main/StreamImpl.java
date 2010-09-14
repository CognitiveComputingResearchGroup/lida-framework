/*
 * BehaviorStream.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:11 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;

/**
 * 
 */
public class StreamImpl implements Stream{
	
    private String name = "blank stream";
    
    private Set<Behavior> behaviors = new HashSet<Behavior>();
    
    private Map<Behavior, Set<Behavior>> successorLinks = new HashMap<Behavior, Set<Behavior>>();   
    
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
		Set<Behavior> values = successorLinks.get(predecessor);
		if(values == null){
			values = new HashSet<Behavior>();
			successorLinks.put(predecessor, values);
		}
		values.add(successor);
	}
	
	public void removeSuccessorLink(Behavior predecessor, Behavior successor){
		if(successorLinks.containsKey(predecessor))
			successorLinks.get(predecessor).remove(successor);
	}
	
	public Set<Behavior> getSuccessors(Behavior b){
		return successorLinks.get(b);
	}
	
}//class
