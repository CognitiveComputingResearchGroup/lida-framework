/*
 * RunSummary.java
 *
 * Sidney D'Mello
 * Created on January 10, 2005, 8:26 AM
 */

package edu.memphis.ccrg.lida.actionselection2.run;

import java.util.*;

import edu.memphis.ccrg.lida.actionselection2.util.CycleInformation;

public class RunSummary implements Comparable, Cloneable
{    
    private int id;
    
    private boolean success;
    private LinkedList firings;
    
    private int cycles;
    private long start_time;
    private long stop_time;        
    
    public RunSummary(int id) 
    {                
        this.id = id;
        reset();
    }        
    
    public RunSummary(int id, boolean start)
    {
        this.id = id;
        
        reset();
        if(start)                    
            start();        
    }
    
    public int compareTo(Object obj) throws ClassCastException
    {
        if(obj != null)
        {
            RunSummary rs = (RunSummary)obj;
            
            if(success && !rs.success)            
                return 1;
            else if(!success && rs.success)
                return -1;
            else
            {
                if(cycles > rs.cycles)
                    return 1;
                else if (cycles < rs.cycles)
                    return -1;
                else
                {
                    if(firings.size() > rs.firings.size())                        
                        return 1;
                    else if(firings.size() > rs.firings.size())
                        return -1;
                    else
                        return 0;
                }
            }
        }
        throw new NullPointerException();
        
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        RunSummary clone = (RunSummary)super.clone();
        
        clone.firings = (LinkedList)firings.clone();
        for(Iterator i = firings.iterator(); i.hasNext();){
        	CycleInformation ci = (CycleInformation) i.next();
            clone.firings.add(ci.clone());
        }
        
        return clone;
    }
    
    public boolean add(CycleInformation cycleInfo)
    {
        if(accepting() && !registered(cycleInfo) )
        {
            firings.add(cycleInfo);
            return true;
        }
        else
            return false;
    }
    
    public boolean registered(CycleInformation cycleInfo)
    {
        return firings.contains(cycleInfo);
    }
    
    public boolean accepting()
    {
        return started() && !stopped();
    }
    
    public boolean successful()
    {
        return success;
    }
    
    public int getID()
    {
        return id;
    }
    
    public int getCycles()
    {
        return cycles;
    }
    
    public long getStartTime()
    {
        return start_time;
    }
    
    public long getStopTime()
    {
        return stop_time;        
    }
    
    public long getRunTime()
    {
        return stop_time - start_time;
    }
    
    public void setCycles()
    {
        this.cycles = cycles;
    }
    
    public void start()
    {
        this.start_time = System.currentTimeMillis();
    }
    
    public void stop()
    {
        this.stop_time = System.currentTimeMillis();
    }
    
    public Collection getFirings()
    {
        return firings;
    }
    
    public boolean started()
    {
        return start_time != 0;
    }
    
    public boolean stopped()
    {
        return stop_time != 0;
    }
    
    public void setCycles(int cycles)
    {
        this.cycles = cycles;
    }
    
    public void markSuccess()
    {
        success = true;
    }
    
    public void markFailure()
    {
        success = false;
    }
    
    public void reset()
    {
        success = false;
        firings = new LinkedList();
        
        cycles = 0;
        start_time = 0;
        stop_time = 0;                
    }
    
    /*
    public Collection getPlan()
    {
        ArrayList plan = null;
        if(firings != null)
        {
            plan = new ArrayList(firings.size());
            for(Iterator i = firings.iterator(); i.hasNext();)
            {
                CycleInformation info = (CycleInformation)i.next();
                plan.add(info.getWinner());
            }
        }
    }*/
    
    public String toString()
    {
        return id + "\t" + cycles + "\t" + success;
    }
}
