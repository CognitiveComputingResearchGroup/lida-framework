package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface Translator {

	/**
	 * Translates a boolean address into a node structure.
	 * @param data a byte vector with the boolean vector to be translated
	 * @return the node structure associated with the address
	 */
	public abstract NodeStructure translate(byte[] data);

	/**
	 * Translates a node structure into a boolean address.
	 * @param structure the node structure to be translated
	 * @return a byte vector with the boolean address associated with the structure
	 */
	public abstract byte[] translate(NodeStructure structure);

}