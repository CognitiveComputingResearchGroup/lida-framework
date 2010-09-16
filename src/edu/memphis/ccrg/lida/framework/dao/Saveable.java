/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import java.util.Map;

/**
 *
 * @author Tom
 */
public interface Saveable {
    public Object getState();
    public boolean setState(Object content);
}
