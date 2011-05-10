package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;

/**
 * A {@link Learnable} {@link Linkable}, a {@link PamNode} or 
 * {@link PamLink}
 * @author Ryan J. McCall
 * @see PamNode
 * @see PamLink
 * @see PerceptualAssociativeMemory
 */
public interface PamLinkable extends Linkable, Learnable{

}
