/*
 * Codelet.java
 *
 * Sidney D'Mello
 * Created on December 18, 2003, 6:57 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;
import java.util.*;

public abstract class SidneyCodelet 
{        
    public final static int UNKNOWN     = 0;
    public final static int BEHAVIOR    = 1;
    public final static int EXPECTATION = 2;
    
    protected String name;
    private int type;
    protected Behavior behavior;
    
    protected Hashtable properties;    
    
    public SidneyCodelet()
    {
        behavior = null;
    }
    
    public SidneyCodelet(String name) throws NullPointerException
    {
        if(name != null)
        {
            this.name = name;
            setType(UNKNOWN);
            properties = new Hashtable();
        }
        else
            throw new NullPointerException();
    }
    
    public SidneyCodelet(String name, int type) throws NullPointerException
    {
        if(name != null)
        {
            this.name = name;
            setType(type);
            properties = new Hashtable();
        }
        else
            throw new NullPointerException();        
    }
    
    public abstract void execute(Environment e);
    
    public Behavior getBehavior()
    {
        return behavior;
    }
    
    public final String getName()
    {
        return name;
    }
    
    public final int getType()
    {
        return type;
    }
    
    public final Hashtable getProperties()
    {
        return properties;
    }
    
    public void setName(String name) throws NullPointerException
    {
        if(name != null)
        {
            this.name = name;
            setType(UNKNOWN);
            properties = new Hashtable();
        }
        else
            throw new NullPointerException();        
    }
    
    public void setType(int type) throws IllegalArgumentException
    {
        if(type == BEHAVIOR)
            this.type = BEHAVIOR;
        else if (type == EXPECTATION)
                this.type = EXPECTATION;
        else if(type == UNKNOWN)
            this.type = UNKNOWN;
        else
            throw new IllegalArgumentException("Unrecognized codelet type: " + type);
    }
    
    public final void setProperties(Hashtable properties)                      
    {
        if(properties != null)
        {
            this.properties = properties;
        }
    }
    
    public final void setBehavior(Behavior behavior) throws NullPointerException
    {
        if(behavior != null)
            this.behavior = behavior;
        else
            throw new NullPointerException();
    }
    
    public void addProperty(Object name, Object value)
    {
        properties.put(name, value);
    }
    
    public Object getProperty(Object name)
    {
        return properties.get(name);
    }
    
    public String toString()
    {
        return name;
    }

}
