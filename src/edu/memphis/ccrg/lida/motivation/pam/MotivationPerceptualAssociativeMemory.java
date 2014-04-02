package edu.memphis.ccrg.lida.motivation.pam;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.motivation.shared.FeelingNodeImpl;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;

/**
 * A motivationally extended {@link PerceptualAssociativeMemory} which adds support for 
 * incentive salience, including current incentive salience passing. Additionally, a detailed
 * learning algorithm, which also concerns incentive salience is included.
 * @author Ryan J McCall
 */
public class MotivationPerceptualAssociativeMemory extends PerceptualAssociativeMemoryImpl {
	
	private static final Logger logger = Logger.getLogger(MotivationPerceptualAssociativeMemory.class.getCanonicalName());

	private String incentiveSalienceLinkType;
	/* 
	 * The label of the temporal LinkCategory.
	 */
	private String temporalLinkCategoryLabel;
	/* 
	 * The degree to which the base-level incentive salience of events effect the update to their immediate predecessors.
	 * A value of 0.0 means the future is completely discounted or ignored, while a value of 1.0 produces a more
	 * future-oriented update process.
	 */
	private double discountRate;
	/*
	 * A general learning rate parameter scaling the change in learned quantities. 
	 */
	private double learningRate;
	
	@Override
	public void init(){
		super.init();
		incentiveSalienceLinkType = getParam("pam.incentiveSalienceLinkType", "FeelingPamLinkImpl");
		temporalLinkCategoryLabel = getParam("pam.temporalLinkCategoryName","");
		double param = getParam("pam.discountRate",0.0);
		if(param<0.0 || param>1.0){
			param = 0.0;
			logger.log(Level.WARNING,"Discount rate must be in the closed interval: [0,1].",TaskManager.getCurrentTick());
		}
		discountRate = param;
		param = getParam("pam.learningRate",1.0);
		if(param<0.0 || param>1.0){
			param = 0.0;
			logger.log(Level.WARNING,"Learning rate must be in the closed interval: [0,1].",TaskManager.getCurrentTick());
		}
		learningRate = param;
	}
	
	@Override
	protected void propagateActivation(PamLinkable src, PamLink link, double amount) {
		FrameworkTask task = null;
		int tpr = getPropagationTaskTicksPerRun();
		if(src instanceof FeelingPamNodeImpl && link instanceof FeelingPamLinkImpl){
			FeelingPamNodeImpl fNode = (FeelingPamNodeImpl) src;
			amount = fNode.getAffectiveValence()*getUpscaleFactor();
			task = new IncentiveSaliencePropagationTask(tpr,(FeelingPamLinkImpl)link,amount,this);
		}else{
			task = new PropagationTask(tpr, link, amount, this);
		}		
		taskSpawner.addTask(task);
	}
	
	@Override
	public boolean isOverPerceptThreshold(PamLinkable l) {
		return (l.getTotalActivation()+l.getTotalIncentiveSalience())>getPerceptThreshold();
	}
	
	@Override
	public void learn(Coalition c){
		NodeStructure content = (NodeStructure) c.getContent();
		double totalAffectiveValence = getTotalAffectiveValence(content);
		learnNodes(content, totalAffectiveValence);
		//
		Node temporalLinkCategory = getNode(temporalLinkCategoryLabel);
		if(temporalLinkCategory instanceof LinkCategory){
			learnLinks(content, totalAffectiveValence, temporalLinkCategory);
		}else{
			logger.log(Level.WARNING,"Cannot retrieve LinkCategory with label: {1}",
						new Object[]{TaskManager.getCurrentTick(),temporalLinkCategoryLabel});
		}
	}

	private static double getTotalAffectiveValence(NodeStructure content) {
		double totalAffectiveValence=0.0;
		int feelingNodeCount = 0;
		for(Node n: content.getNodes()){
			if(n instanceof FeelingNodeImpl){
				totalAffectiveValence += ((FeelingNodeImpl)n).getAffectiveValence();
				feelingNodeCount++;
			}
		}
		return feelingNodeCount==0? 0: totalAffectiveValence/feelingNodeCount;
	}
	private void learnNodes(NodeStructure broadcastContent, double totalAffectiveValence) {
		for(Node n: broadcastContent.getNodes()){  
			if(!(n instanceof FeelingNodeImpl)){ //Only for non-feeling nodes
				PamNode pn = (PamNode) getNode(n.getId());
				if(pn == null){ 
					pn = (PamNode) pamNodeStructure.addDefaultNode(n);
				}else{
					double reinforcementAmount = n.getActivation();
					pn.reinforceBaseLevelActivation(learningRate*reinforcementAmount);
				}
				pn.reinforceBaseLevelIncentiveSalience(learningRate*totalAffectiveValence);
			}
		}
	}

	private void learnLinks(NodeStructure content, double totalAffectiveValence, Node temporalLinkCategory) {
		for(Link l: content.getLinks()){
			if(temporalLinkCategory.equals(l.getCategory())){
				PamLink pl = (PamLink) getLink(l.getExtendedId());
				if(pl == null){
					pl = (PamLink) pamNodeStructure.addDefaultLink(l);
				}else{
					double reinforcementAmount = l.getActivation();
					pl.reinforceBaseLevelActivation(learningRate*reinforcementAmount);
				}
				pl.reinforceBaseLevelIncentiveSalience(learningRate*totalAffectiveValence);
				learnTemporalDifference(content, l);
			}
		}
	}
	/*
	 * Performs learning based on temporal differences between events connected by specified link.
	 * @param ns the NodeStructure containing the events and link
	 * @param l a temporal link
	 */
	private void learnTemporalDifference(NodeStructure ns, Link l) {
		PamNode source = (PamNode) getNode(l.getSource().getId());
		PamNode sink = (PamNode) getNode(l.getSink().getExtendedId());
		if(source != null && sink != null){
			//TD Learning: Update source based on difference in base-level incentive salience
			double difference = sink.getBaseLevelIncentiveSalience()-source.getBaseLevelIncentiveSalience();
			source.reinforceBaseLevelIncentiveSalience(learningRate*discountRate*difference);
			//Incentive Salience links
			learnIncentiveSalienceLinks(ns, source, sink);
		}
	}

	/*
	 * Given a NodeStructure containing 'sourceEvent' temporally linked to 'sinkEvent,' determine
	 * if there was a change in a drive feeling node. If so, learn or update an incentive salience link.
	 */
	private void learnIncentiveSalienceLinks(NodeStructure ns, PamNode sourceEvent, PamNode sinkEvent) {
		Collection<Node> sourceChildren = ns.getConnectedSources(sourceEvent).keySet();
		for(Node src: sourceChildren){
			if(src instanceof FeelingNodeImpl){
				FeelingNodeImpl feelingSrc = (FeelingNodeImpl)src;
				if(feelingSrc.isDrive()){
					Collection<Node> sinkChildren = ns.getConnectedSources(sinkEvent).keySet();
					for(Node snk: sinkChildren){
						if(snk.equals(feelingSrc)){
							double difference = ((FeelingNodeImpl)snk).getAffectiveValence()-feelingSrc.getAffectiveValence();
							PamLink isLink = addLink(incentiveSalienceLinkType, feelingSrc, sourceEvent, PerceptualAssociativeMemoryImpl.LATERAL_LINK_CATEGORY);
							isLink.reinforceBaseLevelActivation(learningRate*difference);
						}
					}
				}
			}
		}
	}
}