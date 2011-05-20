package edu.memphis.ccrg.lida.episodicmemory;

import java.util.Map;

import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
/**
 * Initializes the translator of a {@link EpisodicMemoryImpl} 
 * The {@link EpisodicMemoryImpl} must have a valid {@link PerceptualAssociativeMemory} associated module
 * 
 * @author Javier Snaider
 *
 */
public class BasicEMinitializer implements Initializer {

	@Override
	public void initModule(FullyInitializable module, Agent agent,
			Map<String, ?> params) {
		EpisodicMemoryImpl em = (EpisodicMemoryImpl)module;
		PerceptualAssociativeMemory pam = em.getPam();
		int wordLength = (Integer) em.getParam("tem.wordLength", EpisodicMemoryImpl.DEF_WORD_LENGTH);
		Translator translator = new BasicTranslator(wordLength, pam);
		em.setTranslator(translator);
	}

}
