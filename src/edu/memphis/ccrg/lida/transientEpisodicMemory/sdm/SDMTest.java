/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory.sdm;

/**
 *
 * @author rsilva
 */
public class SDMTest {

    public static void main(String[] args) {
        byte[][] addressMatrix = new byte [2][2];
        double activationProbability = 0.7;
        int activationRadius = 0;
        int[] counterRange = new int[2];
        int wordSize = 2;
        byte[] inputWord = new byte[2];
        byte[] address = new byte[2];
        addressMatrix[0][0] = 0;
        addressMatrix[0][1] = 1;
        addressMatrix[1][0] = 1;
        addressMatrix[1][1] = 0;
        counterRange[0] = -1;
        counterRange[1] = 1;
        inputWord[0] = 0;
        inputWord[1] = 1;
        address[0] = 0;
        address[1] = 1;
        SparseDistributedMemory sdm
                = new SparseDistributedMemory(addressMatrix,
                    activationProbability,
                    activationRadius,
                    counterRange,
                    wordSize);
        sdm.store(inputWord, address);
    }
}
