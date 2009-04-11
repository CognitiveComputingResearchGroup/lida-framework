package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemoryListener;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemoryListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public interface EpisodicBuffer extends TransientEpisodicMemoryListener,
									    DeclarativeMemoryListener, 
									    CodeletReadable{

}
