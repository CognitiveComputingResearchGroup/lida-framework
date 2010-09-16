/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author Tom
 */
public class Serializer {
    public static byte[] getBytes(Object obj) {
        byte[] serializedBytes = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
            ObjectOutput out = new ObjectOutputStream(bos) ;
            out.writeObject(obj);
            out.close();

            serializedBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return serializedBytes;
    }
}
