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
public class Stream 
{    
    private String name;
    
    private LinkedList behaviors;
    private boolean instantiated;
    
    public Stream(String name) throws NullPointerException
    {
        if(name != null)
        {
            this.name = name;
            
            behaviors = new LinkedList();
            instantiated = false;         
        }
        else
            throw new NullPointerException();                
    }
    
    public void instantiate()
    {
        instantiated = true;
    }
    
    public void uninstantiated()
    {
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
    
    public String getName()
    {
        return name;
    }
    
    public LinkedList getBehaviors()
    {
        return behaviors;
    }
    
    public boolean isInstantiated()
    {
        return instantiated;
    }
    
    public void setBehaviors(LinkedList behaviors)throws NullPointerException
    {
        if(behaviors != null)
            this.behaviors = behaviors;
        else
            throw new NullPointerException();        
    }
    
    public Behavior getBehavior(String name) throws NullPointerException
    {
        Behavior behavior = null;
        if(name != null)
        {
            ListIterator li = behaviors.listIterator();
            while(li.hasNext() && behavior == null)
            {
                if(((Behavior)li.next()).getName().compareTo(name) == 0)
                    behavior = (Behavior)li.previous();
            }
        }
        else
            throw new NullPointerException();
        
        return behavior;            
    }
    
    public void reset()
    {
        for(Iterator i = behaviors.iterator(); i.hasNext();)
            ((Behavior)i.next()).reset();
    }
    
    public String toString()
    {
        return name;
    }
}
