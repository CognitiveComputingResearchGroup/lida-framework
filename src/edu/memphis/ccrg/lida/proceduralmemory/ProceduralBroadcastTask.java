package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;

public class ProceduralBroadcastTask extends LidaTaskImpl implements LidaTask {

	private ProceduralMemory pm;
	
	public ProceduralBroadcastTask(ProceduralMemory pm) {
		this.pm = pm;
	}

	@Override
	protected void runThisLidaTask() {
		pm.activateSchemes();
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}

}
