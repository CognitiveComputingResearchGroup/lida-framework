/*
 * StateReader.java
 *
 * Created on June 9, 2005, 5:15 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import java.util.*;

public class EnvironmentReader 
{       
    public final static String COMMENT  = "#";
    public final static String STATE    = "S";
    public final static String GOALS    = "G";    
    public final static String TOKEN    = "\t";    
    public final static int    TOKENS   = 2;
    
    private MultiMap state;
    private Hashtable goals;

    public EnvironmentReader(String fileName) 
    {
        read(fileName);
    }
    
    private void read(String fileName)
    {
        state = new MultiMap();
        goals = new Hashtable();
        
        try
        {
            Vector buffer = new Vector(new FileRepresentation().represent(fileName));
            for(Enumeration e = buffer.elements(); e.hasMoreElements();)
            {
                String line = e.nextElement().toString().trim();
                String splits[] = line.split(TOKEN);
                if(splits.length == 2)
                {
                    if(splits[0].trim().equals(STATE))
                        state.put(splits[1].trim(), new Boolean(true));
                    else if(splits[0].trim().equals(GOALS))
                        goals.put(splits[1].trim(), new Boolean(true));
                }                    
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public MultiMap getState()
    {
        return state;
    }
    
    public Hashtable getGoals()
    {
        return goals;
    }
    
}
