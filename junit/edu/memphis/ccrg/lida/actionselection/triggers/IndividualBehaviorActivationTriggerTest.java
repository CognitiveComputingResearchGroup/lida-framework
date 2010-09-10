/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.triggers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.mockclasses.ActionSelectionImpl;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 * @author Home
 *
 */
public class IndividualBehaviorActivationTriggerTest {
	Set<Scheme> setOfSchemes;
	Scheme schemeA;
	Scheme schemeB;
	IndividualBehaviorActivationTrigger trigger;
	ActionSelection as;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		setOfSchemes = new HashSet<Scheme>();
		schemeA = new SchemeImpl("Scheme1",1,1);
		schemeB = new SchemeImpl("Scheme2",2,2);
			
		trigger = new IndividualBehaviorActivationTrigger();
		
		as = new ActionSelectionImpl();
		schemeA.setActivation(0.2);
		schemeB.setActivation(0.6);		
		
		setOfSchemes.add(schemeA);
		setOfSchemes.add(schemeB);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.IndividualBehaviorActivationTrigger#checkForTrigger(java.util.Set)}.
	 */
	@Test
	public void testCheckForTriggerSetOfScheme() {
		trigger.as=as;
		trigger.threshold=0.5;
		trigger.checkForTrigger(setOfSchemes);
	}

}
