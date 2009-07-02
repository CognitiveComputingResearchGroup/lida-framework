/*
 * @(#)SDMTest.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory.sdm;

/**
 * This is a test of the episodic memory using an SDM implementation.
 * @author rsilva
 */
public class SDMTest {

    /**
     * The main method of the class.
     * @param args command line arguments, not used
     */
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
