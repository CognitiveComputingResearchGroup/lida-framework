/*
 * CodeletLoader.java
 *
 * Created on January 13, 2004, 2:49 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import java.util.Hashtable;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2.SidneyCodelet;

public class CodeletClassLoader
{       
    public CodeletClassLoader() 
    {        
    }
    
    public SidneyCodelet loadClass(String className) throws Exception
    {
        return (SidneyCodelet)Class.forName(className).newInstance();
    }        
}
