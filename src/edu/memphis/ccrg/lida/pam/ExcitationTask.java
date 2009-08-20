package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;

public class ExcitationTask extends LidaTaskImpl implements Future<List<Object>> {
	
	public static int nodesIndex = 0;
	public static int amountIndex = 1;
	private PamNode pamNode;
	private double excitationAmount;
	private PamNodeStructure nodeStruct;
	private PerceptualAssociativeMemory pam;
	private List<Object> results = new ArrayList<Object>();

	public ExcitationTask(PamNode node, double activation,
			              PamNodeStructure pamNodeStructure, 
			              PerceptualAssociativeMemory pam) {
		pamNode = node;
		excitationAmount = activation;
		nodeStruct = pamNodeStructure;
		this.pam = pam;
	}

	public void run() {
		pamNode.excite(excitationAmount);
		if(pamNode.isOverThreshold())
			pam.addToPercept(pamNode);
		Set<PamNode> nodes = nodeStruct.getParents(pamNode);
		double newAmount = excitationAmount * nodeStruct.getUpscale();
		results.add(nodesIndex, nodes);
		results.add(amountIndex, newAmount);
		this.setTaskStatus(LidaTask.FINISHED);
	}//method

	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	public List<Object> get() throws InterruptedException, ExecutionException {
		return results;
	}

	public List<Object> get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return null;
	}

	public boolean isCancelled() {
		return false;
	}

	public boolean isDone() {
		return false;
	}

}//class