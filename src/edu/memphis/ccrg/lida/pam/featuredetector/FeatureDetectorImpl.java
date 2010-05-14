package edu.memphis.ccrg.lida.pam.featuredetector;


import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This class implements the FeatureDetector interface and provides 
 * hook default methods. 
 * Users should extend this class and overwrite the detect() and excitePam() methods.
 * A convenience init() method is added to initialize the class. This method can be 
 * overwritten as well.
 * This implementation is oriented to detect features from sensoryMemory, but the implementation 
 * can be used to detect and burstActivation from other modules, like Workspace, emotions or internal states.
 * 
 * @author Ryan McCall - Javier Snaider
 *
 */
public abstract class FeatureDetectorImpl extends LidaTaskImpl implements FeatureDetector {

	private static Logger logger = Logger.getLogger("lida.pam.featuredetector.FeatureDetectorImpl");
	private PamNode pamNode;
	private PerceptualAssociativeMemory pam;
	protected SensoryMemory sm;
	public FeatureDetectorImpl(PamNode n, SensoryMemory sm,
							   PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		this.sm = sm;
		this.pamNode = n;
	}
	
	public void init(){
		pam = (PerceptualAssociativeMemory)getParam("PAM",null);
		sm = (SensoryMemory)getParam("SensoryMemory",null);
		pamNode = (PamNode)getParam("PamNode",null);
	}

	public void setPamNode(PamNode node) {
		pamNode = (PamNodeImpl) node;
	}
	public PamNode getPamNode() {
		return pamNode;
	}
	
	protected void runThisLidaTask(){
		double amount = detect();
		logger.log(Level.FINE,"detection performed "+ toString()+": " +amount,LidaTaskManager.getActualTick());
		if (amount > 0.0){
			logger.log(Level.FINE,"Pam excited: "+amount,LidaTaskManager.getActualTick());			
			excitePam(amount);
		}
	}//method

	/**
	 * Override this method for domain-specific feature detection
	 */
	public abstract double detect();
	
	public void setParam(String name, Object value){
		
	}
	
	public void excitePam(double amount){
		pam.receiveActivationBurst(pamNode, amount);
	}
	
	public String toString(){
		return "Feature Detector ["+getTaskId()+"] ";
	}

}// class