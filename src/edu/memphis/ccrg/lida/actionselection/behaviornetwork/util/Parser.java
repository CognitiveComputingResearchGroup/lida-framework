/*
 * Parser.java
 *
 * Sidney D'Mello
 * Created on December 18, 2003, 1:59 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.*;


import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorCodelet;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorNetworkImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.ExpectationCodelet;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Goal;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.ProtectedGoal;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.SidneyCodelet;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Stream;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.ReinforcementCurve;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Reinforcer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.SigmoidCurve;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Parser 
{            
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.util.Parser");
	
    public Parser() 
    {        
    }
    
/*
    Naming Convention:
        Net objects: 
            full name: example Behavior, Codelet etc
        Elements:
            Single elements by single character representing the element 
            followed by an 's': example gs for Goals. 
            Multiple elements by a single character representing the object:
            example g for goal.
        NodeList:
            Single character represeting the object followed by an 'l'.
            example gl for goal list.
            
        Note: The 'r' is added for PropertyList and Property.
 *
 *      NOTE: The Parser currently activates ALL goals  
 */    
    
    public BehaviorNetworkImpl parse(String netFileName)
    {
        BehaviorNetworkImpl net = new BehaviorNetworkImpl();
        List<Goal> goals = new ArrayList<Goal>();
        List<Stream> streams = new ArrayList<Stream>();
        Document document = null;
        logger.log(Level.INFO, "Parse request with file: " + netFileName);
        try{
            document = open(netFileName);
            if(document != null)
            {   
                Element n = document.getDocumentElement();                      //net
                logger.info(n.getTagName());
                
                Node ct = n.getElementsByTagName(ParserTags.CONSTANTS).item(0); //read net constants
                logger.log(Level.INFO, "\t" + ct.getNodeName());
                
                net.setTheta(Double.parseDouble(ct.getAttributes().getNamedItem(ParserTags.THETA).getNodeValue().trim()));
                net.setPhi(Double.parseDouble(ct.getAttributes().getNamedItem(ParserTags.PHI).getNodeValue().trim()));
                net.setGamma(Double.parseDouble(ct.getAttributes().getNamedItem(ParserTags.GAMMA).getNodeValue().trim()));
                net.setDelta(Double.parseDouble(ct.getAttributes().getNamedItem(ParserTags.DELTA).getNodeValue().trim()));
                net.setPi(Double.parseDouble(ct.getAttributes().getNamedItem(ParserTags.PI).getNodeValue().trim()));
                net.setOmega(Double.parseDouble(ct.getAttributes().getNamedItem(ParserTags.OMEGA).getNodeValue().trim()));                
                                                
                Element gs = (Element)n.getElementsByTagName(ParserTags.GOALS).item(0); //goals
                logger.info("\t" + gs.getTagName());
                
                NodeList gl = gs.getElementsByTagName(ParserTags.GOAL);                 //get list of goals
                for(int i = 0; i < gl.getLength(); i++)                                 //iterate over goals
                {                    
                    Element g = (Element)gl.item(i);                                    //read a goal
                    logger.info("\t\t" + g.getTagName());
                 
                    Goal goal = new Goal(g.getAttribute(ParserTags.NAME).trim(),
                                                 Boolean.valueOf(g.getAttribute(ParserTags.PERSISTENT).trim()).booleanValue());

                    if(Boolean.valueOf(g.getAttribute(ParserTags.PROTECTED).trim()).booleanValue())
                        goal = new ProtectedGoal(g.getAttribute(ParserTags.NAME).trim(),
                                                 Boolean.valueOf(g.getAttribute(ParserTags.PERSISTENT).trim()).booleanValue());
                    
                    Element ps = (Element)g.getElementsByTagName(ParserTags.PROPOSITIONS).item(0);  //get propositions
                    NodeList pl = ps.getElementsByTagName(ParserTags.PROPOSITION);                  //iterate over propositions
                    for(int j = 0; j < pl.getLength(); j++)
                    {
                        //System.out.println(pl.item(j).getFirstChild().getNodeValue().trim());
                        goal.getExcitatoryPropositions().put(pl.item(j).getFirstChild().getNodeValue().trim(), new LinkedList());
                    }
                    goal.activate();
                    goals.add(goal);
                }
                
                Element ss = (Element)n.getElementsByTagName(ParserTags.STREAMS).item(0);       //get streams
                logger.info("\t" + ss.getTagName());
                
                NodeList sl = ss.getElementsByTagName(ParserTags.STREAM);                       
                for(int i = 0; i < sl.getLength(); i++)                                         //iterate over streams
                {                    
                    Element s = (Element)sl.item(i);
                    logger.info("\t\t" + s.getTagName());
                    
                    Stream stream = new Stream(s.getAttribute(ParserTags.NAME).trim());
                    streams.add(stream);
                    
                    Element bs = (Element)s.getElementsByTagName(ParserTags.BEHAVIORS).item(0);
                    logger.info("\t\t\t" + bs.getTagName());
                    
                    NodeList bl = bs.getElementsByTagName(ParserTags.BEHAVIOR);
                    for(int j = 0; j < bl.getLength(); j++)                                     //iterate over behaviors
                    {                    
                        Element b = (Element)bl.item(j);
                        logger.info("\t\t\t\t" + b.getTagName());
                        
                        Behavior behavior = new Behavior(b.getAttribute(ParserTags.NAME).trim());                        
                        stream.addBehavior(behavior);

                        Element ps = (Element)b.getElementsByTagName(ParserTags.PRECONDITIONS).item(0);
                        logger.info("\t\t\t\t\t" + ps.getTagName());
                        
                        NodeList pl = ps.getElementsByTagName(ParserTags.PROPOSITION);          //iterate over preconditions
                        for(int k = 0; k < pl.getLength(); k++)
                        {
                            //System.out.println(pl.item(k).getFirstChild().getNodeValue().trim());
                            behavior.getPreconditions().put(pl.item(k).getFirstChild().getNodeValue().trim(), new Boolean(false));                            
                        }                 
                        
                        Element as = (Element)b.getElementsByTagName(ParserTags.ADDLIST).item(0);
                        if(as != null)
                        {
                            logger.info("\t\t\t\t\t" + as.getTagName());
                        
                            NodeList al = as.getElementsByTagName(ParserTags.PROPOSITION);
                            for(int k = 0; k < al.getLength(); k++)                             //iterate over add list
                            {
                                //System.out.println(al.item(k).getFirstChild().getNodeValue().trim());
                                behavior.getAddList().add(al.item(k).getFirstChild().getNodeValue().trim());                            
                            }
                        }
                        else
                        {
                            logger.warning("Empty add list: " + behavior.getName());
                        }
                        
                        Element ds = (Element)b.getElementsByTagName(ParserTags.DELETELIST).item(0);
                        if(ds != null)
                        {
                            logger.info("\t\t\t\t\t" + ds.getTagName());
                        
                            NodeList dl = ds.getElementsByTagName(ParserTags.PROPOSITION);      //iterate over delete list
                            for(int k = 0; k < dl.getLength(); k++)
                            {
                                //System.out.println(dl.item(k).getFirstChild().getNodeValue().trim());
                                behavior.getDeleteList().add(dl.item(k).getFirstChild().getNodeValue().trim());                            
                            }
                        }
                        
                        Element cs = (Element)b.getElementsByTagName(ParserTags.CODELETS).item(0);
                        logger.info("\t\t\t\t\t" + cs.getTagName());
                        
                        NodeList cl = cs.getElementsByTagName(ParserTags.CODELET);              //iterate over codelets
                        for(int k = 0; k < cl.getLength(); k++)
                        {
                            Element c = (Element)cl.item(k);
                            logger.info("\t\t\t\t\t\t" + c.getTagName());
                                                    
                            SidneyCodelet codelet = (SidneyCodelet)Class.forName(c.getAttribute(ParserTags.CLASS).trim()).newInstance();
                            codelet.setName(c.getAttribute(ParserTags.NAME).trim());
                            codelet.setBehavior(behavior);
                            
                            if(c.getAttribute(ParserTags.TYPE).trim().compareToIgnoreCase(ParserTags.BEHAVIOR) == 0)
                            {
                                codelet.setType(SidneyCodelet.BEHAVIOR);
                                behavior.addBehaviorCodelet((BehaviorCodelet) codelet);
                            }
                            else if(c.getAttribute(ParserTags.TYPE).trim().compareToIgnoreCase(ParserTags.EXPECTATION) == 0)
                            {
                                codelet.setType(SidneyCodelet.EXPECTATION);                                
                                behavior.addExpectationCodelet((ExpectationCodelet) codelet);
                            }                            
                            Element prs = (Element)c.getElementsByTagName(ParserTags.PROPERTIES).item(0);
                            if(prs != null)
                            {                                
                                Hashtable properties = new Hashtable();                                
                                logger.info("\t\t\t\t\t\t" + prs.getTagName());
                        
                                NodeList prl = prs.getElementsByTagName(ParserTags.PROPERTY);           //iterate over properties
                                for(int l = 0; l < prl.getLength(); l++)
                                {
                                    Element pr = (Element)prl.item(l); 
                                    //System.out.println(prl.item(l).getFirstChild().getNodeValue().trim());
                                                                        
                                    properties.put(pr.getAttribute(ParserTags.NAME).trim(),
                                                   pr.getAttribute(ParserTags.VALUE).trim());
                                }
                                codelet.setProperties(properties);
                            }
                        }
                    }
                }                
                Element r = (Element)n.getElementsByTagName(ParserTags.REINFORCER).item(0); //reinforcer
                if(r != null)
                {
                    Reinforcer reinforcer = new Reinforcer();                
                    logger.info("\t" + r.getTagName());                
                                
                    NodeList rl = r.getElementsByTagName(ParserTags.SIGMOID);                               
                    for(int l = 0; l < rl.getLength(); l++)
                    {
                        Element sr = (Element)rl.item(l);                         
                    
                        ReinforcementCurve curve = (ReinforcementCurve) new SigmoidCurve(Double.parseDouble(sr.getAttribute(ParserTags.A).trim()), 
                                                                    Double.parseDouble(sr.getAttribute(ParserTags.C).trim()));
                   
                        reinforcer.setReinforcementCurve(curve);
                    }
                    net.setReinforcer(reinforcer);
                }                
            }
            else
            {
                
            }
        }
        catch (Exception e)
        {
            logger.warning("Exception: " + e.getMessage());
            
            e.printStackTrace();
        }
        return net;
    }
    
    private Document open(String xmlFileName)
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();            
       
            document = builder.parse(xmlFileName);
        }
        catch (Exception e)
        {
            logger.warning("Exception: " + e.getMessage());
            
            e.printStackTrace();
            document = null;
        }
        return document;
    }        
    
    public static void main(String[] args) throws Exception
    {                
        new Parser().parse("/home/sdmello/bnt/src/edu/memphis/bnt/jut/util/dat/ParserTest.xml");
    }
}
