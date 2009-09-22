/**
 *
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import javax.swing.JPanel;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

/**
 *  Lida Gui Panel
 * @author Javier Snaider
 *
 */
public interface LidaPanel extends FrameworkGuiEventListener {
	public void registrerLidaGuiController(LidaGuiController lgc);
	public void registerLida(Lida lida);
	public void display (Object o);
	public void refresh();
	public JPanel getPanel();
    public Module getSupportedModule();
    public void setSupportedModule(Module module);
    public void setName(String name);
    public String getName();
}

