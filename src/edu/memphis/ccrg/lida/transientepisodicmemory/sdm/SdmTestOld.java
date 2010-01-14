/*
 * @(#)SDMTest.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import static java.lang.Math.random;
import static java.lang.Math.round;

/**
 * This is a test of the episodic memory using an SDM implementation.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class SdmTestOld {

    /**
     * The main method of the class.
     * @param args command line arguments, not used
     */
    public static void main(String[] args) {
        
        final int NUM_HARD_LOCATIONS = 10000;
        final int ADDRESS_LENGTH = 1000;
        final int WORD_LENGTH = 1000;
        final int NUM_TESTS = 10;
        byte[][] addressMatrix = new byte [NUM_HARD_LOCATIONS][ADDRESS_LENGTH];
        double activationProbability = 0.7;
        int activationRadius = 425;
        int[] counterRange = new int[2];
        byte[] inputWord = new byte[WORD_LENGTH];
        byte[] outputWord = new byte[WORD_LENGTH];
        byte[] address = new byte[ADDRESS_LENGTH];

        
        // Initialize the address matrix with random hard locations.
        for (int i = 0; i != NUM_HARD_LOCATIONS; i++) {
            for (int j = 0; j != ADDRESS_LENGTH; j++) {
                addressMatrix[i][j] = (byte) round(random());
            }
        }
        System.out.println("Address matrix initialized.");
        counterRange[0] = -20;
        counterRange[1] = 20;

        SparseDistributedMemoryOld sdm
        = new SparseDistributedMemoryOld(addressMatrix,
            activationProbability,
            activationRadius,
            counterRange,
            WORD_LENGTH);

        for (int j = 1; j <= NUM_TESTS; j++) {
            System.out.println("\nTest " + j);

            // Create a random input word.
            System.out.print("Input word: ");
            for (int i = 0; i != WORD_LENGTH; i++) {
                inputWord[i] = (byte) round(random());
                System.out.print(inputWord[i]);
            }

            // Create a random address.
            System.out.print("\nAddress: ");
            for (int i = 0; i != ADDRESS_LENGTH; i++) {
                address[i] = (byte) round(random());
                System.out.print(address[i]);
            }
            sdm.store(inputWord, address);
            outputWord = sdm.retrieve(address);
            System.out.print("\nOutput word: ");
            for (int i = 0; i != WORD_LENGTH; i++) {
                System.out.print(outputWord[i]);
            }
        }
    }
}
