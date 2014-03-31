package edu.memphis.ccrg.lida.motivation.pam;

import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;

/**
 * Task for the propagation of current incentive salience, originating from a drive feeling nodes, along an incentive salience link.
 * The key key difference with this link is that it transmits current incentive salience to its sink and adds a valence to the
 * excitation amount;
 * @author Ryan J McCall
 */
public class IncentiveSaliencePropagationTask extends PropagationTask {
	
	/**
	 * @param tpr ticks per run
	 * @param lnk a {@link FeelingPamLinkImpl}
	 * @param a an excitation amount
	 * @param pam the {@link PerceptualAssociativeMemory} module.
	 */
	public IncentiveSaliencePropagationTask(int tpr,FeelingPamLinkImpl lnk, 
											double a,PerceptualAssociativeMemory pam) {
		super(tpr,lnk,a,pam);
	}

	@Override
	protected void runThisFrameworkTask() {
		link.exciteActivation(excitationAmount);		
		//
		FeelingPamLinkImpl fLink = (FeelingPamLinkImpl) link;
		double propagatedIncentiveSalience = excitationAmount*fLink.getValenceSign()*fLink.getBaseLevelActivation();
		sink.exciteIncentiveSalience(propagatedIncentiveSalience);
		//
		runPostExcitation();
	}
}