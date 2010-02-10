/*
 * @(#)SDMTest.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import java.util.Arrays;

import cern.colt.bitvector.BitVector;

/**
 * This is a test of the episodic memory using an SDM implementation.
 * 
 * @author Javier Snaider
 */
public class SdmTest {

	static final int NUM_HARD_LOCATIONS = 100000;
	static final int ADDRESS_LENGTH = 1000;
	static final int WORD_LENGTH = 1000;
	static final int NUM_TESTS = 300;
	static final int NOISE = 200;
	static final int SUM_OP = 3;
	static final int SUM_WEIGHT = 3;

	/**
	 * The main method of the class.
	 * 
	 * @param args
	 *            command line arguments, not used
	 */
	public static void main(String[] args) {

		// double activationProbability = 0.7;
		int activationRadius = 451;
		int counterMax = 50;
		BitVector inputWord = null;
		BitVector outputWord = null;
		BitVector address = null;
		BitVector address1 = null;
		BitVector input1 = null;
		SparseDistributedMemory sdm;
		BitVector[] testSet = new BitVector[NUM_TESTS];
		BitVector sumSpace = BitVectorUtils.getRandomVector(ADDRESS_LENGTH);
		BitVector elemSpace = BitVectorUtils.getRandomVector(ADDRESS_LENGTH);
		BitVector sumOpSpace = BitVectorUtils.getRandomVector(ADDRESS_LENGTH);

		BitVector[] sumOperandsMapping = new BitVector[SUM_OP];
		BitVector[] sumWeights = new BitVector[SUM_WEIGHT];

		for (int i = 0; i < sumOperandsMapping.length; i++) {
			sumOperandsMapping[i] = BitVectorUtils
					.getRandomVector(ADDRESS_LENGTH);
		}

		for (int i = 0; i < sumWeights.length; i++) {
			sumWeights[i] = BitVectorUtils.getRandomVector(ADDRESS_LENGTH);
		}

		sdm = new SparseDistributedMemoryImp(NUM_HARD_LOCATIONS,
				activationRadius, WORD_LENGTH);

		for (int j = 0; j < NUM_TESTS; j++) {
			System.out.println("\nTest " + (j + 1));

			// Create a random input word.
			inputWord = BitVectorUtils.getRandomVector(WORD_LENGTH);
			System.out.println("Input word: " + inputWord);
			// Create a random address.
			testSet[j] = inputWord.copy();

			sdm.mappedStore(inputWord, elemSpace);

			BitVector address2 = BitVectorUtils.noisyVector(inputWord, NOISE);
			System.out.println("Output noise address: " + address2);
			outputWord = sdm.retrieveIterating(address2, elemSpace);
			System.out.println("Output word unmapped: " + outputWord);
			inputWord.xor(outputWord);
			System.out.println("Dif: " + inputWord);
		}
/*		System.out.println("\nRetrieving the first 10 inputs");

		for (int j = 0; j < 10; j++) {
			input1 = testSet[j].copy();
			BitVector address2 = BitVectorUtils.noisyVector(input1, NOISE);
			outputWord = sdm.retrieveIterating(address2, elemSpace);
			System.out.println("Input word1: " + testSet[j]);
			System.out.println("Output word1: " + outputWord);
			input1.xor(outputWord);
			System.out.println("Dif: " + input1);
		}
*/
		int[] sum = new int[inputWord.size()];
		for (int j = 0; j < SUM_OP; j++) {
			inputWord = testSet[j].copy();
			System.out.println("Input word: " + inputWord);
			BitVectorUtils.sumVectors(sum, inputWord);
		}

		BitVector[] weights = BitVectorUtils.discretizeIntVector(sum,SUM_WEIGHT);
		int[] bufAux = new int[sum.length];
		for (int j = 0; j < weights.length; j++) {
			inputWord = BitVectorUtils.multiplyVectors(weights[j],sumWeights[j]);
			BitVectorUtils.sumVectors(bufAux, inputWord);
			sdm.mappedStore(weights[j], sumOpSpace);
		}
		BitVector sumVector = BitVectorUtils.normalizeVector(bufAux);
		sdm.mappedStore(sumVector, sumSpace);
		System.out.println("Sum word: " + sumVector);

		int[] sumOut = BitVectorUtils.denormalizeVector(weights);
		int[] sumAux = new int[sumOut.length];
		System.arraycopy(sumOut, 0, sumAux, 0, sumOut.length);
		System.out.println("Sum:  "+ Arrays.toString(sum));
		System.out.println("SumO: "+ Arrays.toString(sumOut));
		System.out.print("Dif Sums:");
		for (int i=0;i<sumOut.length;i++){
			if(sum[i]-sumOut[i]!=0)
				System.out.print(i+",");
		}
		System.out.println();
		
		int[] sum2 = new int[inputWord.size()];
		for (int j = 0; j < SUM_OP; j++) {
			inputWord = testSet[j].copy();
			inputWord = BitVectorUtils.noisyVector(inputWord, NOISE);
			System.out.println("Input word: " + inputWord);
			BitVectorUtils.sumVectors(sum2, inputWord);
		}

		BitVector[] weightsOut = BitVectorUtils.discretizeIntVector(sum2,SUM_WEIGHT);

		bufAux = new int[sum.length];
		for (int j = 0; j < weightsOut.length; j++) {
			inputWord = BitVectorUtils.multiplyVectors(weightsOut[j],
					sumWeights[j]);
			BitVectorUtils.sumVectors(bufAux, inputWord);
		}
		inputWord = BitVectorUtils.normalizeVector(bufAux);

		outputWord = sdm.retrieveIterating(inputWord, sumSpace);
		System.out.println("Output Sum unmapped: " + outputWord);

		address1 = outputWord.copy();
		address1.xor(sumVector);

		System.out.println("Dif: " + address1);

		address1 = outputWord.copy();

		for (int j = 0; j < weights.length; j++) {
			inputWord = BitVectorUtils.multiplyVectors(outputWord,
					sumWeights[j]);
			weightsOut[j] = sdm.retrieveIterating(inputWord, sumOpSpace);
			System.out.println("weightsOut "+j+": " + weightsOut[j]);

			address1 = weightsOut[j].copy();
			address1.xor(weights[j]);

			System.out.println("Dif: " + address1);
		}
		
		sumOut = BitVectorUtils.denormalizeVector(weightsOut);
		sumAux = new int[sumOut.length];
		System.arraycopy(sumOut, 0, sumAux, 0, sumOut.length);
		System.out.print("Dif Sums:");
		for (int i=0;i<sumOut.length;i++){
			if(sum[i]-sumOut[i]!=0)
				System.out.print(i+",");
		}
		System.out.println();
		
		for (int i = 0; i < SUM_OP; i++) {
			inputWord = BitVectorUtils.normalizeVector(sumAux);
			input1 = sdm.retrieveIterating(inputWord, elemSpace);

			System.out.println("Sum Operand " + i + ":" + outputWord);
			verify(testSet, input1);
			sumAux = BitVectorUtils.substractVectors(sumAux, input1);
		}
	}

	public static void verify(BitVector[] testSet, BitVector v) {
		for (int j = 0; j < SUM_OP; j++) {
			BitVector inputWord = testSet[j].copy();
			inputWord.xor(v);
			System.out.println("Dif " + j + ":" + inputWord);
		}
	}
}
