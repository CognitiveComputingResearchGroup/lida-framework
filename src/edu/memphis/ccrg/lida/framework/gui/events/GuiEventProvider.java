package edu.memphis.ccrg.lida.framework.gui.events;

/**
 * A GuiContentProvider is a class that provides content
 * for FrameworkGui's.   GUIs listen to providers using Observer pattern.
 * Examples include PAMImpl.java and PerceptualBufferImpl.java
 *
 * @author ryanjmccall
 *
 */
public interface GuiEventProvider {
	
	public abstract void addFrameworkGuiEventListener(FrameworkGuiEventListener listener);

	/**
	 * A Gui Event provider may want to send different kinds of events at different
	 * times, so the event to be sent is passed as a parameter.
	 * @param evt
	 */
	public abstract void sendEvent(FrameworkGuiEvent evt);
}
