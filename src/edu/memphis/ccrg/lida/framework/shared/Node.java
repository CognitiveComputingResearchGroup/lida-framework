/**
 * Node.java
 */
package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.pam.PamNode;
import java.io.Serializable;

/**
 * Node represents a Concept in LIDA. It could be implemented in different ways 
 * for different parts of the system. For example could be pamNodes in the PAM and WorkspaceNodes
 * in the workspace.
 * Nodes with the same id represents the same concept so equals have to return true even if the objects are
 * of different classes.
 * 
 * @author Javier Snaider
 * 
 */
public interface Node extends Linkable, Activatible, Serializable {
	
	public static final Node NULL_NODE = new NodeImpl();
		
	public double getImportance();
	public void setImportance(double importance);
	
    public PamNode getReferencedNode();
    public void setReferencedNode (PamNode n);
    
    public long getId();
    public void setId(long id);
    
    public String getLabel();
    public void setLabel(String label);
    
	public String getFactoryName();
	public void setFactoryName(String factoryName);

}//interface Node

