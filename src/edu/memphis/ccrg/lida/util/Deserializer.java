/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author Tom
 */
public class Deserializer {
    public static Object getObject(byte[] byteArray) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            Object obj = in.readObject();
            in.close();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
