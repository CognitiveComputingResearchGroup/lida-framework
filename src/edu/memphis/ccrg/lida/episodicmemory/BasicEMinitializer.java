/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory;

import java.util.Map;

import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
/**
 * Initializes the translator of a {@link EpisodicMemoryImpl} using {@link BasicTranslator}
  * 
 * @author Javier Snaider
 *
 */
public class BasicEMinitializer implements Initializer {

	@Override
	public void initModule(FullyInitializable module, Agent agent,
			Map<String, ?> params) {
		EpisodicMemoryImpl em = (EpisodicMemoryImpl)module;
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) agent.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		int wordLength = (Integer) em.getParam("tem.wordLength", EpisodicMemoryImpl.DEF_WORD_LENGTH);
		Translator translator = new BasicTranslator(wordLength, pam);
		em.setTranslator(translator);
	}

}
