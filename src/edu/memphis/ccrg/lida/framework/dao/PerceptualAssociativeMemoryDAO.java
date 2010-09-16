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
public class PerceptualAssociativeMemoryDAO extends DataAccessObjectImpl {
    public static final String STORAGE_NAME = "perceptualassociativememory";
    public PerceptualAssociativeMemoryDAO(LidaModule module, Storage cStorage, int cLidaId) {
        super(module, cStorage, STORAGE_NAME, cLidaId);
    }
}
