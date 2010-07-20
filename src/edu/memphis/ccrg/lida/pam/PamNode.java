package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.Node;

/**
 * A PamNode extends nodes.  The added functionalities are mainly due to the fact that
 * PamNodes are involved in activation passing where Nodes are not. 
 * @author Ryan J McCall, Javier Snaider
 *
 */
public interface PamNode extends Node, LearnableActivatible, PamLinkable{


}// interface