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
public class SparseDistributedMemory {

	private static Logger logger = Logger.getLogger("sdm");
	private static final int MAX_ITERATIONS = 20;
	private BitVector[] addressMatrix;
	private byte[][] contentsMatrix;
	private int wordLength;
	private int memorySize;
	private int activationRadius;
	private int counterMax;
	private int activationThreshold;

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
	public SparseDistributedMemory(BitVector[] a, int h, int counterMax, int u) {
		// Memory's internal parameters
		memorySize = a.length;
		addressMatrix = a;
		activationRadius = h;
		activationThreshold = memorySize - 2 * activationRadius;
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
			if (hamming(addr, addressMatrix[i]) <= activationRadius) {
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
			if (hamming(addr, addressMatrix[i]) <= activationRadius) {
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
	public static BitVector getRandomVector(int size) {
		BitVector v = new BitVector(size);
		for (int i = 0; i < size; i++) {
			v.putQuick(i, Math.random() > .5);
		}
		return v;
	}

	/**
	 * Calculates the Hamming distances between two address.
	 * 
	 * @return the Hamming distances
	 */
	public static int hamming(BitVector addr, BitVector hardLoc) {
		BitVector aux = hardLoc.copy();
		aux.xor(addr);

		return aux.cardinality();
	}

	public static BitVector noisyVector(BitVector orig, int noise) {
		BitVector v = orig.copy();
		int size = v.size();
		for (int i = 0; i < noise; i++) {
			int pos = (int) (Math.random() * size);
			v.putQuick(pos, !v.getQuick(pos));
		}
		return v;
	}
	
	public static int[] sumVectors (int[] accum, BitVector v) {

		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1: -1;
		}
		return accum;
	}
	public static BitVector substractVectors (BitVector a, BitVector b) {
		BitVector r=b.copy();
		r.not();
		BitVector res = new BitVector(a.size());
		for (int i = 0; i < a.size(); i++) {
				boolean bit=(a.getQuick(i)^r.getQuick(i))?(Math.random()>.5):a.getQuick(i);
				res.putQuick(i, bit);
		}
		return res;
	}
	
	public static int[] sumVectors (int[] accum, int[] vector) {

		for (int i = 0; i < vector.length; i++) {
			accum[i] += vector[i];
		}
		return accum;
	}
	
	public static int[] vectorToBipolar (int[] accum, BitVector v) {

		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1: -1;
		}
		return accum;
	}
	
	public static int[] vectorToBipolar (BitVector v) {
		int[] accum=new int[v.size()];
		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1: -1;
		}
		return accum;
	}

	public static BitVector normalizeVector(int[] buff){
		BitVector res = new BitVector(buff.length);
		for (int i=0;i<buff.length;i++){
			res.putQuick(i, buff[i]>0);
			if(buff[i]==0){
				res.putQuick(i, (Math.random()>.5));
			}
		}
		return res;
	}
	
	public static BitVector multiplyVectors(BitVector a, BitVector b){
		BitVector res = a.copy();
		res.xor(b);
		return res;
	}
}