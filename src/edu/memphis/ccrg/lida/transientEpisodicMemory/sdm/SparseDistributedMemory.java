/*
 * @(#)SparseDistributedMemory.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory.sdm;

/**
 *
 * @author Rodrigo Silva L.
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
    private int addressSize;
    private int wordSize;
    private int memorySize;
    private double activationProbability;
    private int activationRadius;
    private int activationThreshold;
    private int []counterRange;

    /**
     *
     * @param a
     * @param p
     * @param h
     * @param c
     * @param u
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
        addressSize = address.length;
        similarityVector = new int[memorySize];
        activationVector = new byte[memorySize];
        wordSize = u;
        inputWord = new byte[wordSize];
        contentsMatrix = new int[memorySize][wordSize];
        sumVector = new int[wordSize];
        outputWord = new byte[wordSize];
    }

    /**
     *
     * @param w
     * @param x
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
     *
     * @param x
     * @return
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
     *
     * @param d
     * @return
     */
    private byte[] getActivations(int[] d)
    {
        byte[] y = new byte[memorySize];
        for (int i = 0; i != memorySize; i++) {
            y[i] = d[i] >= activationThreshold ? (byte) 1 : (byte) 0;
        }
        return y;
    }

    /**
     *
     * @param s
     * @return
     */
    private byte[] getOutput(int[] s) {
        byte[] z = new byte[wordSize];
        for (int i = 0; i != wordSize; i++) {
            z[i] = s[i] > 0 ? (byte) 1 : (byte) -1;
        }
        return z;
    }

    /**
     *
     * @return
     */
    private int[] getSimilarities() {
        for (int i = 0; i != memorySize; i++) {
            similarityVector[i] = 0;
            for (int j = 0; j!= addressSize; j++) {
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
     *
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