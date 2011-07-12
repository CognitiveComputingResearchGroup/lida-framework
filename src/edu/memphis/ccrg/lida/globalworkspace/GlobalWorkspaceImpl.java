/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * {@link Coalition}s. It supports {@link BroadcastTrigger}s that are in charge
 * of triggering the new broadcast. Triggers should implement
 * {@link BroadcastTrigger} interface. This class maintains a list of
 * {@link BroadcastListener}s. These are the modules that need to receive
 * broadcast content.
 * 
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public class GlobalWorkspaceImpl extends FrameworkModuleImpl implements GlobalWorkspace{

    private static final Logger logger = Logger.getLogger(GlobalWorkspaceImpl.class.getCanonicalName());
    private static final ElementFactory factory = ElementFactory.getInstance();
    private static final Integer DEFAULT_REFRACTORY_PERIOD = 40;
    private static final String DEFAULT_COALITION_DECAY = factory.getDefaultDecayType();
    private static final double DEFAULT_COALITION_REMOVAL_THRESHOLD = 0.0;
    
    private double coalitionRemovalThreshold;
    private DecayStrategy coalitionDecayStrategy;
    private int broadcastRefractoryPeriod;
    private long broadcastsSentCount;
    private long tickAtLastBroadcast;
    private double winningCoalitionActivation;
    private BroadcastTrigger lastBroadcastTrigger;
    private AtomicBoolean broadcastStarted = new AtomicBoolean(false);
    
    private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
    private List<BroadcastTrigger> broadcastTriggers = new ArrayList<BroadcastTrigger>();
    private Queue<Coalition> coalitions = new ConcurrentLinkedQueue<Coalition>();

    /**
     * Constructor a new instance with default values
     */
    public GlobalWorkspaceImpl() {
    }
    
    @Override
    public void init() {
        coalitionRemovalThreshold = (Double) getParam("coalitionRemovalThreshold", DEFAULT_COALITION_REMOVAL_THRESHOLD);

        String coalitionDecayStrategyName = (String) getParam("coalitionDecayStrategy", DEFAULT_COALITION_DECAY);
        coalitionDecayStrategy = factory.getDecayStrategy(coalitionDecayStrategyName);
        if(coalitionDecayStrategy == null){
        	coalitionDecayStrategy = factory.getDefaultDecayStrategy();
        	logger.log(Level.WARNING, "failed to obtain decay strategy {0}, using default", coalitionDecayStrategyName);
        }
                
    	int refractoryPeriod = (Integer)getParam("globalWorkspace.refractoryPeriod", DEFAULT_REFRACTORY_PERIOD);
    	setRefractoryPeriod(refractoryPeriod);
    	
        taskSpawner.addTask(new StartTriggersTask());
    }

    private class StartTriggersTask extends FrameworkTaskImpl {
        public StartTriggersTask() {
            super(1);
        }

        @Override
        protected void runThisFrameworkTask() {
            for (BroadcastTrigger t : broadcastTriggers) {
                t.start();
            }
            setTaskStatus(TaskStatus.FINISHED); // Runs only once
        }
    }

    @Override
    public void addListener(ModuleListener listener) {
        if (listener instanceof BroadcastListener) {
            addBroadcastListener((BroadcastListener) listener);
        } else {
            logger.log(Level.WARNING,
                    "Can only add listeners of type BroadcastListener. Tried to add {1}",
                    new Object[]{TaskManager.getCurrentTick(), listener});
        }
    }
    
    @Override
    public void addBroadcastListener(BroadcastListener bl) {
        broadcastListeners.add(bl);
    }

    @Override
    public void addBroadcastTrigger(BroadcastTrigger t) {
        broadcastTriggers.add(t);
    }

    @Override
    public boolean addCoalition(Coalition coalition) {
       coalition.setDecayStrategy(coalitionDecayStrategy);
       coalition.setActivatibleRemovalThreshold(coalitionRemovalThreshold);
        
        if (coalitions.add(coalition)) {
            logger.log(Level.FINEST, "New Coalition added with activation {1}",
                    new Object[]{TaskManager.getCurrentTick(), coalition.getActivation()});
            newCoalitionEvent();
            return true;
        } else {
            return false;
        }
    }

    private void newCoalitionEvent() {
        for (BroadcastTrigger trigger : broadcastTriggers) {
            trigger.checkForTriggerCondition(coalitions);
        }
    }

    @Override
    public void triggerBroadcast(BroadcastTrigger trigger) {
        if (broadcastStarted.compareAndSet(false, true)) {
            if(TaskManager.getCurrentTick() - tickAtLastBroadcast < broadcastRefractoryPeriod){
                broadcastStarted.set(false);
                return;
            }
            
            boolean broadcastWasSent = sendBroadcast();
            if (broadcastWasSent) {
                lastBroadcastTrigger = trigger;
            }
        }
    }

    /*
     * This method realizes the broadcast. First it chooses the winner
     * coalition. Then, all registered {@link BroadcastListener}s receive a
     * reference to the coalition content. The winning Coalition is removed from
     * the pool. Broadcast recipients must return as soon as possible in order
     * to not delay the rest of the broadcasting. A good implementation should
     * copy the broadcast content and create a task to process it. This method
     * is supposed to be called from {@link BroadcastTrigger}s. The reset()
     * method is invoked on each trigger at the end of this method.
     */
    private boolean sendBroadcast() {
        logger.log(Level.FINEST, "Triggering broadcast",
                TaskManager.getCurrentTick());
        boolean broadcastWasSent = false;
        Coalition winningCoalition = chooseCoalition();
        if (winningCoalition != null) {
            winningCoalitionActivation = winningCoalition.getActivation();
            coalitions.remove(winningCoalition);
            NodeStructure copy = ((NodeStructure) winningCoalition.getContent()).copy();
            // TODO Create FrameworkTask for parallel processing
            for (BroadcastListener bl : broadcastListeners) {
                bl.receiveBroadcast((BroadcastContent) copy);
            }

            logger.log(Level.FINEST, "Broadcast Performed at tick: {0}",
                    TaskManager.getCurrentTick());
            broadcastsSentCount++;
            tickAtLastBroadcast = TaskManager.getCurrentTick();
            broadcastWasSent = true;
        }
        resetTriggers();
        broadcastStarted.set(false);
        return broadcastWasSent;
    }

    private Coalition chooseCoalition() {
        Coalition chosenCoal = null;
        for (Coalition c : coalitions) {
            if (chosenCoal == null
                    || c.getActivation() > chosenCoal.getActivation()) {
                chosenCoal = c;
            }
        }
        return chosenCoal;
    }

    private void resetTriggers() {
        for (BroadcastTrigger t : broadcastTriggers) {
            t.reset();
        }
    }

    @Override
    public Object getModuleContent(Object... params) {
        if (params.length > 0) {
            if ("winnerCoalActivation".equals(params[0])) {
                return winningCoalitionActivation;
            } else if ("lastBroadcastTrigger".equals(params[0])) {
                return lastBroadcastTrigger;
            } else if ("coalitions".equals(params[0])) {
                return Collections.unmodifiableCollection(coalitions);
            }
        }
        return Collections.unmodifiableCollection(coalitions);
    }

    @Override
    public void decayModule(long ticks) {
        decay(ticks);
        logger.log(Level.FINEST, "Coalitions Decayed",
                TaskManager.getCurrentTick());
    }

    private void decay(long ticks) {
        for (Coalition c : coalitions) {
            c.decay(ticks);
            if (c.isRemovable()) {
                coalitions.remove(c);
                logger.log(Level.FINEST, "Coalition removed",
                        TaskManager.getCurrentTick());
            }
        }
    }

    @Override
    public long getBroadcastSentCount() {
        return broadcastsSentCount;
    }

    /**
     * Gets refractoryPeriod
     * @return number of ticks that must pass after a broadcast has been sent before
     * a new one can be sent.
     */
    @Override
    public int getRefractoryPeriod() {
        return broadcastRefractoryPeriod;
    }
    

	@Override
    public long getTickAtLastBroadcast() {
        return tickAtLastBroadcast;
    }

    /**
	 * Sets refractoryPeriod
	 * 
	 * @param period number of ticks that must pass after a broadcast has been
	 * sent before a new one can be sent.
	 */
    @Override
    public void setRefractoryPeriod(int period) {
    	if (period > 0) {
    		broadcastRefractoryPeriod = period;
    	}else{
    		broadcastRefractoryPeriod = DEFAULT_REFRACTORY_PERIOD;
    		logger.log(Level.WARNING,
    				"refractory period must be positive, using default value",
    				TaskManager.getCurrentTick());
    	}
    }

    @Override
	public double getCoalitionRemovalThreshold() {
		return coalitionRemovalThreshold;
	}

	@Override
	public void setCoalitionRemovalThreshold(double coalitionRemovalThreshold) {
		this.coalitionRemovalThreshold = coalitionRemovalThreshold;
	}

	@Override
	public DecayStrategy getCoalitionDecayStrategy() {
		return coalitionDecayStrategy;
	}

	@Override
	public void setCoalitionDecayStrategy(DecayStrategy coalitionDecayStrategy) {
		this.coalitionDecayStrategy = coalitionDecayStrategy;
	}

}
