/*
 * MultiMap.java
 *
 * Sidney D'Mello
 * Created on March 22, 2004, 3:44 PM
 */

package edu.memphis.ccrg.lida.actionselection2.util;

import java.util.*;

public class MultiMap extends Hashtable
{    
    public MultiMap() 
    {
        super();
    }
       
    public MultiMap(int initialCapacity)
    {
        super(initialCapacity);
    }
   
    public MultiMap(int initialCapacity, float loadFactor)
    {
        super(initialCapacity, loadFactor);
    }
   
    public MultiMap(Map t)     
    {
        super(t);
    }
    
    
    public Object clone()
    {
        MultiMap clone = new MultiMap(size());
        for(Enumeration e = keys(); e.hasMoreElements();)
        {
            Object key = e.nextElement();
            Vector value = (Vector)super.get(key);
            
            for(Enumeration ee = value.elements(); ee.hasMoreElements();)
                clone.put(key, ee.nextElement());            
        }
        
        return clone;
    }
    
    public boolean contains(Object value) throws NullPointerException
    {
        boolean contains = false;
        
        if(value != null)
        {
            Iterator i = super.values().iterator();
            while(i.hasNext() && !contains)
            {
                contains = ((Vector)i.next()).contains(value);
            }
        }
        else
            throw new NullPointerException();
        
        return contains;
    }    
    
    public boolean containsValue(Object value) throws NullPointerException
    {
        return contains(value);
    }
    
    public Enumeration elements()
    {
        Vector elements = new Vector(size());
        
        Iterator i = super.values().iterator();
        while(i.hasNext())
        {
            elements.addAll((Vector)i.next());
        }
        return elements.elements();
    }
    
    public Object put(Object key, Object value) throws NullPointerException
    {
        Object object = null;
        if(key != null && value != null)
        {
            Vector vec = (Vector)get(key);
            if(vec == null)
            {
                vec = new Vector();
                vec.add(value);
                super.put(key, vec);
            }
            else
            {
                if(!vec.isEmpty())                
                    object = vec.lastElement();
                vec.add(value);
            }
        }
        else
            throw new NullPointerException();
        
        return object;
    }
    
    public void putAll(Map t) throws NullPointerException
    {
        if(t != null)
        {
            Iterator i = t.keySet().iterator();
            while(i.hasNext())
            {
                Object key = i.next();
                put(key, t.get(key));
            }
        }
        else
            throw new NullPointerException();
    }        
    
    public void remove(Object key, Object value) throws NullPointerException
    {              
        if(key != null && value != null)
        {
            Vector vec = (Vector)super.get(key);
            if(vec != null)
            {
                vec.remove(value);
                if(vec.isEmpty())
                    super.remove(key);
            }
        }     
        else
            throw new NullPointerException();
    }
    
    
    public Collection values()
    {
        Vector elements = new Vector(size());
        
        Iterator i = super.values().iterator();
        while(i.hasNext())
        {
            elements.addAll((Vector)i.next());
        }
        return elements;        
    }
    
    public Object getFirst(Object key)
    {
        Object obj = null;
        
        Vector vec = (Vector)super.get(key);
        if(vec != null)
            obj = vec.firstElement();
        
        return obj;
    }
    
    public Object getLast(Object key) throws NoSuchElementException
    {
        Object obj = null;
        
        Vector vec = (Vector)super.get(key);
        if(vec != null)
            obj = vec.lastElement();
        
        return obj;
    }    
    
    public Object get(Object key, int index) throws ArrayIndexOutOfBoundsException
    {
        Object obj = null;
        
        Vector vec = (Vector)super.get(key);
        if(vec != null)
            obj = vec.elementAt(index);
        
        return obj;
    }
    
    public Enumeration getAll(Object key)
    {
        Vector vec = (Vector)super.get(key);
        if(vec == null)
            vec = new Vector();
        
        return vec.elements();        
    }
}
