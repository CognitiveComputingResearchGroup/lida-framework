/**
 * PAMinterface.java
 */
package edu.memphis.ccrg.lida.perception;

import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.perception.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory{
	
	/**
	 * Updates PAM's parameters from the supplied map
	 */
	public void setParameters(Map<String, Object> parameters);
	
	/**
	 * Changes how the nodes in this PAM are excited.
	 * 
	 * @param behavior
	 */
	public void setExciteBehavior(ExciteBehavior behavior);
	public void setDecayBehavior(DecayBehavior c);
	
	/**
	 * Adds the specified nodes and/or links to the PAM
	 * Each nodes added must be register which requires
	 * the refresh and buildLayerMap operations (see PAM.java) 
	 */
	public void addToPAM(Set<PamNode> nodes, List<FeatureDetector> ftDetectors, Set<Link> links);
	
	/**
	 * Sense the current SenseContent
	 */ 
	public void detectSensoryMemoryContent();
	
	/**
	 * Passes activation in a feed-forward direction. 
	 * After activation is passed nodes must synchronize() their total activation. 
	 */
	public void propogateActivation();
	
	/**
	 * Send Percept to PAM Listeners
	 */
	public void sendOutPercept();	
	
	/**
	 * Decay activations of PAM nodes and/or links
	 */
	public void decayPAM();
	
	public void receiveActivationBurst(PamNode pNode,double activation);
	public PamNode getPamNode(long id);
	public void addPAMListener(PAMListener pl);

}//interface PAMinterface