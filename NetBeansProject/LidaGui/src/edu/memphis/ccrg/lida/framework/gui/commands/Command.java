/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.gui.commands;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 *
 * @author Javier Snaider
 */
public interface Command {
    public void setParameter(String name, Object value);
    public Object getParameter(String name);
    public Object getResult();
    public void execute(Lida lida);
    public void setParameters(Map<String, Object> parameters);
}
