/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.CodeletImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Abstract implementation of {@link AttentionCodelet} that checks the CSM for desired
 * content.  If this is found it creates a
 * {@link Coalition} and adds it to the {@link GlobalWorkspace}.
 * 
 * @author Ryan J McCall
 * 
 */
public abstract class AttentionCodeletImpl extends CodeletImpl implements
        AttentionCodelet {

    private static final Logger logger = Logger.getLogger(AttentionCodeletImpl.class.getCanonicalName());
    private static final String DEFAULT_COALITION_DECAY = "coalitionDecay";
    private static final int DEFAULT_REFRACTORY_PERIOD = 50;
    private DecayStrategy coalitionDecayStrategy;
    /**
     * Where codelet will look for and retrieve content from
     */
    protected WorkspaceBuffer currentSituationalModel;
    /**
     * Module where codelet will add {@link Coalition}
     */
    protected GlobalWorkspace globalWorkspace;

    private int refractoryPeriod;

    /**
     * Default constructor
     */
    public AttentionCodeletImpl() {
        super();
    }

    @Override
    public void init() {
        refractoryPeriod = (Integer) getParam("refractoryPeriod", DEFAULT_REFRACTORY_PERIOD);
        String coalitionDecayStrategyName = (String) getParam("coalitionDecayStrategy", DEFAULT_COALITION_DECAY);
        double initialActivation = (Double) getParam("initialActivation", getActivation());
        setActivation(initialActivation);
        coalitionDecayStrategy = ElementFactory.getInstance().getDecayStrategy(coalitionDecayStrategyName);
        
        
    }

    /**
     * Sets associated Module
     *
     * @param module
     *            the module to be associated with
     * @param usage
     *            - way of associating the module
     */
    @Override
    public void setAssociatedModule(FrameworkModule module, String usage) {
        if (usage.equals(ModuleUsage.TO_READ_FROM)) {
            if (module instanceof WorkspaceBuffer) {
                currentSituationalModel = (WorkspaceBuffer) module;
            }
        } else if (usage.equals(ModuleUsage.TO_WRITE_TO)) {
            if (module instanceof GlobalWorkspace) {
                globalWorkspace = (GlobalWorkspace) module;
            }
        } else {
            logger.log(Level.WARNING, "Module usage not supported {1}",
                    new Object[]{TaskManager.getCurrentTick(),usage});
        }
    }

    /**
     * On finding sought content in CSM, create a coalition and add it to the
     * {@link GlobalWorkspace}.
     */
    @Override
    protected void runThisFrameworkTask() {
        if (bufferContainsSoughtContent(currentSituationalModel)) {
            NodeStructure csmContent = retrieveWorkspaceContent(currentSituationalModel);
            if (csmContent.getLinkableCount() > 0) {
                Coalition coalition = new CoalitionImpl(csmContent, getActivation(), this);
                coalition.setDecayStrategy(coalitionDecayStrategy);
                globalWorkspace.addCoalition(coalition);
                setNextTicksPerRun(refractoryPeriod);
                logger.log(Level.FINER, "{1} adds new coalition with activation {2}",
                        new Object[]{TaskManager.getCurrentTick(), this, coalition.getActivation()});
            }
        }
    }
}
