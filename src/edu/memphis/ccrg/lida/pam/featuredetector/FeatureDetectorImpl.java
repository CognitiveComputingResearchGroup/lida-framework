package edu.memphis.ccrg.lida.pam.featuredetector;


import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This class implements the FeatureDetector interface and provides 
 * hook default methods. 
 * Users should extend this class and overwrite the detect() and burstPam() methods.
 * A convenience init() method is added to initialize the class. This method can be 
 * overwritten as well.
 * This implementation is oriented to detect features from sensoryMemory, but the implementation 
 * can be used to detect and burstActivation from other modules, like Workspace, emotions or internal states.
 * 
 * @author Ryan McCall - Javier Snaider
 *
 */
public class FeatureDetectorImpl extends LidaTaskImpl implements FeatureDetector {

	private Logger logger = Logger.getLogger("lida.pam.featuredetector.FeatureDetectorImpl");
	private PamNode pamNode;
	private PerceptualAssociativeMemory pam;
	protected SensoryMemory sm;
	public FeatureDetectorImpl(PamNode n, SensoryMemory sm,
							   PerceptualAssociativeMemory pam, 
							   LidaTaskManager tm) {
		super(tm, LidaTaskNames.FEATURE_DETECTOR);
		this.pam = pam;
		this.sm = sm;
		this.pamNode = n;
	}
	public FeatureDetectorImpl(){	
		super(null, null);
	}
	
	public void setPamNode(PamNode node) {
		pamNode = (PamNodeImpl) node;
	}
	public PamNode getPamNode() {
		return pamNode;
	}
	
	protected void runThisLidaTask(){
		double amount = detect();
		logger.log(Level.INFO,"detection performed:{0}",amount);
		if (amount>0.0){
			logger.log(Level.INFO,"Pam excited:{0}",amount);			
			excitePam(amount);
		}
	}

	//Override this method for domain-specific feature detector impl's
	public double detect() {
		return 0.0;
	}
	public void excitePam(double amount){
		pam.receiveActivationBurst(pamNode, amount);
	}
	
	public void init(){
		pam=(PerceptualAssociativeMemory)getParameter("PAM");
		sm=(SensoryMemory)getParameter("SensoryMemory");
		pamNode=(PamNode)getParameter("PamNode");
	}
}// class