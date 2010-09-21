/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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
			accum[i] += (v.getQuick(i)) ? 1 : -1;
		}
		return accum;
	}

	public static BitVector substractVectors(BitVector a, BitVector b) {
		BitVector r = b.copy();
		r.not();
		BitVector res = new BitVector(a.size());
		for (int i = 0; i < a.size(); i++) {
			boolean bit = (a.getQuick(i) ^ r.getQuick(i)) ? (Math.random() > .5)
					: a.getQuick(i);
			res.putQuick(i, bit);
		}
		return res;
	}

	public static int[] substractVectors(int[] a, BitVector v) {
		int[] res = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			res[i] = a[i]-((v.getQuick(i))?1:-1);
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
			accum[i] += (v.getQuick(i)) ? 1 : -1;
		}
		return accum;
	}

	public static int[] vectorToBipolar(BitVector v) {
		int[] accum = new int[v.size()];
		for (int i = 0; i < v.size(); i++) {
			accum[i] += (v.getQuick(i)) ? 1 : -1;
		}
		return accum;
	}

	public static BitVector normalizeVector(int[] buff) {
		BitVector res = new BitVector(buff.length);
		for (int i = 0; i < buff.length; i++) {
			res.putQuick(i, buff[i] > 0);
			if (buff[i] == 0) {
				res.putQuick(i, (Math.random() > .5));
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

	public static BitVector[] discretizeIntVector(int[] buff, int bitSteps) {
		BitVector[] weights = new BitVector[bitSteps];
		int maxUnsignedValue=(1<<bitSteps);
		for (int i = 0; i < weights.length; i++) {
			weights[i] = new BitVector(buff.length);
		}

		for (int i = 0; i < buff.length; i++) {
			int aux = buff[i];
			//In case of 0, a value is chosen by random
//			if (aux == 0) {
//				aux = (Math.random() > .5) ? 1 : -1;
//			}
//			//The value is decreased by 1 in case of a positive value. In this way the value zero is used
//			if (aux > 0) {
//				aux--;
//			}
			//the value is biased by 4 to use only positive values
			aux = aux + maxUnsignedValue/2;
			//the value is truncated between 0 and maxUnsignedValue to be stored in bitSteps bits
			aux = (aux >= maxUnsignedValue) ? maxUnsignedValue-1 : aux;
			aux = (aux < 0) ? 0 : aux;
			
			for (int j = 0; j < bitSteps; j++) {
				boolean b=((aux & (1 << j)) != 0);
				weights[j].putQuick(i, b);
			}
		}
		return weights;
	}

	public static int[] denormalizeVector(BitVector[] weights) {

		int bitSteps=weights.length;
		int maxUnsignedValue=1<<bitSteps;
		int[] sum = new int[weights[0].size()];
		for (int i = 0; i < sum.length; i++) {
			int totDigit = 0;
			for (int j = 0; j < bitSteps; j++) {
				int aux = (weights[j].getQuick(i)) ? 1 : 0;
				totDigit += aux << j;
			}
			totDigit = totDigit - maxUnsignedValue/2;

//			if (totDigit >= 0) {
//				totDigit++;
//			}
			sum[i] += totDigit;
		}

		return sum;
	}

}