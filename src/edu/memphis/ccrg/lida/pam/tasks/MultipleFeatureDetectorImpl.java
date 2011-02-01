package edu.memphis.ccrg.lida.pam.tasks;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This class implements the FeatureDetector interface and provides default methods. 
 * Users should extend this class and overwrite the detect() and excitePam() methods.
 * A convenience init() method is added to initialize the class. This method can be 
 * overwritten as well.
 * This implementation is oriented to detect features from sensoryMemory, but the implementation 
 * can be used to detect and burstActivation from other modules, like Workspace, emotions or internal states.
 * 
 * @author Ryan J. McCall
 *
 * 
 */
public abstract class MultipleFeatureDetectorImpl extends LidaTaskImpl implements FeatureDetector {

	private static final Logger logger = Logger.getLogger(MultipleFeatureDetectorImpl.class.getCanonicalName());
	protected Map<String, PamNode> pamNodeMap;
	protected PerceptualAssociativeMemory pam;
	protected SensoryMemory sm;
	
	/**
	 * @param nodeMap Nodes indexed by their label
	 * @param sm {@link SensoryMemory}
	 * @param pam {@link PerceptualAssociativeMemory}
	 */
	public MultipleFeatureDetectorImpl(Map<String, PamNode> nodeMap, SensoryMemory sm,
	   		   						   PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		this.sm = sm;
		this.pamNodeMap = nodeMap;
	}
	
	@Override
	protected void runThisLidaTask(){
		detectMultipleNodes();
		logger.log(Level.FINE,"detection performed "+ toString(),LidaTaskManager.getCurrentTick());
	}

	/**
	 * Override this method for domain-specific feature detection
	 */
	public abstract void detectMultipleNodes();
	
	@Override
	public String toString(){
		return "Feature Detector ["+getTaskId()+"] ";
	}

}
