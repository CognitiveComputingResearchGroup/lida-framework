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
public class ProceduralMemoryDAO extends DataAccessObjectImpl {
    public static final String STORAGE_NAME = "proceduralmemory";
    public ProceduralMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        super(module, cStorage, STORAGE_NAME, cLidaId);
    }
}
