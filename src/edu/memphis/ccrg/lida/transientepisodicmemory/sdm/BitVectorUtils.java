package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;

public class BitVectorUtils {

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

	public static int[] sumVectors(int[] accum, BitVector v) {
	
		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1: -1;
		}
		return accum;
	}

	public static BitVector substractVectors(BitVector a, BitVector b) {
		BitVector r=b.copy();
		r.not();
		BitVector res = new BitVector(a.size());
		for (int i = 0; i < a.size(); i++) {
				boolean bit=(a.getQuick(i)^r.getQuick(i))?(Math.random()>.5):a.getQuick(i);
				res.putQuick(i, bit);
		}
		return res;
	}

	public static int[] sumVectors(int[] accum, int[] vector) {
	
		for (int i = 0; i < vector.length; i++) {
			accum[i] += vector[i];
		}
		return accum;
	}

	public static int[] vectorToBipolar(int[] accum, BitVector v) {
	
		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1: -1;
		}
		return accum;
	}

	public static int[] vectorToBipolar(BitVector v) {
		int[] accum=new int[v.size()];
		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1: -1;
		}
		return accum;
	}

	public static BitVector normalizeVector(int[] buff) {
		BitVector res = new BitVector(buff.length);
		for (int i=0;i<buff.length;i++){
			res.putQuick(i, buff[i]>0);
			if(buff[i]==0){
				res.putQuick(i, (Math.random()>.5));
			}
		}
		return res;
	}

	public static BitVector multiplyVectors(BitVector a, BitVector b) {
		BitVector res = a.copy();
		res.xor(b);
		return res;
	}

	public BitVectorUtils() {
		super();
	}

}