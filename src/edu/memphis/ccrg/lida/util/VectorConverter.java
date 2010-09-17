/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.util;

import cern.colt.bitvector.BitVector;

/**
 *
 * @author Tom
 */
public class VectorConverter {
    public static BitVector fromByteArray(byte[] bytes) {
        int size = bytes.length * 8;
        BitVector bv = new BitVector(size);
        for (int i=0; i<size; i++) {
            if ((bytes[i/8] & (1<<(7-i%8))) > 0)
                bv.set(i);
            else
                bv.clear(i);
        }
        return bv;
    }

    public static byte[] toByteArray(BitVector bv) {
        byte[] bytes = new byte[bv.size()/8+1];
        for (int i=0; i<bv.size(); i++) {
            if (bv.get(i)) {
                bytes[i/8] |= 1<<(7-i%8);
            }
        }
        return bytes;
    }

}
