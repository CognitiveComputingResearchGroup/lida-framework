/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * Initializes the translator of a {@link EpisodicMemoryImpl} using
 * {@link BasicTranslator}
 * 
 * @author Javier Snaider
 * 
 */
public class BasicEpisodicMemoryInitializer implements Initializer {

	private static final Logger logger = Logger
			.getLogger(BasicEpisodicMemoryInitializer.class.getCanonicalName());

	@Override
	public void initModule(FullyInitializable module, Agent agent,
			Map<String, ?> params) {

		EpisodicMemoryImpl em = (EpisodicMemoryImpl) module;

		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) agent
				.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		if (pam != null) {
			int wordLength = (Integer) em.getParam("tem.wordLength",
					EpisodicMemoryImpl.DEF_WORD_LENGTH);

			Translator translator = new BasicTranslator(wordLength, pam);
			em.setTranslator(translator);
		} else {
			logger
					.log(
							Level.SEVERE,
							"No Perseptual Associtive Memory in Agent. Translator is not created.",
							TaskManager.getCurrentTick());
		}
	}

}
