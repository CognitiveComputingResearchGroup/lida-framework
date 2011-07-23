/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alifeagent.initializers;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.pam.BasicPamInitializer;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import java.util.Map;

/**
 *
 * @author UofM
 */
public class CustomPamInitializerSolution extends BasicPamInitializer{
    
    @Override
    public void initModule(FullyInitializable module, Agent agent, Map<String, ?> params) {
        super.initModule(module, agent, params);
        PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
        
        // Obtains the ElementFactory
        ElementFactory factory = ElementFactory.getInstance();
        
        // Creates a new Node in PAM labeled 'object'
        Node objectNode = pam.addDefaultNode(factory.getNode("PamNodeImpl", "object"));
        
        // Obtains the 'rock' node from PAM
        Node child = pam.getNode("rock"); 
        
        //  Adds a Link in PAM from 'rock' to 'object' with link category PARENT
        pam.addDefaultLink(factory.getLink(child, objectNode, PerceptualAssociativeMemoryImpl.PARENT));

        // Task 2: INSERT YOUR CODE HERE **************************
        child = pam.getNode("food");        
        pam.addDefaultLink(factory.getLink(child, objectNode, PerceptualAssociativeMemoryImpl.PARENT));
        
        // Task 3: INSERT YOUR CODE HERE **************************
        DecayStrategy decayStrategy = factory.getDecayStrategy("slowDecay");
        objectNode.setDecayStrategy(decayStrategy);
        
    }
}
