/*
 * @(#)SparseDistributedMemory.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

/**
 * Implementation of Kanerva's sparse distributed memory. This implementation is
 * based on the model described in P. Kanerva, "Sparse Distributed Memory and
 * Related Models" in <i>Associative Neural Memories: Theory and Implementation
 * </i>, pp. 50-76, Oxford University Press, 1993.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class SparseDistributedMemory {

    private byte []inputWord;
    private byte []outputWord;
    private byte []address;
    private byte [][]addressMatrix;
    private byte []activationVector;
    private int [][]contentsMatrix;
    private int []similarityVector;
    private int []sumVector;
    private int addressLength;
    private int wordLength;
    private int memorySize;
    @SuppressWarnings("unused")
	private double activationProbability;
    private int activationRadius;
    private int activationThreshold;
    private int []counterRange;

    /**
     * Constructor of the class that receives all the parameters necessary for
     * this sparse distributed memory.
     * @param a the address (hard locations) matrix
     * @param p the activation probability
     * @param h the activation radius
     * @param c the counter range
     * @param u the word size
     */
    public SparseDistributedMemory(byte [][]a, double p, int h, int[] c, int u) {
        //Memory's internal parameters
        memorySize = a.length;
        addressMatrix = a;
        activationProbability = p;
        activationRadius = h;
        activationThreshold = memorySize - 2 * activationRadius;
        counterRange = c;

        //Memory's state and operation
        address = new byte[a[0].length];
        addressLength = address.length;
        similarityVector = new int[memorySize];
        activationVector = new byte[memorySize];
        wordLength = u;
        inputWord = new byte[wordLength];
        contentsMatrix = new int[memorySize][wordLength];
        sumVector = new int[wordLength];
        outputWord = new byte[wordLength];
    }

    /**
     * Stores a word in the given address in this sparse distributed memory.
     * @param w the word to be stored
     * @param x the address where the word is to be stored
     */
    public void store(byte[] w, byte[] x) {
        inputWord = w;
        address = x;
        similarityVector = getSimilarities();
        activationVector = getActivations(similarityVector);
        contentsMatrix = getNewContents(contentsMatrix,
                calculateCounters(activationVector, inputWord));
    }

    /**
     * Retrieves the contents of this sparse distributed memory at the given
     * address.
     * @param x the address of the contents to be retrieved
     * @return the contents of this sparse distributed memory associated with
     * the given address
     */
    public byte[] retrieve(byte[] x) {
        address = x;
        similarityVector = getSimilarities();
        activationVector = getActivations(similarityVector);
        sumVector = getSums(traspose(contentsMatrix), activationVector);
        outputWord = getOutput(sumVector);
        return outputWord;
    }

    /**
     * Gets the acticvation of each hard location in the address matrix. This
     * activation is 1 if the Hamming distance between the address register and
     * the hard location is less or equal to the activation threshold.
     * @param d the Hamming distances vector between the address register and
     * the hard locations
     * @return a vector with the activation of each hard location
     */
    private byte[] getActivations(int[] d)
    {
        byte[] y = new byte[memorySize];
        for (int i = 0; i != memorySize; i++) {
            y[i] = d[i] <= activationThreshold ? (byte) 1 : (byte) 0;
        }
        return y;
    }

    /**
     * Gets the output word based on the sums vector. For each position in the
     * sums vector, if the position is greater than zero, the output is 1,
     * otherwise the output is 0.
     * @param s the sums vector, position i contains the sum of all the counters
     * in column i of the contents matrix
     * @return the output word
     */
    private byte[] getOutput(int[] s) {
        byte[] z = new byte[wordLength];
        for (int i = 0; i != wordLength; i++) {
            z[i] = s[i] > 0 ? (byte) 1 : (byte) 0;
        }
        return z;
    }

    /**
     * Calculates the Hamming distances between the address register, and each
     * of the hard locations in the address matrix.
     * @return a vector with the Hamming distances
     */
    private int[] getSimilarities() {
        for (int i = 0; i != memorySize; i++) {
            similarityVector[i] = 0;
            for (int j = 0; j!= addressLength; j++) {
                similarityVector[i] += addressMatrix[i][j] == address[j] ? 1 : 0;
            }
        }
        return similarityVector;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    private int[][] getNewContents(int[][] contents, int[][] oldCounters) {
        int r = contents.length;
        int c = contents[0].length;
        int[][] newCounters = new int[r][c];
        for (int i = 0; i != r; i++) {
            for (int j = 0; j != c; j++) {
                if (contents[i][j] > counterRange[0]
                    && contents[i][j] < counterRange[1]) {
                    newCounters[i][j] = contents[i][j] + oldCounters[i][j];
                }
            }
        }
        return newCounters;
    }

    /**
     * Trasposes a matrix.
     * @param contents
     * @return
     */
    private int[][] traspose(int[][] m) {
        int[][] mt = new int[m[0].length][m.length];
        for (int i = 0; i != mt.length; i++) {
            for (int j = 0; j != m[0].length; j++) {
                mt[i][j] = m [j][i];
            }
        }
        return mt;
    }

    /**
     *
     * @param activations
     * @param word
     * @return
     */
    private int[][] calculateCounters(byte[] activations, byte[] word) {
        int[][] counters = new int[activations.length][word.length];
        for (int i = 0; i != counters.length; i++)
            for (int j = 0; j != counters[0].length; j++) {
                counters[i][j] += activations[i] * word[j];
            }
        return counters;
    }

    /**
     *
     * @param contents
     * @param activations
     * @return
     */
    private int[] getSums(int[][] contents, byte[] activations) {
        int[] sums = new int[contents.length];
        for (int i = 0; i != contents.length; i++) {
            for (int j = 0; j != contents[0].length; j++) {
                sums[i] += contents[i][j] * activations[j];
            }
        }
        return sums;
    }
}