package edu.memphis.ccrg.lida.pam;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;

public class PamDriver extends ModuleDriverImpl{

	private PerceptualAssociativeMemory pam;
	//private Logger logger = Logger.getLogger("lida.pam.PamDriver");
	
	public PamDriver(PerceptualAssociativeMemory pam, int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm, LidaTaskNames.PAM_DRIVER);
		this.pam = pam;
	}//constructor
	
	@Override
	protected void runThisDriver() {
		if (pam instanceof PerceptualAssociativeMemoryImpl){
			((PerceptualAssociativeMemoryImpl)pam).sendEvent();
		}
		pam.decayPam();  //Decay the activations	
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void processResults(LidaTask task) {
//		System.out.println(isTasksPaused() + " in pam driver");
		try {
			List<Object> results = (List<Object>) ((Future) task).get();
			Set<PamNode> nodes = (Set<PamNode>) results.get(ExcitationTask.nodesIndex);
//			for(PamNode n: nodes)
//				System.out.println(n.getLabel());
//			System.out.println();
			Double amount = (Double) results.get(ExcitationTask.amountIndex);
			pam.receiveActivationBurst(nodes, amount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}//method

}//class 