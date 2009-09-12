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
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

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
			              PerceptualAssociativeMemory pam, 
			              LidaTaskManager tm) {
		super(tm);
		pamNode = node;
		excitationAmount = activation;
		nodeStruct = pamNodeStructure;
		this.pam = pam;
	}

	protected void runThisLidaTask() {
		pamNode.excite(excitationAmount);
		pam.checkIfOverThreshold(pamNode);
		Set<PamNode> nodes = nodeStruct.getParents(pamNode);
		double newAmount = pamNode.getTotalActivation() * nodeStruct.getUpscale();
		results.add(nodesIndex, nodes);
		results.add(amountIndex, newAmount);
		this.setTaskStatus(LidaTask.FINISHED_WITH_RESULTS);
	}//method

	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	public List<Object> get() throws InterruptedException, ExecutionException {
		return results;
	}

	public List<Object> getResult(){
		return null;
	}

	public boolean isCancelled() {
		return false;
	}

	public boolean isDone() {
		return false;
	}

	public List<Object> get(long arg0, TimeUnit arg1)
			throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}
	public String toString(){
		return "ExitationTask-"+ getTaskId();
	}

}//class