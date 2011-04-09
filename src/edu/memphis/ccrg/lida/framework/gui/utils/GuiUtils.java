package edu.memphis.ccrg.lida.framework.gui.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;

/**
 * A collection of static Gui utility methods.
 * @author Ryan McCall, Javier Snaider
 *
 */
public class GuiUtils {
	
	private static final Logger logger = Logger.getLogger(GuiUtils.class
			.getCanonicalName());

	/**
	 * Utility method to parse a String to obtain a {@link LidaModule}
	 * @param param String to parse
	 * @param lida {@link Lida}
	 * @return LidaModule with specified name or null
 	 */
	public static LidaModule parseLidaModule(String param, Lida lida) {
		if(param == null){
			logger.log(Level.WARNING, "null string argument.", 0L);
			return null;
		}
		
		
		String[] modules = param.trim().split("\\.");
		ModuleName moduleType = ModuleName.getModuleName(modules[0]);
		if (moduleType == null) {
			logger.log(Level.WARNING, "Error getting module type " + moduleType, 0L);
			return null;
		}
		LidaModule module = lida.getSubmodule(moduleType);
		if (module == null) {
			logger.log(Level.WARNING, "Error getting submodule " + moduleType,
					0L);
			return null;
		}
		for (int i = 1; i < modules.length; i++) {
			moduleType = ModuleName.getModuleName(modules[i]);
			if (moduleType == null) {
				logger.log(Level.WARNING, "Error getting submodule "
						+ moduleType, 0L);
				return null;
			}

			module = module.getSubmodule(moduleType);
			if (module == null) {
				logger.log(Level.WARNING, "Error getting submodule "
						+ moduleType, 0L);
				return null;
			}
		}
		return module;
	}
}
