/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.framework.dao;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 *
 * @author Tom
 */
public interface DataAccessObject {
    public boolean save();
    public boolean load();
    public boolean load(int lidaId);
    public boolean delete();
}
