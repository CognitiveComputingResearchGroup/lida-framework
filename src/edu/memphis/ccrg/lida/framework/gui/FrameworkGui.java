/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameworkGui.java
 *
 * Created on 12/07/2009, 08:47:40
 */
package edu.memphis.ccrg.lida.framework.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;
import edu.memphis.ccrg.lida.framework.gui.commands.DAOCommand;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.panels.AddEditPanel;
import edu.memphis.ccrg.lida.framework.gui.panels.GuiPanel;
import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * The main GUI for the LIDA framework. A swing JFrame that is divide into four
 * regions. Each region can contain one or more {@link GuiPanel}s specified by
 * a config file. Panels can be assigned to one of the regions or can "float"
 * apart from the main JFrame. Implements the GUI part of the MVC pattern. <br/>
 * The format of the configuration file is as follows:<br/>
 * <br/>
 * #name = panel title, class name, Position [A,B,C,FLOAT, TOOL], tab order,
 * Refresh after load, [optional parameters]* <br/>
 * <br/>
 * <b>name</b> - panel name, must be unique<br/>
 * <b>panel title</b> - title of the panel as displayed in the GUI<br/>
 * <b>class name</b> - canonical name of the panel's java class, must implement
 * {@link GuiPanel}<br/>
 * <b>position</b> - location the panel will be placed in the main GUI window<br/>
 * <b>tab order</b> - if there are multiple panels assigned to a particular
 * region, this specifies the position this panel will appear in that region. <br/>
 * <b>refresh after load</b> - determines if the panel will be refreshed after
 * loading.<br/>
 * <b>optional parameters</b> - additional parameters may be defined, separated
 * by comma.
 * 
 * @author Javier Snaider
 */
public class FrameworkGui extends javax.swing.JFrame implements FrameworkGuiEventListener{

	private static final Logger logger = Logger.getLogger(FrameworkGui.class
			.getCanonicalName());
	private static final String PANEL_PROPFILE_COMMENT = "name = real name, class name, Position [A,B,C,FLOAT, TOOL], Order at Position, Refresh after load?, Additional strings are used as general parameters for the panel's initPanel method";

	private static int PANEL_NAME = 0;
	private static int CLASS_NAME = 1;
	private static int PANEL_POSITION = 2;
	private static int TAB_ORDER = 3;
	private static int MUST_REFRESH = 4;
	private static int FIRST_PARAM = 5;

	private List<String> panelClassNames = new ArrayList<String>();
	private List<GuiPanel> panels = new ArrayList<GuiPanel>();
	private List<String[]> panelParameters = new ArrayList<String[]>();
	private List<Container> panelParents = new ArrayList<Container>();

	private Agent agent;
	private FrameworkGuiController controller;
	private javax.swing.JDialog addEditDialog;

	//TODO convert config files from Properties to XML
	/**
	 * Constructs a new FrameworkGui using the {@link Agent} object as the model and
	 * {@link FrameworkGuiController} as the controller. Reads the {@link Properties}
	 * file and creates and configures the {@link GuiPanel}s specified therein.
	 * 
	 * @param agent
	 *            {@link Agent} the model.
	 * @param controller
	 *            {@link FrameworkGuiController} the controller
	 * @param panelProperties
	 *            for configuration
	 */
	public FrameworkGui(Agent agent, FrameworkGuiController controller,
			Properties panelProperties) {
		if (agent == null || controller == null || panelProperties == null) {
			logger.log(Level.WARNING, "Null argument", TaskManager
					.getCurrentTick());
		}

		initComponents();

		this.agent = agent;
		this.controller = controller;
		TaskManager tm = agent.getTaskManager();
		tm.addFrameworkGuiEventListener(this);
		tm.setGuiEventsInterval(10); //TODO parameter or command
				
		loadPanels(panelProperties);

		pack();
	}

