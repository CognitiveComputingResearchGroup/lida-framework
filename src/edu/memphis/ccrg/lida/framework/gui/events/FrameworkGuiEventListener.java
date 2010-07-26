/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.gui.events;

/**
 * An object that listens for FrameworkGuiEvents, that is, data being sent from the model (framework)
 * to the Gui
 * @author Javier Snaider
 */
public interface FrameworkGuiEventListener {
	
	/**
	 * Must be able to receive FrameworkGuiEvents  
	 * @param event
	 */
	public abstract void receiveFrameworkGuiEvent(FrameworkGuiEvent event);
	
}
