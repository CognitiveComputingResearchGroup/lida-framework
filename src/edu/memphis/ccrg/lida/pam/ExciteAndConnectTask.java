package edu.memphis.ccrg.lida.pam;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkType;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class ExciteAndConnectTask extends LidaTaskImpl {

	/**
	 * PamNode to be excited
	 */
	private PamNode source, sink;
	
	private Link link;
	
	/**
	 * Amount to excite
	 */
	private double excitationAmount;
	
	/**
	 * Used to make another excitation call
	 */
	private PerceptualAssociativeMemory pam;
	
	/**
	 * For threshold task creation
	 */
	private TaskSpawner taskSpawner;

	/**
	 * 
	 * @param source
	 * @param excitation
	 * @param pam
	 * @param ts
	 */
	public ExciteAndConnectTask(PamNode source, PamNode sink, Link l, 
								double excitation,
			              		PerceptualAssociativeMemory pam, 
			              		TaskSpawner ts) {
		super();
		this.source = source;
		this.sink = sink;
		this.link = l;
		excitationAmount = excitation;
		this.pam = pam;
		taskSpawner = ts;
	}

	/**
	 * 
	 */
	protected void runThisLidaTask() {
		//System.out.println("Exciting " + pamNode.getLabel());
		source.excite(excitationAmount);
		sink.excite(excitationAmount);
		if(source.isOverThreshold() && sink.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			NodeStructure ns = new NodeStructureImpl();
			ns.addNode(source);
			ns.addNode(sink);
			ns.addLink(source.getIds(), sink.getIds(), LinkType.GROUNDING, 1.0);
			AddToPerceptTask task = new AddToPerceptTask(ns, pam);
			taskSpawner.addTask(task);
		}
		//Tell PAM to propagate the activation of pamNode to its parents
		pam.sendActivationToParentsOf(sink);
		//TODO: don't think I should excite source actually!
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}//method

	/**
	 * 
	 */
	public String toString(){
		return "Excitation " + getTaskId();
	}
}