	/*
	 * @param panelProp
	 */
	private void loadPanels(Properties panelProp) {
		String[][] panelsArray = new String[panelProp.size()][];
		int i = 0;
		// for each key in the properties config file
		for (Object key : panelProp.keySet()) {
			String line = panelProp.getProperty((String) key);
			String[] vals = line.split(","); // name,class,pos,tab
			// Order,refresh[Y/N],[optional
			// parameters],...
			if ((vals.length < FIRST_PARAM)) {
				logger.log(Level.WARNING,
						"Error reading line for Panel {1}", new Object[]{0L, key});
			} else {
				panelsArray[i++] = vals;
			}
		}
		Arrays.sort(panelsArray, new Comparator<String[]>() { // sort panel by
					// position and
					// tab order

					@Override
					public int compare(String[] arg0, String[] arg1) {
						String s1 = arg0[PANEL_POSITION];
						String s2 = arg1[PANEL_POSITION];
						int pos = s1.compareToIgnoreCase(s2);
						if (pos == 0) {
							s1 = arg0[TAB_ORDER];
							s2 = arg1[TAB_ORDER];
							pos = s1.compareToIgnoreCase(s2);
						}
						return pos;
					}
				});

		for (String[] vals : panelsArray) {
			panelClassNames.add(vals[CLASS_NAME]);
			createGuiPanel(vals);
		}
	}

	/**
	 * Based on the specified parameters (from the configuration file), creates
	 * a new {@link GuiPanel} and initializes it.
	 * 
	 * @param panelParams
	 *            Parameters specified in 'configs.guiPanels.properties'
	 */
	public void createGuiPanel(String[] panelParams) {
		GuiPanel panel;
		try {
			panel = (GuiPanel) (Class.forName(panelParams[CLASS_NAME]))
					.newInstance();
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Error instantiating panel {1}", new Object[]{0L,CLASS_NAME});
			e.printStackTrace();
			return;
		}
		panelParameters.add(panelParams);
		panel.setName(panelParams[PANEL_NAME]);
		panel.registerAgent(agent);
		panel.registerGuiController(controller);

		// init panel with optional parameters
		String[] param = new String[panelParams.length - FIRST_PARAM];
		System.arraycopy(panelParams, FIRST_PARAM, param, 0, panelParams.length
				- FIRST_PARAM);
		try {
			panel.initPanel(param);
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Exception {1} encountered initializing panel {2}", 
					new Object[]{0L, e.getMessage(), panel});
			e.printStackTrace();
		}

		// add the panel to the specified region of the GUI
		addGuiPanel(panel, panelParams[PANEL_POSITION]);

