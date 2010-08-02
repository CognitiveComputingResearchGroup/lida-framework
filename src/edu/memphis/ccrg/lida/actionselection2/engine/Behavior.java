/*
 * Behavior.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 5:35 PM
 */

package edu.memphis.ccrg.lida.actionselection2.engine;

import java.util.*;
import java.util.logging.Logger;

public class Behavior
{   
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Behavior");
	
    private String name;

    private double alpha;    
    private double beta; 
    private double incoming;
 
    private Hashtable preconditions;
    private LinkedList addList;
    private LinkedList deleteList;        
    
    private Hashtable predecessors;
    private Hashtable successors;
    private Hashtable conflictors;
    
    private LinkedList behaviorCodelets;
    private LinkedList expectationCodelets;
    
    private Stream stream;
    
    private LinkedList history;
    
    public Behavior(String name) throws NullPointerException
    {
        if(name != null)
        {
            this.name = name;                        
            
            preconditions = new Hashtable();
            addList = new LinkedList();
            deleteList = new LinkedList();
            
            predecessors = new Hashtable();
            successors = new Hashtable();
            conflictors = new Hashtable();
            
            behaviorCodelets = new LinkedList();
            expectationCodelets = new LinkedList();
            
            stream = null;
            
            reset();
        }
        else
            throw new NullPointerException();
    }
        
    public void excite(double excitation)
    {        
        incoming += Math.abs(excitation);
    }
    
    public void inhibit(double inhibition)
    {       
        incoming -= Math.abs(inhibition);                 
    }
    
    public void reinforce(double reinforcement)
    {
        if(reinforcement >= 0 && reinforcement <= 1)
            beta = reinforcement;
        else
            throw new IllegalArgumentException("Reinforcement must be 0 <= " +
                                               "reinforcement <= 1 " + 
                                               reinforcement); 
    }
    
    public void merge()
    {        
        alpha = alpha + (BehaviorNetwork.getOmega() * beta) + incoming;
        if(alpha < 0)
            alpha = 0;
        
        incoming = 0;        
    }
    
    public void decay(double alpha)
    {
        this.alpha = alpha;
        if(alpha < 0)
            alpha = 0;
    }
    
    public void resetActivation()
    {
        alpha = 0;
    }
     
    public void spreadExcitation()
    {           
        //logger.info("BEHAVIOR : EXCITATION " + name);
        
        if(isActive())
            spreadSuccessorActivation();        
        else
            spreadPredecessorActivation();                
    }
    
    public void spreadInhibition()
    {
        //logger.info("BEHAVIOR : INHIBITION " + name);
        spreadConflictorActivation();        
    }
  
