package edu.memphis.ccrg.lida.framework;

/**
 * A GuiContentProvider is a class that provides content
 * for FrameworkGui's.   GUIs listen to providers using Observer pattern.
 * Examples include PAMImpl.java and PerceptualBufferImpl.java
 *
 * @author ryanjmccall
 *
 */
public interface GuiContentProvider {

	public abstract void sendGuiContent();
	public abstract void addFrameworkGui(FrameworkGui listener);

}
