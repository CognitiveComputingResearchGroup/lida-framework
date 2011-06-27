/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is intended for use by Initializers during initialization only.
 * It allows Initializers to access the same attributes. Attributes can be
 * added by an Initializer and retrieved later by any other Initializer
 * @author Javier Snaider
 * @author Ryan McCall
 *
 */
public class GlobalInitializer {

    /*
     * Sole instance of this class that will be used.
     */
    private static final GlobalInitializer instance = new GlobalInitializer();
    private static final Map<String, Object> globalAttributes = new HashMap<String, Object>();

    /**
     * Returns the sole instance of this class. Implements the Singleton
     * pattern.
     *
     * @return instance sole instance of this class
     */
    public static GlobalInitializer getInstance() {
        return instance;
    }

    /*
     * Creates the initializer
     */
    private GlobalInitializer() {
    }

    public Object getAttribute(String key) {
        return globalAttributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        globalAttributes.put(key, value);
    }

    public Object removeAttribute(String key) {
        return globalAttributes.remove(key);
    }

    public void clearAttributes() {
        globalAttributes.clear();
    }
}
