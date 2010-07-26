package edu.memphis.ccrg.lida.framework.gui.events;

/**
 * A GuiContentProvider is a class that provides content
 * to a GUI.  The GUIs listen to providers, following Observer pattern.
 * Example content providers are PAMImpl.java and PerceptualBufferImpl.java
 *
 * @author Ryan J McCall
 *
 */
public interface GuiEventProvider {
	
	/**
	 * Must be able to register FrameworkGuiEvent listeners
	 * 
	 * @param listener
	 */
	public abstract void addFrameworkGuiEventListener(FrameworkGuiEventListener listener);

	/**
	 * A Gui Event provider may want to send different kinds of events at different
	 * times so it should implement and use this method to do so.
	 * 
	 * @param evt
	 */
	public abstract void sendEventToGui(FrameworkGuiEvent evt);
}
