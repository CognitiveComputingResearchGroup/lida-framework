/**
 * PAMinterface.java
 */

package edu.memphis.ccrg.lida.perception;
import java.util.Set;
import java.util.Map;

import edu.memphis.ccrg.lida.perception.Node;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemory;
import edu.memphis.ccrg.lida.util.DecayCurve;

//

/**
 * @author Ryan McCall
 */
public interface PAMInterface{
	
	/**
	 * Adds the specified nodes and/or links to the PAM
	 * Each nodes added must be register which requires
	 * the refresh and buildLayerMap operations (see PAM.java) 
	 */
	public void addToPAM(Set<Node> nodes, Set<Link> links);
	
	public Set<Node> getNodes();	
	public LinkMap getLinks();
	
	/**
	 * Updates PAM's parameters from the supplied map
	 */
	public void setParameters(Map<String, Object> parameters);
	
	/**
	 * Sense the current SenseContent
	 */ 
	public void sense();
	
	public void setExciteBehavior(ExciteBehavior behavior);
	
	/**
	 * Passes activation in a feed-forward direction. 
	 * After activation is passed nodes must synchronize() their total activation. 
	 */
	public void passActivation();
	
	public void decay();
	public void setDecayCurve(DecayCurve c);

}//interface PAMinterface