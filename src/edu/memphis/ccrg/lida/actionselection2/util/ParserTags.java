/*
 * ParserConstants.java
 *
 * Sidney D'Mello
 * Created on December 16, 2003, 2:41 PM
 */

package edu.memphis.ccrg.lida.actionselection2.util;


public class ParserTags 
{   
    //reuseable tags
    public final static String NAME             = "name";
    public final static String PROPOSITION      = "proposition";    
    
    //net elements
    public final static String NET              = "net";
    public final static String CONSTANTS        = "constants";
    public final static String GOALS            = "goals";
    public final static String STREAMS          = "streams";
    public final static String REINFORCER       = "reinforcer";
    
    //net constants
    public final static String THETA            = "theta";
    public final static String PHI              = "phi";
    public final static String GAMMA            = "gamma";
    public final static String DELTA            = "delta";
    public final static String PI               = "pi";
    public final static String OMEGA            = "omega";    
    
    //goal tahs
    public final static String GOAL             = "goal";
    public final static String PROTECTED        = "protected";
    public final static String PERSISTENT       = "persistent";
    public final static String PROPOSITIONS     = "propositions";
    
    
    //stream tags
    public final static String STREAM           = "stream";
    public final static String BEHAVIORS        = "behaviors";
    
    //behavior tags
    public final static String BEHAVIOR         = "behavior";
    public final static String PRECONDITIONS    = "preconditions";
    public final static String ADDLIST          = "addlist";
    public final static String DELETELIST       = "deletelist";
    public final static String CODELETS         = "codelets"; 
    public final static String BASEACTIVATION   = "baseactivation";
    
    //codelet tags: <Behavior defiend above>
    public final static String CODELET          = "codelet";
    public final static String TYPE             = "type";
    public final static String EXPECTATION      = "expectation";
    public final static String CLASS            = "class";
    public final static String PROPERTIES       = "properties";
    
    //property tags: <name defined above>
    public final static String PROPERTY         = "property";
    public final static String VALUE            = "value";
    public final static String INITIALIZTION    = "initialization";    
        
    //for the sigmoid reinforcer
    public final static String SIGMOID          = "sigmoid";
    public final static String A                = "a";
    public final static String C                = "c";
}
