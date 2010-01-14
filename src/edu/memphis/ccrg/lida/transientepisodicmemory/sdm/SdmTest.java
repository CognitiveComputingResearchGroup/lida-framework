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
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class SdmTest {

	/**
	 * The main method of the class.
	 * 
	 * @param args
	 *            command line arguments, not used
	 */
	public static void main(String[] args) {

		final int NUM_HARD_LOCATIONS = 100000;
		final int ADDRESS_LENGTH = 1000;
		final int WORD_LENGTH = 1000;
		final int NUM_TESTS = 10;
		final int NOISE = 300;
		BitVector[] addressMatrix = new BitVector[NUM_HARD_LOCATIONS];
		double activationProbability = 0.7;
		int activationRadius = 451;
		int counterMax = 50;
		BitVector inputWord = null;
		BitVector outputWord = null;
		BitVector address = null;
		BitVector address1 = null;
		BitVector input1 = null;
		SparseDistributedMemory sdm;
		BitVector[] testSet = new BitVector[NUM_TESTS];
		BitVector sumSpace = SparseDistributedMemory
				.getRandomVector(ADDRESS_LENGTH);
		BitVector elemSpace = SparseDistributedMemory
				.getRandomVector(ADDRESS_LENGTH);

		// Initialize the address matrix with random hard locations.
		for (int i = 0; i != NUM_HARD_LOCATIONS; i++) {
			addressMatrix[i] = SparseDistributedMemory
					.getRandomVector(ADDRESS_LENGTH);
		}
		System.out.println("Address matrix initialized.");

		// sdm = new SparseDistributedMemory(
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

		sdm = new SparseDistributedMemory(addressMatrix, activationRadius,
				counterMax, WORD_LENGTH);

		for (int j = 0; j < NUM_TESTS; j++) {
			System.out.println("\nTest " + (j + 1));

			// Create a random input word.
			inputWord = SparseDistributedMemory.getRandomVector(WORD_LENGTH);
			System.out.println("Input word: " + inputWord);
			// Create a random address.
			testSet[j] = inputWord.copy();
			inputWord.xor(elemSpace);
			address = inputWord.copy();
			System.out.println("Address: " + address);

			sdm.store(inputWord, address);
			BitVector address2 = SparseDistributedMemory.noisyVector(address,
					NOISE);
			outputWord = sdm.retrieveIterating(address2);
			System.out.println("Output noise address: " + address2);
			System.out.println("Output word: " + outputWord);
			inputWord.xor(outputWord);
			address2.xor(outputWord);
			System.out.println("Dif1: " + address2);
			System.out.println("Dif: " + inputWord);
			outputWord.xor(elemSpace);
			System.out.println("Output word unmapped: " + outputWord);
			outputWord.xor(testSet[j]);

			System.out.println("Dif unmapped: " + outputWord);
		}

		input1 = testSet[0].copy();
		BitVector address2 = SparseDistributedMemory.noisyVector(input1, NOISE);
		address2.xor(elemSpace);
		outputWord = sdm.retrieveIterating(address2);
		input1 = testSet[0].copy();
		System.out.println("Input word1: " + testSet[0]);
		System.out.println("Output word1: " + outputWord);
		input1.xor(outputWord);
		address2.xor(outputWord);
		System.out.println("Dif1: " + address2);
		System.out.println("Dif: " + input1);

		int[] sum = new int[inputWord.size()];
		for (int j = 0; j < 5; j++) {
			inputWord = testSet[j].copy();
			System.out.println("Input word: " + inputWord);

			SparseDistributedMemory.sumVectors(sum, inputWord);

			// inputWord.xor(elemSpace);
			// System.out.println("mapped word: " + inputWord);
			// // Create a random address.
			// address = inputWord.copy();
			//
			// sdm.store(inputWord, address);
			// address2 = SparseDistributedMemory.noisyVector(address, NOISE);
			// outputWord = sdm.retrieveIterating(address2);
			// System.out.println("Output noise address: " + address2);
			// System.out.println("Output word: " + outputWord);
			// inputWord.xor(outputWord);
			// System.out.println("Dif: " + inputWord);
			// outputWord.xor(elemSpace);
			// System.out.println("Output word unmapped: " + outputWord);
			// outputWord.xor(testSet[j]);
			//
			// System.out.println("Dif unmapped: " + inputWord);
		}
		BitVector sumVector = SparseDistributedMemory.normalizeVector(sum);
		inputWord = sumVector.copy();
		inputWord.xor(sumSpace);
		sdm.store(inputWord, inputWord);
		address2 = SparseDistributedMemory.noisyVector(sumVector, NOISE);
		address2.xor(sumSpace);
		outputWord = sdm.retrieveIterating(address2);
		outputWord.xor(sumSpace);
		System.out.println("Output Sum unmapped: " + outputWord);

		address1 = outputWord.copy();
		address1.xor(sumVector);
		System.out.println("Dif: " + address1);

		address1 = outputWord.copy();
		address2 = outputWord.copy();
		address1.xor(elemSpace);
		for (int i = 0; i < 5; i++) {

		BitVector outSum = sdm.retrieveIterating(address1);
		System.out.println("Output Sum element: " + outSum);
		BitVector out2 = outSum.copy();
			out2.xor(elemSpace);
			System.out.println("Output Sum element unmapped: " + out2);
			for (int j = 0; j < 5; j++) {
				inputWord = testSet[j].copy();
				inputWord.xor(out2);
				System.out.println("Dif " + j + ":" + inputWord);
				if (inputWord.cardinality()==0){
					address2=SparseDistributedMemory.substractVectors(address2, testSet[j]);
					address1=address2.copy();
				}
			}
		}
	}

}
