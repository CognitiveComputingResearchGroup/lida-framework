/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FrameworkGuiDef;
import edu.memphis.ccrg.lida.framework.initialization.FrameworkGuiDef.ExtendedState;
import edu.memphis.ccrg.lida.framework.initialization.FrameworkGuiPanelDef;
import edu.memphis.ccrg.lida.framework.initialization.XmlUtils;

/**
 * For specific agent implementations, create a main class simply
 * need call this class's start method with particular parameters for that agent: {@link Agent} instance and a
 * Properties.
 * Creates a MVC for the system.
 * 
 * @author Ryan J. McCall
 * @author Matthew Lohbihler (edits)
 * 
 */
public class FrameworkGuiFactory {

    private static final Logger logger = Logger.getLogger(FrameworkGuiFactory.class.getCanonicalName());
    private static final String DEFAULT_CONFIG_FILENAME = "configs/guiDef.xml";
    private static final String DEFAULT_CONFIG_SCHEMA_PATH = "edu/memphis/ccrg/lida/framework/initialization/config/LidaGuiDef.xsd";
    private static final String DEFAULT_COMMANDS_FILENAME = "configs/guiCommands.properties";
    private static final String DEFAULT_PANELS_FILENAME = "configs/guiPanels.properties";
    private static final int DEFAULT_GUI_REFRESH_RATE = 5;

    // Legacy panel properties file fields.
    //    private static final String PANEL_PROPFILE_COMMENT = "name = real name, class name, Position [A,B,C,FLOAT, TOOL], Order at Position, Refresh after load?, Additional strings are used as general parameters for the panel's initPanel method";
    private static int PANEL_TITLE = 0;
    private static int PANEL_CLASS_NAME = 1;
    private static int PANEL_POSITION = 2;
    private static int PANEL_TAB_ORDER = 3;
    private static int PANEL_MUST_REFRESH = 4;
    private static int PANEL_FIRST_PARAM = 5;

    /**
     * Based on the properties file, first creates a {@link FrameworkGuiController} with specified {@link Agent}.
     * Then create a {@link FrameworkGui} with the controller.
     * 
     * @param agent
     *            {@link Agent}
     * @param systemProperties
     *            properties containing information about gui configuration, gui commands
     */
    public static void start(final Agent agent, final Properties systemProperties) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Create the controller
                String filename = systemProperties.getProperty("lida.gui.commands", DEFAULT_COMMANDS_FILENAME);
                Properties properties = ConfigUtils.loadProperties(filename);
                int guiEventsInterval = DEFAULT_GUI_REFRESH_RATE;
                if (properties == null) {
                    logger.log(Level.SEVERE, "Unable to load GUI commands from " + filename);
                    properties = new Properties();
                }
                FrameworkGuiController controller = new FrameworkGuiControllerImpl(agent, properties);
                logger.log(Level.INFO, "GUI Controller created\n", 0L);

                String refreshRateStr = systemProperties.getProperty("lida.gui.refreshRate", "");
                try {
                    guiEventsInterval = Integer.parseInt(refreshRateStr);
                }
                catch (NumberFormatException e) {
                    guiEventsInterval = DEFAULT_GUI_REFRESH_RATE;
                }
                agent.getTaskManager().setGuiEventsInterval(guiEventsInterval);

                // GUI configuration.
                FrameworkGuiDef guiDef = loadConfig(systemProperties);

