/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem.MPT;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This basic MPT is designed based on the idea of Subsumption Architecture (SA)
 * @author Daqi
 */
public abstract class SubsumptionMPTImpl implements MPT{

    public static final Logger logger = Logger
                    .getLogger(SubsumptionMPTImpl.class.getCanonicalName());

    protected Map<String, Object> sensedData = new HashMap<String, Object>();

    protected Map<String, Object> commands = new HashMap<String, Object>();

    protected TaskSpawner taskSpawner;

    //TODO: Here we just built-in the FSM and their relationship as a
    // subsumption architecture. Actually, it's better to store the set of
    // FSM and their relationship in a long-term memory as a template and
    // load it when use it.
    @Override
    public void load() {
        //TODO: Impl
    }

    @Override
    public void save() {
        //TODO: Impl
    }

    @Override
    public void receiveData(Object o) {
        if (o instanceof HashMap) {
            sensedData.putAll((HashMap) o);
        } else {
            logger.log(Level.INFO, "Sensed data is not a HashMap!", TaskManager.getCurrentTick());
        }
    }

    @Override
    public Object outputCommands() {
        return commands;
    }

    @Override
    public void receiveTS(TaskSpawner ts){
        taskSpawner = ts;
    }

    /*
     * Suppress: Replace the lower-level FSM's output
     * Idea: The semantics of these nodes is that a signal coming into the side
     * can override the signal passing through.
     * Usage: This inner class is supposed to act in the onlineControl() method at a concrete
     * class extending this SubsumptionMPT class
     */
    protected class suppress extends FrameworkTaskImpl {

        private Map<String, Object> higher = new HashMap<String, Object>();
        private Map<String, Object> lower = new HashMap<String, Object>();
        private Map<String, Object> cmd = new HashMap<String, Object>();

        public suppress(int ticksPerRun) {
            super(ticksPerRun);
        }

        public void inputHigherLayerOutput(Object HigherFSMOutput){

            if (HigherFSMOutput != null && HigherFSMOutput instanceof HashMap)
                higher = (HashMap<String, Object>) HigherFSMOutput;
        }

        public void inputLowerLayerOutput(Object LowerFSMOutput){

            if (LowerFSMOutput != null && LowerFSMOutput instanceof HashMap)
                lower = (HashMap<String, Object>) LowerFSMOutput;
        }

        public Map<String, Object> output(){
            return cmd;
        }

        @Override
        protected void runThisFrameworkTask() {

            // If there is at least one nontrivial item in higher level layer
            // then substitute the lower one by it; otherwise reuse lower one.
            // TODO: Instead of checking each item's value every time, hold a
            // special bit in the commands variable to indicate whether the content
            // is Null.
            boolean nontrivial = false;
            for (String key : higher.keySet()){
                if (higher.get(key) != null){
                    nontrivial = true;
                    break;
                }
            }

            if (nontrivial == true)
                cmd = higher;
            else
                cmd = lower;
        }
    }

    /*
     * Five types of wires:
     * 1) A FSM --> A Suppress Node's lower input layer
     * 2) A FSM --> A Suppress Node's higher input layer
     * 3) A Suppress Node --> A Suppress Node's lower input layer
     * 4) A Suppress Node --> A Suppress Node's higher input layer
     * 5) A Suppress Node --> MPT output
     */
    protected class Wire_FSM_S_LOWER extends FrameworkTaskImpl {

        FSM fsm1;
        suppress snode1;

        public Wire_FSM_S_LOWER(int ticksPerRun, FSM fsm, suppress snode) {
            super(ticksPerRun);

            fsm1 = fsm;
            snode1 = snode;
        }

        @Override
        protected void runThisFrameworkTask() {
            snode1.inputLowerLayerOutput(fsm1.outputCommands());
        }

    }

    protected class Wire_FSM_S_HIGHER extends FrameworkTaskImpl {

        FSM fsm1;
        suppress snode1;

        public Wire_FSM_S_HIGHER(int ticksPerRun, FSM fsm, suppress snode) {
            super(ticksPerRun);

            fsm1 = fsm;
            snode1 = snode;
        }

        @Override
        protected void runThisFrameworkTask() {
            snode1.inputHigherLayerOutput(fsm1.outputCommands());
        }

    }

    protected class Wire_S_S_LOWER extends FrameworkTaskImpl {

        suppress snode1, snode2;

        public Wire_S_S_LOWER(int ticksPerRun, suppress snode_former, suppress snode_later) {
            super(ticksPerRun);

            snode1 = snode_former;
            snode2 = snode_later;
        }

        @Override
        protected void runThisFrameworkTask() {
            snode2.inputLowerLayerOutput(snode1.output());
        }

    }

    protected class Wire_S_S_HIGHER extends FrameworkTaskImpl {

        suppress snode1, snode2;

        public Wire_S_S_HIGHER(int ticksPerRun, suppress snode_former, suppress snode_later) {
            super(ticksPerRun);

            snode1 = snode_former;
            snode2 = snode_later;
        }

        @Override
        protected void runThisFrameworkTask() {
            snode2.inputHigherLayerOutput(snode1.output());
        }

    }

    protected class Wire_S_CMD extends FrameworkTaskImpl {

        suppress snode1;

        Map<String, Object> cmd1;

        public Wire_S_CMD(int ticksPerRun, suppress snode, Map<String, Object> cmd) {
            super(ticksPerRun);

            snode1 = snode;
            cmd1 = cmd;
        }

        @Override
        protected void runThisFrameworkTask() {
            cmd1.putAll(snode1.output());
        }

    }
}
