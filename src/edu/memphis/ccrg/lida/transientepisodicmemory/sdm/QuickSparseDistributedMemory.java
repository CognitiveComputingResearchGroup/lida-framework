package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.bitvector.BitVector;

/**
 * Implementation of Kanerva's sparse distributed memory. This implementation is
 * based on the model described in P. Kanerva, "Sparse Distributed Memory and
 * Related Models" in <i>Associative Neural Memories: Theory and Implementation
 * </i>, pp. 50-76, Oxford University Press, 1993.
 * 
 * @author Javier Snaider
 */
public class QuickSparseDistributedMemory implements SparseDistributedMemory{

	private static Logger logger = Logger.getLogger("sdm");
	private static final int MAX_ITERATIONS = 20;
	private BitVector[] addressMatrix;
	private byte[][] contentsMatrix;
	private int wordLength;
	private int memorySize;
	private int activationRadius;
	private int counterMax;

	/**
	 * Constructor of the class that receives all the parameters necessary for
	 * this sparse distributed memory.
	 * 
	 * @param a
	 *            the address (hard locations) matrix
	 * @param p
	 *            the activation probability
	 * @param h
	 *            the activation radius
	 * @param c
	 *            the counter range
	 * @param u
	 *            the word size
	 */
	public QuickSparseDistributedMemory(BitVector[] a, int h, int counterMax, int u) {
		// Memory's internal parameters
		memorySize = a.length;
		addressMatrix = a;
		activationRadius = h;
		this.counterMax = counterMax;

		wordLength = u;
		contentsMatrix = new byte[memorySize][wordLength];
	}

	/**
	 * Stores a word in the given address in this sparse distributed memory.
	 * 
	 * @param w
	 *            the word to be stored
	 * @param x
	 *            the address where the word is to be stored
	 */
	public void store(BitVector wrd, BitVector addr) {

		for (int i = 0; i < addressMatrix.length; i++) {
			if (BitVectorUtils.hamming(addr, addressMatrix[i]) <= activationRadius) {
				actualizeCounters(i, wrd);
			}
		}
	}

	/**
	 * Stores a word in this sparse distributed memory using the word as address.
	 * 
	 * @param wrd
	 *            the word to be stored
	 */
	public void store(BitVector wrd) {
		store(wrd,wrd);
			}

	/**
	 * Stores a word in this sparse distributed memory using the word as address.
	 * The word is mapped (xor) with the mapping address.
	 * 
	 * @param wrd
	 *            the word to be stored.
	 * @param mapping
	 *            the mapping address.
	 */
	public void mappedStore(BitVector wrd, BitVector mapping) {
		BitVector mapped = wrd.copy();
		mapped.xor(mapping);
		store(mapped);
			}
	/**
	 * Stores a word in this sparse distributed memory using the word as address.
	 * The word is mapped (xor) with the mapping address.
	 * 
	 * @param wrd
	 *            the word to be stored.
	 * @param mapping
	 *            the mapping address.
	 */
	public void mappedStore(BitVector wrd,BitVector addr, BitVector mapping) {
		BitVector mapped = addr.copy();
		mapped.xor(mapping);
		store(wrd,mapped);
			}

	/**
	 * Retrieves the contents of this sparse distributed memory at the given
	 * address.
	 * 
	 * @param x
	 *            the address of the contents to be retrieved
	 * @return the contents of this sparse distributed memory associated with
	 *         the given address
	 */
	public BitVector retrieve(BitVector addr) {
		int[] buff = new int[wordLength];
		for (int i = 0; i < addressMatrix.length; i++) {
			if (BitVectorUtils.hamming(addr, addressMatrix[i]) <= activationRadius) {
				readHard(buff, i);
			}
		}
		BitVector res = new BitVector(wordLength);
		for (int i = 0; i < wordLength; i++) {
			res.putQuick(i, buff[i] > 0);
		}
		return res;
	}

	public BitVector retrieveIterating(BitVector addr) {

		BitVector res=null;
		if (wordLength == addr.size()) {
			for (int i = 1; i < MAX_ITERATIONS; i++) {
				res = retrieve(addr);
				if (res.equals(addr)) {
					logger.log(Level.INFO,"number of iterations: "+i);
					return res;
				}
				addr = res;
			}
		}
		return res;
	}

	public BitVector retrieveIterating(BitVector addr,BitVector mapping) {
		BitVector mapped = addr.copy();
		mapped.xor(mapping);

		BitVector res= retrieveIterating(mapped);
		res.xor(mapping);
		return res;
	}

	
	
	private void actualizeCounters(int row, BitVector word) {
		for (int j = 0; j != word.size(); j++) {
			if (word.getQuick(j)) {
				if (contentsMatrix[row][j] < counterMax) {
					contentsMatrix[row][j] += 1;
				}
			} else {
				if (contentsMatrix[row][j] > -counterMax) {
					contentsMatrix[row][j] += -1;
				}
			}
		}
	}

	private int[] readHard(int[] buff, int row) {

		for (int i = 0; i < wordLength; i++) {
			buff[i] += Integer.signum(contentsMatrix[row][i]);
		}
		return buff;
	}
}