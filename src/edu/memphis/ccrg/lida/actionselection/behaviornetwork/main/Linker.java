/*
 * Linker.java
 *
 * Created on December 18, 2003, 4:18 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Linkable;

public class Linker{    
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Linker");
	
    //private final static String PROTECTED_GOAL = "edu.memphis.bnt.engine.ProtectedGoal";
    
    private BehaviorNetworkImpl net;
        
    public Linker(BehaviorNetworkImpl net) throws NullPointerException
    {
        if(net != null)
            this.net = net;
        else
            throw new NullPointerException();
    }
    
    public void buildLinks()
    {
        logger.info("LINKER : BUILDING LINKS");
        
        buildEnvironmentalLinks();        
        buildGoalLinks();        
        buildBehaviorLinks();
    }
    
    public void buildEnvironmentalLinks() throws NullPointerException
    {        
        logger.info("ENVIRONMENTAL LINKS");
        
        Iterator iterator = net.getStreams().iterator();                        //iterator for all streams
        while(iterator.hasNext())
        {
            Iterator li = ((Stream)(iterator.next())).getBehaviors().iterator();//iterator for a stream
            while(li.hasNext())
            {
                Behavior behavior = (Behavior)li.next();
                Iterator lj = behavior.getPreconditions().keySet().iterator();  //iterator for a behavior
                
                while(lj.hasNext())
                {   
                    Object proposition = lj.next();                    
                    
                    LinkedList behaviors = (LinkedList)net.getPropositions().get(proposition);
                    if(behaviors == null)
                    {
                        behaviors = new LinkedList();
                        behaviors.add(behavior);
                        net.getPropositions().put((Linkable) proposition, behaviors);
                    }
                    else                        
                        behaviors.add(behavior);                                                                        
                }
            }
        }
 //       report(null, net.getPropositions());
        
        logger.info("\n");
    }
    
    public void buildGoalLinks()
    {
        logger.info("GOAL LINKS");
        
        logger.info("EXCITATORY GOAL LINKS");
        buildExcitatoryGoalLinks();        
        
        Iterator i = net.getGoals().iterator();
        while(i.hasNext())
        {
            Goal goal = (Goal)i.next();
            report("GOAL " + goal.getName(), goal.getExcitatoryPropositions());
        }
        logger.info("");
        
        logger.info("INHIBITATORY GOAL LINKS");
        buildInhibitoryGoalLinks();        
        
        i = net.getGoals().iterator();
        while(i.hasNext())
        {
            Goal goal = (Goal)i.next();
            if(goal instanceof ProtectedGoal)
                report("GOAL " + goal.getName(), ((ProtectedGoal)goal).getInhibitoryPropositions());
        }
        logger.info("\n");
    }
    
    public void buildExcitatoryGoalLinks()
    {                                
        Iterator iterator = net.getStreams().iterator();                        //iterate over streams
        while(iterator.hasNext())
        {            
            Iterator li = ((Stream)(iterator.next())).getBehaviors().iterator();//iterator gor a stream
            while(li.hasNext())
            {
                Behavior behavior = (Behavior)li.next();                
                Iterator lj = behavior.getAddList().iterator();                 //iterator the add list for a behavior
                
                while(lj.hasNext())
                {   
                    Object proposition = lj.next();
                    
                    Iterator lk = net.getGoals().iterator();                    //iterator forr all goals
                    while(lk.hasNext())
                    {
                        Goal goal = (Goal)lk.next();                        
                        Hashtable propositions = goal.getExcitatoryPropositions();
                        
                        if(propositions.containsKey(proposition))
                        {
                            LinkedList behaviors = (LinkedList)propositions.get(proposition);
                            if(behaviors == null)
                            {                                
                                behaviors = new LinkedList();
                                behaviors.add(behavior);
                                propositions.put(proposition, behaviors);
                            }
                            else
                            {                             
                                behaviors.add(behavior);                                                                    
                            }
                        }                        
                    }
                }
            }
        }           
    }
    
    
    public void buildInhibitoryGoalLinks()
    {                                        
        Iterator iterator = net.getStreams().iterator();                        //iterate over streams
        while(iterator.hasNext())
        {            
            Iterator li = ((Stream)(iterator.next())).getBehaviors().iterator();//iterate over a stream
            while(li.hasNext())
            {
                Behavior behavior = (Behavior)li.next();                
                Iterator lj = behavior.getDeleteList().iterator();              //iterate over the delete list
                while(lj.hasNext())
                {   
                    Object proposition = lj.next();
                    
                    Iterator lk = net.getGoals().iterator();                    //iterate over the goals
                    while(lk.hasNext())
                    {
                        Object goal = lk.next();
                        if(goal instanceof ProtectedGoal)
                        {                            
                            Hashtable propositions = ((ProtectedGoal)goal).getInhibitoryPropositions();
                                                        
                            if(((ProtectedGoal)goal).getExcitatoryPropositions().containsKey(proposition))
                            {
                                LinkedList behaviors = (LinkedList)propositions.get(proposition);
                                if(behaviors == null)
                                {
                                    behaviors = new LinkedList();
                                    behaviors.add(behavior);
                                    propositions.put(proposition, behaviors);
                                }
                                else
                                {
                                    behaviors.add(behavior);                                    
                                }
                            }                            
                        }                        
                    }
                }
            }
        }   
    }
    
    public void buildBehaviorLinks()
    {
        logger.info("BEHAVIOR LINKS");
        
        Iterator iterator = net.getStreams().iterator();                        //iterate over all streams
        while(iterator.hasNext())
        {
            Stream currentStream = (Stream)iterator.next();
            
            Iterator li = currentStream.getBehaviors().iterator();              //iterate over a single stream
            while(li.hasNext())
            {
                Behavior firstBehavior = (Behavior)li.next();                                  
                Iterator lj = currentStream.getBehaviors().iterator();          //iterate over current stream
                while(lj.hasNext())
                {
                    Behavior secondBehavior = (Behavior)lj.next();
                    if(!firstBehavior.equals(secondBehavior))
                    {
                        buildSuccessorLinks(firstBehavior, secondBehavior);
                        buildPredecessorLinks(firstBehavior, secondBehavior);
                        buildConflictorLinks(firstBehavior, secondBehavior);
                    }
                }
                logger.info("BEHAVIOR : " + firstBehavior);
                logger.info("SUCCESSOR LINKS");
                report(null, firstBehavior.getSuccessors());
                logger.info("");
                
                logger.info("PREDECESSOR LINKS");
                report(null, firstBehavior.getPredecessors());
                logger.info("");
                
                logger.info("CONFLICTOR LINKS");
                report(null, firstBehavior.getConflictors());
                logger.info("\n");
            }
        }   
    }
    
    private void buildSuccessorLinks(Behavior firstBehavior, Behavior secondBehavior)
    {                
        
        Iterator iterator = firstBehavior.getAddList().iterator();              //iterate over add propositions of first behavior
        while(iterator.hasNext())
        {   
            Object addProposition = iterator.next();
            Iterator li = secondBehavior.getPreconditions().keySet().iterator();//iterate over preconditions of second behavior
            while(li.hasNext())
            {   
                if(addProposition.equals(li.next()))
                {
                    LinkedList behaviors = (LinkedList)firstBehavior.getSuccessors().get(addProposition);
                    if(behaviors == null)
                    {
                        behaviors = new LinkedList();
                        behaviors.add(secondBehavior);
                        firstBehavior.getSuccessors().put(addProposition, behaviors);
                    }
                    else
                    {
                        if(!behaviors.contains(secondBehavior))
                        {
                            behaviors.add(secondBehavior);
                        }
                    }
                }
            }
        }       
    }
    
    private void buildPredecessorLinks(Behavior firstBehavior, Behavior secondBehavior)
    {                        
        Iterator iterator = firstBehavior.getPreconditions().keySet().iterator();        //iterate over preconditon of first behavior
        while(iterator.hasNext())
        {   
            Object precondition = iterator.next();
            Iterator li = secondBehavior.getAddList().iterator();               //iterate over addlist of second behavior
            while(li.hasNext())
            {   
                if(precondition.equals(li.next()))
                {
                    LinkedList behaviors = (LinkedList)firstBehavior.getPredecessors().get(precondition);
                    if(behaviors == null)
                    {
                        behaviors = new LinkedList();
                        behaviors.add(secondBehavior);
                        firstBehavior.getPredecessors().put(precondition, behaviors);
                    }
                    else
                    {
                        if(!behaviors.contains(secondBehavior))
                        {
                            behaviors.add(secondBehavior);
                        }
                    }
                }
            }
        }               
    }
    
    private void buildConflictorLinks(Behavior firstBehavior, Behavior secondBehavior)
    {                        
        Iterator iterator = firstBehavior.getPreconditions().keySet().iterator();        //iterate over preconditon of first behavior
        while(iterator.hasNext())
        {   
            Object precondition = iterator.next();
            Iterator li = secondBehavior.getDeleteList().iterator();               //iterate over delete list of second behavior
            while(li.hasNext())
            {   
                if(precondition.equals(li.next()))
                {
                    LinkedList behaviors = (LinkedList)firstBehavior.getConflictors().get(precondition);
                    if(behaviors == null)
                    {
                        behaviors = new LinkedList();
                        behaviors.add(secondBehavior);
                        firstBehavior.getConflictors().put(precondition, behaviors);
                    }
                    else
                    {
                        if(!behaviors.contains(secondBehavior))
                        {
                            behaviors.add(secondBehavior);
                        }
                    }
                }
            }
        }                    
    }
    
    private void report(String header, Map<Object,List<Behavior>> links)
    {
        if(header != null)
            logger.info(header);
        
        if(links != null)
        {
            Iterator i = links.keySet().iterator();
            while(i.hasNext())
            {
                Object proposition = i.next();
                LinkedList behaviors = (LinkedList)links.get(proposition);
                
                logger.info("\t" + proposition + " --> " + behaviors);
            }
        }        
    }
    
    /*
    private void setInhibitoryPropositions()
    {
        Iterator iterator = net.getGoals().iterator();                                //iterate over the goals
        while(iterator.hasNext())
        {
            Object goal = iterator.next();
            if(goal instanceof edu.memphis.bnt.engine.ProtectedGoal)
            {
                Hashtable inhibitoryPropositions = new Hashtable();
                Hashtable exitatoryPropositions = ((ProtectedGoal)goal).getExcitatoryPropositions();
                Iterator li = exitatoryPropositions.keySet().iterator();
                while(li.hasNext())
                {
                    inhibitoryPropositions.put(li.next(), new LinkedList());
                }
                ProtectedGoal currentGoal = (ProtectedGoal)goal;
                currentGoal.setInhibitoryPropositions(inhibitoryPropositions);
            }
        }           
    }*/    
}