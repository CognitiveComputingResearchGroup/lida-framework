/*
 * @(#)SDMTest.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;

/**
 * This is a test of the episodic memory using an SDM implementation.
 * 
 * @author Javier Snaider
 */
public class QuickSdmTest {

	/**
	 * The main method of the class.
	 * 
	 * @param args
	 *            command line arguments, not used
	 */
	public static void main(String[] args) {

		final int NUM_HARD_LOCATIONS = 1000000;
		final int ADDRESS_LENGTH = 1000;
		final int WORD_LENGTH = 1000;
		final int NUM_TESTS = 50;
		final int NOISE = 300;
//		double activationProbability = 0.7;
		BitVector[] addressMatrix=new BitVector[NUM_HARD_LOCATIONS];
		int activationRadius = 451;
		int counterMax = 50;
		BitVector inputWord = null;
		BitVector outputWord = null;
		BitVector address = null;
		BitVector address1 = null;
		BitVector input1 = null;
		QuickSparseDistributedMemory sdm;
		BitVector[] testSet = new BitVector[NUM_TESTS];
		BitVector sumSpace = BitVectorUtils
				.getRandomVector(ADDRESS_LENGTH);
		BitVector elemSpace = BitVectorUtils
				.getRandomVector(ADDRESS_LENGTH);

		// Initialize the address matrix with random hard locations.
		for (int i = 0; i != NUM_HARD_LOCATIONS; i++) {
			addressMatrix[i] = BitVectorUtils
					.getRandomVector(ADDRESS_LENGTH);
		}
		System.out.println("Address matrix initialized.");

		// sdm = new SparseDistributedMemoryImp(
		// addressMatrix, activationRadius, counterMax, WORD_LENGTH);
		//
		// for (int j = 1; j <= NUM_TESTS; j++) {
		// System.out.println("\nTest " + j);
		//
		// // Create a random input word.
		// inputWord = getRandomVector(WORD_LENGTH);
		// System.out.println("Input word: " + inputWord);
		// // Create a random address.
		// address = getRandomVector(ADDRESS_LENGTH);
		// System.out.println("Address: " + address);
		//
		// if (j == 1) {
		// address1 = address.copy();
		// input1 = inputWord.copy();
		// }
		// sdm.store(inputWord, address);
		// outputWord = sdm.retrieve(address);
		// System.out.println("Output word: " + outputWord);
		// inputWord.xor(outputWord);
		// System.out.println("Dif: " + inputWord);
		// }
		// outputWord = sdm.retrieve(address1);
		// System.out.println("Input word1: " + input1);
		// System.out.println("Output word1: " + outputWord);
		// input1.xor(outputWord);
		// System.out.println("Dif: " + input1);

		sdm = new QuickSparseDistributedMemory(addressMatrix, activationRadius,counterMax,
				WORD_LENGTH);

		for (int j = 0; j < NUM_TESTS; j++) {
			System.out.println("\nTest " + (j + 1));

			// Create a random input word.
			inputWord = BitVectorUtils.getRandomVector(WORD_LENGTH);
			System.out.println("Input word: " + inputWord);
			// Create a random address.
			testSet[j] = inputWord.copy();

			sdm.mappedStore(inputWord, elemSpace);

			BitVector address2 = BitVectorUtils.noisyVector(inputWord,
					NOISE);
			System.out.println("Output noise address: " + address2);
			outputWord = sdm.retrieveIterating(address2, elemSpace);
			System.out.println("Output word unmapped: " + outputWord);
			inputWord.xor(outputWord);
			System.out.println("Dif: " + inputWord);
		}

		input1 = testSet[0].copy();
		BitVector address2 = BitVectorUtils.noisyVector(input1, NOISE);
		outputWord = sdm.retrieveIterating(address2, elemSpace);
		System.out.println("Input word1: " + testSet[0]);
		System.out.println("Output word1: " + outputWord);
		input1.xor(outputWord);
		System.out.println("Dif: " + input1);

		int[] sum = new int[inputWord.size()];
		for (int j = 0; j < 3; j++) {
			inputWord = testSet[j].copy();
			System.out.println("Input word: " + inputWord);

			BitVectorUtils.sumVectors(sum, inputWord);
		}

		BitVector sumVector = BitVectorUtils.normalizeVector(sum);

		sdm.mappedStore(sumVector, sumSpace);
		address2 = BitVectorUtils.noisyVector(sumVector, NOISE);
		outputWord = sdm.retrieveIterating(address2, sumSpace);
		System.out.println("Output Sum unmapped: " + outputWord);

		address1 = outputWord.copy();
		address1.xor(sumVector);

		System.out.println("Dif: " + address1);

		address1 = outputWord.copy();
		for (int i = 0; i < 3; i++) {

			BitVector outSum = sdm.retrieveIterating(address1, elemSpace);
			System.out.println("Output Sum element: " + outSum);
			for (int j = 0; j < 3; j++) {
				inputWord = testSet[j].copy();
				inputWord.xor(outSum);
				System.out.println("Dif " + j + ":" + inputWord);
				if (inputWord.cardinality() == 0) {
					address2 = BitVectorUtils.substractVectors(
							address2, testSet[j]);
					address1 = address2.copy();
				}
			}
		}
	}

}