		if (panelParams[MUST_REFRESH].equalsIgnoreCase("Y")) {
			try {
				panel.refresh();
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"Exception {1} encountered when refreshing panel {2}", 
						new Object[]{0L, e.getMessage(), panel});
				e.printStackTrace();
			}
		}
		
		logger.log(Level.INFO, "GuiPanel added: " + panel.getName());
	}
	
	/*
	 * Adds a Panel to the main GUI.
	 * 
	 * @param panel
	 *            the panel to add.
	 * @param panelPosition
	 *            Determines where the panel is going to be placed. </br> A:
	 *            Upper Left position. </br> B: Upper Right position. In a new
	 *            TAB</br> C: Lower position. In a new TAB</br> TOOL: In the
	 *            ToolBox </br> FLOAT: In a new frame. </br>
	 */
	private void addGuiPanel(GuiPanel panel, String panelPosition) {
		final JPanel jPanel = panel.getPanel();
		java.awt.Container parent = null;

		panels.add(panel);

		javax.swing.JMenu associatedMenu = areaOthersPanelsMenu;
		if ("A".equalsIgnoreCase(panelPosition)) {
			jSplitPane2.setTopComponent(jPanel);
			parent = jSplitPane2;
			associatedMenu = areaAPanelsMenu;
		} else if ("B".equalsIgnoreCase(panelPosition)) {
			jTabbedPanelR.addTab(panel.getName(), jPanel);
			parent = jTabbedPanelR;
			associatedMenu = areaBPanelsMenu;
		} else if ("C".equalsIgnoreCase(panelPosition)) {
			principalTabbedPanel.addTab(panel.getName(), jPanel);
			parent = principalTabbedPanel;
			associatedMenu = areaCPanelsMenu;
		} else if ("FLOAT".equalsIgnoreCase(panelPosition)) {
			JDialog dialog = new JDialog(this, panel.getName());
			dialog.add(jPanel);
			dialog.pack();
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
			dialog.addWindowListener(new java.awt.event.WindowAdapter() {
				int index = panels.size() - 1;

				@Override
				public void windowClosing(WindowEvent winEvt) {
					for (java.awt.Component firstLevelMenu : panelsMenu
							.getMenuComponents()) {
						if (firstLevelMenu instanceof javax.swing.JMenu) {
							for (java.awt.Component secondLevelMenu : ((javax.swing.JMenu) firstLevelMenu)
									.getMenuComponents()) {
								if (secondLevelMenu instanceof javax.swing.JMenu) {
									String menuText = ((javax.swing.JMenu) secondLevelMenu)
											.getText();
									String panelName = panels.get(index)
											.getName();
									if (menuText.equals(panelName))
										((javax.swing.JCheckBoxMenuItem) ((javax.swing.JMenu) secondLevelMenu)
												.getMenuComponent(0))
												.setSelected(false);
									// ((javax.swing.JMenu)firstLevelMenu).remove(secondLevelMenu);
								}
							}
						}
					}
				}
			});

		} else if ("TOOL".equalsIgnoreCase(panelPosition)) {
			getContentPane().add(jPanel, java.awt.BorderLayout.PAGE_START);
			parent = getContentPane();
		} else {
			logger.log(Level.WARNING, "Position error for panel "
					+ panel.getName() + " pos:" + panelPosition, 0L);
		}

		panelParents.add(parent);

		addToPanelsMenu(panel, parent, associatedMenu);
	}

	private void addToPanelsMenu(final GuiPanel panel, final Container parent, JMenu associatedMenu) {
		final JPanel jPanel = panel.getPanel();
		JMenu cMenu;
		String menuItemLabel;
		int unnamedIndex = 0;

		menuItemLabel = panel.getName();
		if (menuItemLabel.equals("")) {
			menuItemLabel = "Unnamed" + (unnamedIndex++);
		}
		cMenu = new JMenu();
		cMenu.setText(menuItemLabel);

		JCheckBoxMenuItem showItem = new JCheckBoxMenuItem();
		showItem.setText("Show Panel");
		showItem.setSelected(true);
		showItem.addActionListener(new ActionListener() {
			private GuiPanel cGuiPanel = panel;
			private JPanel cjPanel = jPanel;
			private Container cParent = parent;
			private String[] cParameters = panelParameters.get(panels
					.indexOf(panel));

			@Override
			public void actionPerformed(ActionEvent evt) {
				togglePanel();
			}

			private void togglePanel() {
				int index = -1;
				if (parent != null) {
					// normal panel (not FLOAT)
					for (java.awt.Component c : parent.getComponents()) {
						if (c.equals(cjPanel)) {
							for (int i = 0; i < panels.size(); i++) {
								if (panels.get(i).getPanel().equals(cjPanel)) {
									index = i;
									break;
								}
							}
							break;
						}
					}

					if (index > -1) {
						// panel found, remove it
						removePanelAt(index, false);
					} else {
						// panel not found, add it
						cParent.add(cjPanel);
						panels.add(cGuiPanel);
						panelParents.add(cParent);
						panelParameters.add(cParameters);
					}
				} else {
					// dialog (FLOAT)
					for (int i = 0; i < panels.size(); i++) {
						if (panels.get(i).equals(cGuiPanel)) {
							index = i;
							break;
						}
					}

					Container c = cjPanel.getParent();
					while (c != null && !(c instanceof JDialog))
						c = c.getParent();
					if (c instanceof JDialog) {
						if (c.isVisible())
							((JDialog) c).setVisible(false);
						else
							((JDialog) c).setVisible(true);
					}
				}
			}
		});
		cMenu.add(showItem);
		cMenu.add(new JPopupMenu.Separator());
		JMenuItem editItem = new JMenuItem();
		editItem.setText("Edit Panel");
		final String[] panelParams = panelParameters.get(panels.indexOf(panel));
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				showEditPanelDialog(panelParams);
			}
		});
		cMenu.add(editItem);

		associatedMenu.add(cMenu);
	}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPanelR = new javax.swing.JTabbedPane();
        principalTabbedPanel = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        panelsMenu = new javax.swing.JMenu();
        addPanelMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        areaAPanelsMenu = new javax.swing.JMenu();
        areaBPanelsMenu = new javax.swing.JMenu();
        areaCPanelsMenu = new javax.swing.JMenu();
        areaOthersPanelsMenu = new javax.swing.JMenu();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        loadPanelSettingsMenuItem = new javax.swing.JMenuItem();
        savePanelSettingsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setRightComponent(jTabbedPanelR);

        jSplitPane1.setLeftComponent(jSplitPane2);
        jSplitPane1.setRightComponent(principalTabbedPanel);
        principalTabbedPanel.getAccessibleContext().setAccessibleName("Visual");

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        panelsMenu.setText("Panels");

        addPanelMenuItem.setText("Add new panel");
        addPanelMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPanelMenuItemActionPerformed(evt);
            }
        });
        panelsMenu.add(addPanelMenuItem);
        panelsMenu.add(jSeparator1);

        areaAPanelsMenu.setText("Area A");
        panelsMenu.add(areaAPanelsMenu);

        areaBPanelsMenu.setText("Area B");
        panelsMenu.add(areaBPanelsMenu);

        areaCPanelsMenu.setText("Area C");
        panelsMenu.add(areaCPanelsMenu);

        areaOthersPanelsMenu.setText("Others");
        panelsMenu.add(areaOthersPanelsMenu);
        panelsMenu.add(jSeparator3);

        loadPanelSettingsMenuItem.setText("Load panel settings");
        loadPanelSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPanelSettingsMenuItemActionPerformed(evt);
            }
        });
        panelsMenu.add(loadPanelSettingsMenuItem);

        savePanelSettingsMenuItem.setText("Save panel settings");
        savePanelSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePanelSettingsMenuItemActionPerformed(evt);
            }
        });
        panelsMenu.add(savePanelSettingsMenuItem);
        panelsMenu.add(jSeparator2);

        menuBar.add(panelsMenu);

        helpMenu.setText("Help");

        contentsMenuItem.setText("Help");
        contentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentsMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
       JOptionPane.showMessageDialog(this,"LIDA Framework 1.0 Beta \n "
               + "Cognitive Computing Research Group \n The University of Memphis \n"
               + " ccrg.cs.memphis.edu","About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void contentsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contentsMenuItemActionPerformed
       JOptionPane.showMessageDialog(this,"For more on the LIDA framework visit:\n"
               + "ccrg.cs.memphis.edu/framework.html","Help", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_contentsMenuItemActionPerformed

	@SuppressWarnings("unused")
	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		Command command = new DAOCommand();
		command.setParameter("action", DAOCommand.SAVE_ACTION);
		controller.executeCommand(command);
	}

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		Command command = new DAOCommand();
		command.setParameter("action", DAOCommand.LOAD_ACTION);
		controller.executeCommand(command);
	}

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void addPanelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		showAddPanelDialog();
	}
	private void showAddPanelDialog() {
		final AddEditPanel addEditPanel = new AddEditPanel();
		addEditPanel.setName("AddPanel");
		addEditPanel.registerAgent(agent);
		addEditPanel.registerGuiController(this.controller);
		addEditPanel.initClassnames(panelClassNames);
		if (addEditDialog != null)
			addEditDialog.setVisible(false);
		addEditDialog = new JDialog();
		addEditDialog.add(addEditPanel.getPanel());
		addEditDialog.pack();
		addEditDialog.setVisible(true);

		final javax.swing.JPanel jpanel = addEditPanel.getPanel();
		for (java.awt.Component c : jpanel.getComponents()) {
			if (c instanceof javax.swing.JButton) {
				((javax.swing.JButton) c)
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent evt) {
								createGuiPanel(addEditPanel.getPanelParams());
								addEditDialog.setVisible(false);
							}
						});
			}
		}

		addEditPanel.refresh();
	}

	private void loadPanelSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		javax.swing.JFileChooser fc = new javax.swing.JFileChooser(
				new java.io.File(AgentStarter.DEFAULT_PROPERTIES_PATH));
		fc.showOpenDialog(this);
		java.io.File file = fc.getSelectedFile();
		if (file != null) {
			loadPanelConfigFromFile(file.getPath());
		}
	}
	private void loadPanelConfigFromFile(String path) {
		Properties panelProperties = ConfigUtils.loadProperties(path);
		if (panelProperties != null) {
			loadPanels(panelProperties);
			while (panels.size() > 0) {
				removePanelAt(0);
			}

			areaAPanelsMenu.removeAll();
			areaBPanelsMenu.removeAll();
			areaCPanelsMenu.removeAll();
			areaOthersPanelsMenu.removeAll();

			pack();
		}
	}

	private void savePanelSettingsMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		javax.swing.JFileChooser fc = new javax.swing.JFileChooser(
				new java.io.File(AgentStarter.DEFAULT_PROPERTIES_PATH));
		fc.showSaveDialog(this);
		java.io.File file = fc.getSelectedFile();
		if(file != null){
			savePanelConfigToFile(file.getPath());
		}
	}
	private void savePanelConfigToFile(String path) {
		Properties prop = new Properties();
		for (GuiPanel p : panels) {
			String panelProperties = "";
			String[] propArray = panelParameters.get(panels.indexOf(p));
			for (int i = 0; i < propArray.length; i++){
				panelProperties += propArray[i] + ",";
			}
			prop.setProperty(p.getName(), panelProperties.substring(0,
					panelProperties.length() - 1));
		}
		try {
			prop.store(new java.io.FileOutputStream(path),
					PANEL_PROPFILE_COMMENT);
		} catch (IOException ex) {
			logger.log(Level.WARNING, ex.getMessage(), 0L);
		}
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		controller.executeCommand("quitAll", null);
	}// GEN-LAST:event_exitMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenuItem addPanelMenuItem;
	private javax.swing.JMenu areaAPanelsMenu;
	private javax.swing.JMenu areaBPanelsMenu;
	private javax.swing.JMenu areaCPanelsMenu;
	private javax.swing.JMenu areaOthersPanelsMenu;
	private javax.swing.JMenuItem contentsMenuItem;
	private javax.swing.JMenuItem copyMenuItem;
	private javax.swing.JMenuItem cutMenuItem;
	private javax.swing.JMenuItem deleteMenuItem;
	private javax.swing.JMenu editMenu;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JPopupMenu.Separator jSeparator1;
	private javax.swing.JPopupMenu.Separator jSeparator2;
	private javax.swing.JPopupMenu.Separator jSeparator3;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JTabbedPane jTabbedPanelR;
	private javax.swing.JMenuItem loadPanelSettingsMenuItem;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenu panelsMenu;
	private javax.swing.JMenuItem pasteMenuItem;
	private javax.swing.JTabbedPane principalTabbedPanel;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JMenuItem savePanelSettingsMenuItem;
	// End of variables declaration//GEN-END:variables

	private void showEditPanelDialog(final String[] panelParams) {
		final AddEditPanel addEditPanel = new AddEditPanel();
		addEditPanel.setName("EditPanel");
		addEditPanel.registerAgent(agent);
		addEditPanel.registerGuiController(this.controller);
		addEditPanel.setPanelParams(panelParams);
		if (addEditDialog != null)
			addEditDialog.setVisible(false);
		addEditDialog = new javax.swing.JDialog();
		addEditDialog.add(addEditPanel.getPanel());
		addEditDialog.pack();
		addEditDialog.setVisible(true);

		final javax.swing.JPanel jpanel = addEditPanel.getPanel();
		for (java.awt.Component c : jpanel.getComponents()) {
			if (c instanceof javax.swing.JButton) {
				((javax.swing.JButton) c)
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent evt) {
								String[] params = addEditPanel.getPanelParams();
								int index = -1;
								for (int i = 0; i < panels.size(); i++) {
									String className = panels.get(i).getClass()
											.toString().replace("class ", "");
									if (panels.get(i).getName().equals(
											params[PANEL_NAME])
											&& className
													.equals(params[CLASS_NAME])) {
										index = i;
										break;
									}
								}
								if (index >= 0) {
									removePanelAt(index);
								}

								createGuiPanel(addEditPanel.getPanelParams());
								addEditDialog.setVisible(false);
							}
						});
			}
		}

		addEditPanel.refresh();
	}

	private void removePanelAt(int panelIndex) {
		removePanelAt(panelIndex, true);
	}
	private void removePanelAt(int panelIndex, boolean removeFromMenu) {
		if (removeFromMenu) {
			for (java.awt.Component firstLevelMenu : panelsMenu
					.getMenuComponents()) {
				if (firstLevelMenu instanceof javax.swing.JMenu) {
					for (java.awt.Component secondLevelMenu : ((javax.swing.JMenu) firstLevelMenu)
							.getMenuComponents()) {
						if (secondLevelMenu instanceof javax.swing.JMenu) {
							String menuText = ((javax.swing.JMenu) secondLevelMenu)
									.getText();
							String panelName = panels.get(panelIndex).getName();
							if (menuText.equals(panelName))
								((javax.swing.JMenu) firstLevelMenu)
										.remove(secondLevelMenu);
						}
					}
				}
			}
		}

		if (panelIndex > -1) {
			panelParents.get(panelIndex).remove(
					panels.get(panelIndex).getPanel());
			panelParents.get(panelIndex).repaint();
			panelParents.remove(panelIndex);
			panels.remove(panelIndex);
			panelParameters.remove(panelIndex);
		}
	}

	@Override
	public void receiveFrameworkGuiEvent(FrameworkGuiEvent event) {
		for (GuiPanel panel: panels){
			panel.refresh();
		}
		
	}

}