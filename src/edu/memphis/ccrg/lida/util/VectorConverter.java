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
    // Returns a bitset containing the values in bytes.
    // The byte-ordering of bytes must be big-endian which means the most significant bit is in element 0.
    public static BitVector fromByteArray(byte[] bytes) {
        BitVector bv = new BitVector(bytes.length*8);
        for (int i=0; i<bytes.length*8; i++) {
            if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
                bv.set(i);
            }
        }
        return bv;
    }

    // Returns a byte array of at least length 1.
    // The most significant bit in the result is guaranteed not to be a 1
    // (since BitSet does not support sign extension).
    // The byte-ordering of the result is big-endian which means the most significant bit is in element 0.
    // The bit at index 0 of the bit set is assumed to be the least significant bit.
    public static byte[] toByteArray(BitVector bv) {
        long[] bits = bv.elements();
        byte[] bytes = new byte[bits.length/8+1];
        for (int i=0; i<bits.length; i++) {
            if (bits[i] > 0) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }

}
