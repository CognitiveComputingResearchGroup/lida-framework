/**
 *
 */
package edu.memphis.ccrg.lida.framework.gui;

import javax.swing.JPanel;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.Module;

/**
 *  Lida Gui Panel
 * @author Javier Snaider
 *
 */
public interface LidaPanel {
	public void registrerLidaGuiController(LidaGuiController lgc);
	public void registrerLida(Lida lida);
	public void display (Object o);
	public void refresh();
	public JPanel getPanel();
    public Module moduleSuported();
    public void setName(String name);
    public String getName();
}

