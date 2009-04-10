package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.wumpusWorld.e_perceptualBuffer.PerceptualBufferContentImpl;


public interface PerceptualBufferListener {
	
	public void receivePBufferContent(PerceptualBufferContentImpl pbc);

}
