/**
 *
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import javax.swing.JPanel;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;

/**
 *  Lida Gui Panel
 * @author Javier Snaider
 *
 */
public interface LidaPanel {
	public void registrerLidaGuiController(LidaGuiController lgc);
	public void registerLida(Lida lida);
	public void display (Object o);
	public void refresh();
	public JPanel getPanel();
    public ModuleType getSupportedModule();
    public void setSupportedModule(ModuleType module);
    public void setName(String name);
    public String getName();
    public void initPanel(String[] param);
}