    public void spreadSuccessorActivation()
    {           
        Iterator iterator = successors.keySet().iterator();
        while(iterator.hasNext())
        {
            Object addProposition = iterator.next();
            LinkedList behaviors = (LinkedList)successors.get(addProposition);

            Iterator li = behaviors.iterator();                
            while(li.hasNext())
            {                               
                Behavior successor = (Behavior)li.next();
                if(!((Boolean)(successor.getPreconditions().get(addProposition))).booleanValue())
                {
                    double granted = ((alpha * Environment.getEnergy()) / Goal.getExcitatoryStrength()) / (behaviors.size() * successor.getPreconditions().size());
                    successor.excite(granted);

                    logger.info("\t:+" + name + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }
    
    public void spreadPredecessorActivation()
    {             
        Iterator iterator = preconditions.keySet().iterator();
        while(iterator.hasNext())
        {
            Object precondition = iterator.next();
            if(!((Boolean)(preconditions.get(precondition))).booleanValue())
            {                      
                LinkedList behaviors = (LinkedList)predecessors.get(precondition);                                
                if(behaviors != null)
                {
                    Iterator li = behaviors.iterator();                    
                    while(li.hasNext())
                    {
                        Behavior predecessor = (Behavior)li.next();                                              
                        double granted = (alpha / predecessor.getAddList().size())/behaviors.size();                        
                        predecessor.excite(granted);

                        logger.info("\t:+" + alpha + " " + name + "<--" + granted + " to " +
                                        predecessor + " for " + precondition);
                    }
                }
            }
        }        
    }
    
    
    public void spreadConflictorActivation()
    {
        double fraction = ProtectedGoal.getInhibitoryStrength() / Goal.getExcitatoryStrength();
        
        Iterator iterator = preconditions.keySet().iterator();
        while(iterator.hasNext())
        {
            Object precondition = iterator.next();
            
            //if(((Boolean)(preconditions.get(precondition))).booleanValue())
            if(BehaviorNetwork.getEnvironment().isPropositionTrue(precondition))
            {                             
                LinkedList behaviors = (LinkedList)conflictors.get(precondition); 
                if(behaviors != null)
                {
                    Iterator li = behaviors.iterator();                    
                                
                    while(li.hasNext())
                    {
                        boolean mutualConflict = false;
                        Behavior conflictor = (Behavior)li.next();
                        double inhibited = (alpha * fraction) / (behaviors.size() * conflictor.getDeleteList().size());
                    
                        Iterator lj = conflictor.getPreconditions().keySet().iterator();
                        while(lj.hasNext() && !mutualConflict)
                        {
                            Object conflictorPreCondition = lj.next();
                            if(((Boolean)conflictor.getPreconditions().get(conflictorPreCondition)).booleanValue())
                            {
                                Iterator lk = deleteList.iterator();
                                while(lk.hasNext() && !mutualConflict)
                                {
                                    if(conflictorPreCondition.equals(lk.next()))
                                    {
                                        mutualConflict = true;
                                    }
                                }
                            }
                        }    
                        if(mutualConflict)
                        {
                            //System.out.println("Mutual conflict detected");
                            if(conflictor.getAlpha() < this.getAlpha())
                            {
                                conflictor.inhibit(inhibited);
                                logger.info("\t:-" + name + "---" + 
                                                inhibited + " to " + conflictor 
                                                + " for " + precondition);                                
                            }
                        }
                        else
                        {
                            //System.out.println("No Mutual conflict detected");
                            conflictor.inhibit(inhibited);
                            logger.info("\t:-" + name + "---" + inhibited + 
                                            " to " + conflictor + " for " + precondition);                                
                        }
                    }
                }
            }                
        }        
    }    
    
    public boolean isActive()
    {
        boolean active = true;
        
        Iterator iterator = preconditions.keySet().iterator();
        while(iterator.hasNext() && active)        
        {
            if(!((Boolean)preconditions.get(iterator.next())).booleanValue())
                active = false;
        }
        return active;
    }    
        
    public void deactivate()
    {                
        Iterator iterator = preconditions.keySet().iterator();
        while(iterator.hasNext())
        {
            preconditions.put(iterator.next(), new Boolean(false));
        }
    }
    
    public void prepareToFire()
    {
        logger.info("BEHAVIOR : PREPARE TO FIRE " + name);
        
        Iterator bi = behaviorCodelets.iterator();
        while(bi.hasNext())
        {
            SidneyCodelet codelet = (SidneyCodelet)bi.next();
            Iterator ci = ((Map) codelet.getProperties()).keySet().iterator();
            while(ci.hasNext())
            {
                Object name = ci.next();
                Object value = Environment.getCurrentState().get(name);
                if(value != null)
                    codelet.addProperty(name, value );
            }
        }        
    }
    
    public Collection getHistory()
    {
        return history;
    }
    
    public void record()
    {
        history.addLast(new ActivationRecord(this));
    }
    
   
// start add methods    
    public void addPrecondition(Object precondition) throws NullPointerException
    {
        if(precondition != null)
        {
            if(!preconditions.contains(precondition))
                preconditions.put(precondition, new Boolean(false));            
        }
        else
            throw new NullPointerException();
    }
    
    public void addAddCondition(Object addCondition) throws NullPointerException
    {    
        if(addCondition != null)
        {
            if(!addList.contains(addCondition))
                addList.add(addCondition);
        }
        else
            throw new NullPointerException();
    }
    
    public void addDeleteCondition(Object deleteCondition) throws NullPointerException
    {    
        if(deleteCondition != null)
        {
            if(!deleteList.contains(deleteCondition))
                deleteList.add(deleteCondition);
        }
        else
            throw new NullPointerException();
    }    
    
    public void addPredecessor(Object precondition, Behavior predecessor) throws NullPointerException
    {
        if(precondition != null && predecessor != null)
        {
            LinkedList list = (LinkedList)predecessors.get(precondition);            
            if(list == null)
            {
                list = new LinkedList();
                list.add(predecessor);
                predecessors.put(precondition, list);
            }
            else
            {
                if(!list.contains(predecessor))                
                    list.add(predecessor);                                    
            }
        }
        else
            throw new NullPointerException();
    } 
    
    public void addSuccessor(Object addProposition, Behavior successor) throws NullPointerException
    {
        if(addProposition != null && successor != null)
        {
            LinkedList list = (LinkedList)successors.get(addProposition);
            if(list == null)
            {              
                list = new LinkedList();
                list.add(successor);
                successors.put(addProposition, list);
            }
            else
            {                
                if(!list.contains(successor))                
                    list.add(successor);                                    
            }
        }
        else
            throw new NullPointerException();
    }
    
    public void addConflictor(Object precondition, Behavior conflictor) throws NullPointerException
    {
        if(precondition != null && conflictor != null)
        {
            LinkedList list = (LinkedList)conflictors.get(precondition);
            if(list == null)
            {
                list = new LinkedList();
                list.add(conflictor);
                conflictors.put(precondition, list);
            }
            else
            {
                if(!list.contains(conflictor))
                    list.add(conflictor);
            }
        }
        else
            throw new NullPointerException();
    }
    
    public void addBehaviorCodelet(Object behaviorCodelet) throws NullPointerException
    {
        if(behaviorCodelet != null)
        {
            if(!behaviorCodelets.contains(behaviorCodelet))
                behaviorCodelets.add(behaviorCodelet);
        }
        else
            throw new NullPointerException();
    }    
    
    public void addExpectationCodelet(Object expectationCodelet) throws NullPointerException
    {
        if(expectationCodelet != null)
        {
            if(!expectationCodelets.contains(expectationCodelet))
                expectationCodelets.add(expectationCodelet);
        }
        else
            throw new NullPointerException();
    }     
// end add methods    
    
// start get methods.          
    
    public String getName()
    {
        return name;
    }    
    
    public double getAlpha()
    {
        return alpha;
    }
    
    public double getBeta()
    {
        return beta;
    }        
    
    public Hashtable getPreconditions()
    {
        return preconditions;
    }
    
    public LinkedList getAddList()
    {
        return addList;
    }
    
    public LinkedList getDeleteList()
    {
        return deleteList;
    }
    
    public Hashtable getPredecessors()
    {
        return predecessors;
    }
    
    public Hashtable getSuccessors()
    {
        return successors;
    } 
    
    public Hashtable getConflictors()
    {
        return conflictors;
    }
        
    public LinkedList getBehaviorCodelets()
    {
        return behaviorCodelets;
    } 
    
    public LinkedList getExpectationCodelets()
    {
        return expectationCodelets;
    }
    
    private Stream getStream()
    {
        return stream;
    }
//end get methods    
    
//start set methods    
    
    public void setPreconditions(Hashtable preconditions)
                                throws NullPointerException
                                 
    {
        if(preconditions != null)
            this.preconditions = preconditions;
        else
            throw new NullPointerException();
    }
    
    public void setAddList(LinkedList addList) throws NullPointerException                                 
    {
        if(addList != null)
            this.addList = addList;
        else
            throw new NullPointerException();
    } 
    
    public void setDeleteList(LinkedList deleteList) throws NullPointerException                                 
    {
        if(deleteList != null)
            this.deleteList = deleteList;
        else
            throw new NullPointerException();
    }
    
    public void setPredecessors(Hashtable predecessors) 
                                 throws NullPointerException
    {
        if(predecessors != null)
            this.predecessors = predecessors;
        else
            throw new NullPointerException();
    }    
    
    public void setSuccesors(Hashtable successors)
                                 throws NullPointerException
    {
        if(successors != null)
            this.successors = successors;
        else
            throw new NullPointerException();
    }    
    
    public void setConflictors(Hashtable conflictors) 
                                 throws NullPointerException
    {
        if(conflictors != null)
            this.conflictors = conflictors;
        else
            throw new NullPointerException();
    }    
    
    public void setBehaviorCodelets(LinkedList behaviorCodelets)
                                 throws NullPointerException
    {
        if(behaviorCodelets != null)
            this.behaviorCodelets = behaviorCodelets;
        else
            throw new NullPointerException();
    }
    
    public void setExpectationCodelets(LinkedList expectationCodelets)
                                 throws NullPointerException
    {
        if(expectationCodelets != null)
            this.expectationCodelets = expectationCodelets;
        else
            throw new NullPointerException();
    }
    
    public void setStream(Stream stream)throws NullPointerException                                 
    {
        if(stream != null)
            this.stream = stream;
        else
            throw new NullPointerException();
    }    
// end of set methods
    
    public SidneyCodelet getCodelet(String name) throws NullPointerException
    {
        SidneyCodelet codelet = getBehaviorCodelet(name);
        
        if(codelet == null)
            codelet = getExpectationCodelet(name);
        
        return codelet;        
    }
    
    
    public SidneyCodelet getBehaviorCodelet(String name) throws NullPointerException
    {
        SidneyCodelet codelet = null;
        if(name != null)
        {
            ListIterator li = behaviorCodelets.listIterator();
            while(li.hasNext() && codelet == null)
            {
                if(((SidneyCodelet)li.next()).getName().compareTo(name) == 0)
                    codelet = (SidneyCodelet)li.previous();
            }
        }
        else
            throw new NullPointerException();
        
        return codelet;            
    }
    
    public SidneyCodelet getExpectationCodelet(String name) throws NullPointerException
    {
        SidneyCodelet codelet = null;
        if(name != null)
        {
            ListIterator li = expectationCodelets.listIterator();
            while(li.hasNext() && codelet == null)
            {
                if(((SidneyCodelet)li.next()).getName().compareTo(name) == 0)
                    codelet = (SidneyCodelet)li.previous();
            }
        }
        else
            throw new NullPointerException();
        
        return codelet;            
    }
    
    public void reset()
    {
        alpha = 0;
        beta = 0;
        incoming = 0;
            
        history = new LinkedList();
        
        deactivate();
    }
    
    public String toString()
    {
        return name;
    }
    
    
//end set methods    
}


