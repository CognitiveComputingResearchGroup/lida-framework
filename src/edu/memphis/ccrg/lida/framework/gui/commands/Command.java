/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.gui.commands;

/**
 *
 * @author Javier
 */
public interface Command {
    public void setParameter(String name, Object value);
    public Object getParameter(String name);
    public Object getResult();
    public void execute();
}