                FrameworkGui frameworkGui = new FrameworkGui(agent, controller, guiDef);
                frameworkGui.setVisible(true);
                logger.log(Level.INFO, "FrameworkGui started\n", 0L);
            }
        });
    }

    /**
     * When given a filename at which to find the configuration, use the file's extension to determine whether XML or
     * properties is being used.
     * 
     * @param filename
     *            where to find the configuration.
     * @return
     *         the loaded GUI configuration.
     */
    public static FrameworkGuiDef loadConfig(String filename) {
        FrameworkGuiDef guiDef = new FrameworkGuiDef();

        if (filename.toLowerCase().endsWith(".xml")) {
            Document doc = XmlUtils.parseXmlFile(filename, DEFAULT_CONFIG_SCHEMA_PATH);
            if (doc == null)
                logger.log(Level.SEVERE, "Error loading GUI config XML file at: " + filename);
            else
                populateConfig(guiDef, doc);
        }
        else {
            Properties properties = ConfigUtils.loadProperties(filename);
            if (properties == null)
                logger.log(Level.SEVERE, "unable to load GUI panels from " + filename);
            else
                populateConfig(guiDef, properties);
        }

        return guiDef;
    }

    /**
     * When looking in the system properties for the configuration, we look at properties with different names to
     * distinguish the XML configuration file from the panel properties file.
     * 
     * @param systemProperties
     *            the system properties.
     * @return
     *         the loaded GUI configuration
     */
    static FrameworkGuiDef loadConfig(Properties systemProperties) {
        FrameworkGuiDef guiDef = new FrameworkGuiDef();

        // Look for the config xml file.
        String filename = systemProperties.getProperty("lida.gui.config", DEFAULT_CONFIG_FILENAME);
        File file = new File(filename);
        if (file.exists()) {
            Document doc = XmlUtils.parseXmlFile(filename, DEFAULT_CONFIG_SCHEMA_PATH);
            if (doc == null)
                logger.log(Level.SEVERE, "Error loading GUI config XML file at: " + filename);
            else
                populateConfig(guiDef, doc);
        }
        else {
            logger.log(Level.INFO, "GUI config XML file not found. Failing over to legacy properties file");

            filename = systemProperties.getProperty("lida.gui.panels", DEFAULT_PANELS_FILENAME);
            Properties properties = ConfigUtils.loadProperties(filename);
            if (properties == null)
                logger.log(Level.SEVERE, "unable to load GUI panels from " + filename);
            else
                populateConfig(guiDef, properties);
        }

        return guiDef;
    }

    private static void populateConfig(FrameworkGuiDef guiDef, Document xml) {
        Element root = xml.getDocumentElement();

        Element frame = XmlUtils.getChild(root, "frame");
        if (frame != null) {
            String left = frame.getAttribute("left");
            String top = frame.getAttribute("top");
            if (!isBlank(left) || !isBlank(top))
                guiDef.setFrameLocation(new Point(toInt(left, 0), toInt(top, 0)));

            String width = frame.getAttribute("width");
            String height = frame.getAttribute("height");
            if (!isBlank(width) || !isBlank(height))
                guiDef.setFrameSize(new Dimension(toInt(width, 0), toInt(height, 0)));

            try {
                guiDef.setFrameExtendedState(ExtendedState.valueOf(frame.getAttribute("extendedState")));
            }
            catch (Exception e) {
                logger.log(Level.WARNING, "Invalid extendedState value: " + frame.getAttribute("extendedState"));
            }
        }

        Element panels = XmlUtils.getChild(root, "panels");
        if (panels != null) {
            List<Element> panelElements = XmlUtils.getChildren(panels, "panel");

            for (Element panelElement : panelElements) {
                FrameworkGuiPanelDef panel = new FrameworkGuiPanelDef();

                panel.setName(panelElement.getAttribute("name"));
                panel.setTitle(panelElement.getAttribute("title"));
                panel.setClassName(XmlUtils.getTextValue(panelElement, "class"));
                panel.setPosition(panelElement.getAttribute("position"));
                panel.setTabOrder(XmlUtils.getIntAttribute(panelElement, "tabOrder", 0));
                panel.setRefreshAfterLoad(XmlUtils.getBooleanAttribute(panelElement, "refreshAfterLoad", true));

                String s = XmlUtils.getTextValue(panelElement, "options");
                if (s != null)
                    panel.setOptions(s.split(","));
                else
                    panel.setOptions(new String[0]);

                guiDef.getPanels().add(panel);
            }
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    private static int toInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            // ignore
        }
        return defaultValue;
    }

    private static void populateConfig(FrameworkGuiDef guiDef, Properties properties) {
        for (Entry<Object, Object> entry : properties.entrySet()) {
            String name = (String) entry.getKey();
            String line = (String) entry.getValue();

            // title,class,pos,tab Order,refresh[Y/N],[optional parameters],...
            String[] vals = line.split(",");
            if ((vals.length < PANEL_FIRST_PARAM))
                logger.log(Level.WARNING, "Error reading line for Panel {1}", new Object[] { 0L, name });
            else {
                FrameworkGuiPanelDef panel = new FrameworkGuiPanelDef();

                panel.setName(name);
                panel.setTitle(vals[PANEL_TITLE]);
                panel.setClassName(vals[PANEL_CLASS_NAME]);
                panel.setPosition(vals[PANEL_POSITION]);
                panel.setTabOrder(Integer.parseInt(vals[PANEL_TAB_ORDER]));  //TODO catch and log Exceptions
                panel.setRefreshAfterLoad(Boolean.parseBoolean(vals[PANEL_MUST_REFRESH]));//TODO catch and log Exceptions

                String[] options;
                if (vals.length > PANEL_FIRST_PARAM) {
                    options = new String[vals.length - PANEL_FIRST_PARAM];
                    System.arraycopy(vals, PANEL_FIRST_PARAM, options, 0, options.length);
                }
                else
                    // Ensure not null
                    options = new String[0];
                panel.setOptions(options);

                guiDef.getPanels().add(panel);
            }
        }
    }

    //    public static void savePanelConfigToFile(String path, Rectangle bounds, int extendedState) {
    //        // TODO save based upon filename extension
    //        //        private void savePanelConfigToFile(String path) {
    //        //            Properties prop = new Properties();
    //        //            for (GuiPanel p : panels) {
    //        //                String panelProperties = "";
    //        //                String[] propArray = panelParameters.get(panels.indexOf(p));
    //        //                for (int i = 0; i < propArray.length; i++) {
    //        //                    panelProperties += propArray[i] + ",";
    //        //                }
    //        //                prop.setProperty(p.getName(), panelProperties.substring(0, panelProperties.length() - 1));
    //        //            }
    //        //            try {
    //        //                prop.store(new java.io.FileOutputStream(path), PANEL_PROPFILE_COMMENT);
    //        //            }
    //        //            catch (IOException ex) {
    //        //                logger.log(Level.WARNING, ex.getMessage(), 0L);
    //        //            }
    //        //        }
    //    }
}