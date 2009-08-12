/**
 * Node.java
 */
package edu.memphis.ccrg.lida.shared;

import java.util.Map;

import edu.memphis.ccrg.lida.pam.PamNode;

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
public interface Node extends Linkable, Activatible{
	
	//Standard for node
	public void setValue(Map<String, Object> values);
	public double getImportance(); 
    public PamNode getReferencedNode();
    public void setReferencedNode (PamNode n);
    public long getId();
    public void setId(long id);
    public void setLabel(String label);

   
}//interface Node

