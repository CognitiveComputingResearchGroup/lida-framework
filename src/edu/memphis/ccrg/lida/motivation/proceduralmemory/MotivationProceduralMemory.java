package edu.memphis.ccrg.lida.motivation.proceduralmemory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.motivation.shared.FeelingNode;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class MotivationProceduralMemory extends ProceduralMemoryImpl {

	private static final Logger logger = Logger.getLogger(MotivationProceduralMemory.class.getCanonicalName());
	private boolean isLesioned;
	private LinkCategory temporalCategory;
	
	@Override
	public void init() {
		super.init();
		isLesioned = getParam("proceduralMemory.isLesioned", false);
	}

	void setTemporalLinkCategory(LinkCategory c) {
		temporalCategory = c;
	}

	@Override
	public void receiveBroadcast(Coalition coalition) {
		NodeStructure ns = (NodeStructure) coalition.getContent();
		logger.log(Level.INFO, " Broadcast at {0}: {1}",
					new Object[]{TaskManager.getCurrentTick(), ns.toString()}); //TODO Remove
		for (Node broadcastNode: ns.getNodes()) {
			// For each broadcast node, check if it is in the condition pool,
			// i.e., there is at least 1 scheme that has context or result
			// condition equal to the node.
			Node condition = (Node) conditionPool.get(broadcastNode.getConditionId());
			if (condition != null) { //Add node to broadcast buffer only if already in the condition pool
				if (!broadcastBuffer.containsNode(condition)) {
					// Add a reference to the condition pool Node to the
					// broadcast buffer without copying
					broadcastBuffer.addNode(condition, false);
				}
				// Update the activation of the condition-pool/broadcast-buffer
				// node if needed
				if (broadcastNode.getActivation() > condition.getActivation()) {
					condition.setActivation(broadcastNode.getActivation());
				}
				// Since the incentive salience can change signs, here, we set directly.
				condition.setIncentiveSalience(broadcastNode.getIncentiveSalience());
			}
		}
		// Spawn a new task to activate and instantiate relevant schemes.
		// This task runs at the next time and only one time.
		FrameworkTask t = new MotivationProceduralMemoryTask(ns);
		taskSpawner.addTask(t);
	}
	
	private class MotivationProceduralMemoryTask extends FrameworkTaskImpl{
		
		private NodeStructure broadcastContent;

		public MotivationProceduralMemoryTask(NodeStructure ns) {
			broadcastContent=ns;
		}

		@Override
		protected void runThisFrameworkTask() {
			Set<Scheme> candidateSchemes = new HashSet<Scheme>();
			for (Node n: broadcastBuffer.getNodes()) {
				double incentiveSalience = 0;
				if(isLesioned){
					incentiveSalience = n.getIncentiveSalience();
				}else{ 
					incentiveSalience = getModelBasedIncentiveSalience(n);
				}
				if (incentiveSalience >= 0) {
					Set<Scheme> schemes = contextSchemeMap.get(n.getConditionId());
					if(schemes != null){
						candidateSchemes.addAll(schemes);
					}
				}
			}
			for (Scheme scheme: candidateSchemes) {
				if (shouldInstantiate(scheme, broadcastBuffer)) {
					createInstantiation(scheme);
				}
			}
			cancel();
		}

		private double mbIncentiveSalienceSum;
		private int mbNodeCount;
		
		private double getModelBasedIncentiveSalience(Node n) {
			mbIncentiveSalienceSum = n.getIncentiveSalience();
			mbNodeCount = 1;
			auxGetModelBasedIncSal(n);
			return mbIncentiveSalienceSum/mbNodeCount;
		}
		private void auxGetModelBasedIncSal(Node n){
			Map<Linkable, Link> sinkMap = broadcastContent.getConnectedSinks(n);
			for(Linkable lnk: sinkMap.keySet()){
				if(lnk instanceof Node && !(lnk instanceof FeelingNode)){
					Link l = sinkMap.get(lnk);
					if(l != null && temporalCategory.equals(l.getCategory())){
						mbIncentiveSalienceSum += ((Node)lnk).getIncentiveSalience();
						mbNodeCount++;
						auxGetModelBasedIncSal((Node)lnk);
					}
				}
			}
		}
	}

}