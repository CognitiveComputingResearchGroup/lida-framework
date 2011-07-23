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

import java.util.Map;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 *
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class ProceduralMemoryInitializer implements Initializer {

    private ProceduralMemory proceduralMemory;
    private static final GlobalInitializer initializer = GlobalInitializer.getInstance();

    @Override
    public void initModule(FullyInitializable module, Agent agent, Map<String, ?> params) {
        proceduralMemory = (ProceduralMemory) module;
        
        //Scheme 1
        NodeStructure context = new NodeStructureImpl();
        Action action = new ActionImpl();
        initializer.setAttribute("turnLeft", action);
        NodeStructure result = new NodeStructureImpl();
        
        Node n = (Node) initializer.getAttribute("rockFront");
        context.addDefaultNode(n);
        auxAddScheme("turn left from rock", context, action, result);
        
        //Scheme 2
        context = new NodeStructureImpl();
        action = new ActionImpl();
        initializer.setAttribute("turnRight", action);
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("rockFront");
        context.addDefaultNode(n);
        auxAddScheme("turn right from rock", context, action, result);
        
        //Scheme 3
        context = new NodeStructureImpl();
        action = new ActionImpl();
        initializer.setAttribute("turnAround", action);
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("rockFront");
        context.addDefaultNode(n);
        auxAddScheme("turn around from rock", context, action, result);
        
        //Scheme 4
        context = new NodeStructureImpl();
        Action moveAction = new ActionImpl();
        initializer.setAttribute("moveAgent", moveAction);
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("foodFront");
        context.addDefaultNode(n);
        auxAddScheme("move to food", context, moveAction, result);
        
        //Scheme 5
        context = new NodeStructureImpl();
        action = new ActionImpl();
        initializer.setAttribute("eat", action);
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("foodOrigin");
        context.addDefaultNode(n);
        auxAddScheme("eat food at origin", context, action, result);
        
        //Scheme 6
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("moveAgent");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("badHealth");
        context.addDefaultNode(n);
        auxAddScheme("move forward if bad health", context, action, result);
        
        //Scheme 6a
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnLeft");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("badHealth");
        context.addDefaultNode(n);
        auxAddScheme("turnLeft if bad health", context, action, result);
        
        //Scheme 6b
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnRight");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("badHealth");
        context.addDefaultNode(n);
        auxAddScheme("turnRight if bad health", context, action, result);
        
        //Scheme 7
        context = new NodeStructureImpl();
        action =(Action) initializer.getAttribute("moveAgent");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("fairHealth");
        context.addDefaultNode(n);
        auxAddScheme("move forward if fair health", context, action, result);
        
        //Scheme 7a
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnLeft");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("fairHealth");
        context.addDefaultNode(n);
        auxAddScheme("turnLeft if fair health", context, action, result);
        
        //Scheme 7b
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnRight");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("fairHealth");
        context.addDefaultNode(n);
        auxAddScheme("turnRight if fair health", context, action, result);
        
        //Scheme 8
        context = new NodeStructureImpl();
        action= new ActionImpl();
        initializer.setAttribute("flee", action);
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("predatorFront");
        context.addDefaultNode(n);
        auxAddScheme("flee if predator in front", context, action, result);
        
        //Scheme 8a
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("flee");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("predatorOrigin");
        context.addDefaultNode(n);
        auxAddScheme("flee if predator at origin", context, action, result);
        
        
        //Scheme 9
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnLeft");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("outOfBounds");
        context.addDefaultNode(n);
        auxAddScheme("turn left from outOfBounds", context, action, result);
        
        //Scheme 9a
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnRight");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("outOfBounds");
        context.addDefaultNode(n);
        auxAddScheme("turn right from outOfBounds", context, action, result);
        
        //Scheme 9b
        context = new NodeStructureImpl();
        action = (Action) initializer.getAttribute("turnAround");
        result = new NodeStructureImpl();
        
        n = (Node) initializer.getAttribute("outOfBounds");
        context.addDefaultNode(n);
        auxAddScheme("turnAround from outOfBounds", context, action, result);
    }

    protected void auxAddScheme(String name, NodeStructure context, Action action, NodeStructure result) {
        Scheme scheme = new SchemeImpl(name, action);
        scheme.setContext(context);
        scheme.setAddingResult(result);
        proceduralMemory.addScheme(scheme);
    }
}
