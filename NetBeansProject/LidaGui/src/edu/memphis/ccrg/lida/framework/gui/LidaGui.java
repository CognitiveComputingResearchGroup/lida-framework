/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LidaGui.java
 *
 * Created on 12/07/2009, 08:47:40
 */
package edu.memphis.ccrg.lida.framework.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.gui.panels.AddEditPanel;
import edu.memphis.ccrg.lida.framework.gui.panels.LidaPanel;
import edu.memphis.ccrg.lida.framework.initialization.*;

/**
 * 
 * @author Javier Snaider
 */
public class LidaGui extends javax.swing.JFrame {

    private static final String PANEL_PROPFILE_COMMENT = "name = real name, class name, Position [A,B,C,FLOAT, TOOL], Order at Position, Refresh after load?, Additional strings are used as general parameters for the panel's initPanel method";

    private static final long serialVersionUID = 100L;
    private static int PANEL_NAME = 0;
    private static int CLASS_NAME = 1;
    private static int PANEL_POSITION = 2;
    private static int TAB_ORDER = 3;
    private static int MUST_REFRESH = 4;
    private static int FIRST_PARAM = 5;
    private List<LidaPanel> panels = new ArrayList<LidaPanel>();
    private List<String[]> panelParameters = new ArrayList<String[]>();
    private List<java.awt.Container> panelParents = new ArrayList<java.awt.Container>();
    private Lida lida;
    private LidaGuiController controller;
    private static Logger logger = Logger.getLogger("lida.framework.gui.LidaGui");
    private javax.swing.JDialog addEditDialog;

    public LidaGui(Lida lida, LidaGuiController controller, Properties panelProperties) {
        initComponents();
        this.lida = lida;
        this.controller = controller;

        loadPanels(panelProperties);

        pack();
        logger.log(Level.INFO, "LidaGUI started", 0L);
    }

    /**
     * @param lida
     * @param controller
     * @param panelsFile
     */
    private void loadPanels(Properties panelProp) {
        String[][] panelsArray = new String[panelProp.size()][];
        int i = 0;
        for (Object key : panelProp.keySet()) {
            String line = panelProp.getProperty((String) key);
            String[] vals = line.split(","); // name,class,pos,tab Order,refresh[Y/N],[optional parameters],...
            if ((vals.length < FIRST_PARAM)) {
                logger.log(Level.WARNING, "Error reading line for Panel " + key, 0L);
            } else {
                panelsArray[i++] = vals;
            }
        }
        Arrays.sort(panelsArray, new Comparator<String[]>() { //sort panel by position and tab order

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
            createLidaPanel(vals);
        }
    }

    public void createLidaPanel(String[] panelParams) {
    	LidaPanel panel;

        try {
            panel = (LidaPanel) (Class.forName(panelParams[CLASS_NAME])).newInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), 0L);
            return;
        }
        panel.setName(panelParams[PANEL_NAME]);
        panel.registerLida(lida);
        panel.registrerLidaGuiController(controller);
        String[] param = new String[panelParams.length - FIRST_PARAM];
        System.arraycopy(panelParams, FIRST_PARAM, param, 0, panelParams.length - FIRST_PARAM);
        panel.initPanel(param);
        panelParameters.add(panelParams);
        addLidaPanel(panel, panelParams[PANEL_POSITION]);
        if (panelParams[MUST_REFRESH].equalsIgnoreCase("Y")) {
            panel.refresh();
        }
    }

    private void loadPanelConfigFromFile(String path) {
        while (panels.size() > 0) removePanelAt(0);
        areaAPanelsMenu.removeAll();
        areaBPanelsMenu.removeAll();
        areaCPanelsMenu.removeAll();
        areaOthersPanelsMenu.removeAll();
        
        Properties panelProperties = ConfigUtils.loadProperties(path);


        loadPanels(panelProperties);
        pack();
    }

    private void savePanelConfigToFile(String path) {
        Properties prop = new Properties();
        for (LidaPanel p : panels) {
            String panelProperties = "";
            String[] propArray = panelParameters.get(panels.indexOf(p));
            for (int i = 0; i < propArray.length; i++)
                panelProperties += propArray[i]+",";
            prop.setProperty(p.getName(), panelProperties.substring(0, panelProperties.length() - 1));
        }
        try {
            prop.store(new java.io.FileOutputStream(path), PANEL_PROPFILE_COMMENT);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), 0L);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPanelR = new javax.swing.JTabbedPane();
        principalTabbedPanel = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
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

        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        panelsMenu.setText("Panels");

        addPanelMenuItem.setText("Add new panel");
        addPanelMenuItem.addActionListener(new java.awt.event.ActionListener() {
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
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPanelSettingsMenuItemActionPerformed(evt);
            }
        });
        panelsMenu.add(loadPanelSettingsMenuItem);

        savePanelSettingsMenuItem.setText("Save panel settings");
        savePanelSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePanelSettingsMenuItemActionPerformed(evt);
            }
        });
        panelsMenu.add(savePanelSettingsMenuItem);
        panelsMenu.add(jSeparator2);

        menuBar.add(panelsMenu);

        helpMenu.setText("Help");

        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void addPanelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPanelMenuItemActionPerformed
    	showAddPanelDialog();
    }//GEN-LAST:event_addPanelMenuItemActionPerformed

    private void loadPanelSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPanelSettingsMenuItemActionPerformed
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser(new java.io.File(LidaStarter.DEFAULT_LIDA_PROPERTIES_PATH));
        fc.showOpenDialog(this);
        java.io.File file = fc.getSelectedFile();
        loadPanelConfigFromFile(file.getPath());
    }//GEN-LAST:event_loadPanelSettingsMenuItemActionPerformed

    private void savePanelSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePanelSettingsMenuItemActionPerformed
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser(new java.io.File(LidaStarter.DEFAULT_LIDA_PROPERTIES_PATH));
        fc.showSaveDialog(this);
        java.io.File file = fc.getSelectedFile();
        savePanelConfigToFile(file.getPath());
    }//GEN-LAST:event_savePanelSettingsMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        //System.exit(0);
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

    /**
     * Adds a Panel to the main GUI.
     *
     * @param panel
     *            the panel to add.
     * @param panelPosition
     *            Determines where the panel is going to be placed. </br>
     *            A: Upper Left position. </br>
     *            B: Upper Right position. In a new TAB</br>
     *            C: Lower position. In a new TAB</br>
     *			  TOOL: In the ToolBox </br>
     *            FLOAT: In a new frame. </br>
     */
    private void addLidaPanel(LidaPanel panel, String panelPosition) {
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
        } else if ("TOOL".equalsIgnoreCase(panelPosition)) {
            getContentPane().add(jPanel, java.awt.BorderLayout.PAGE_START);
            parent = getContentPane();
        } else {
            logger.log(Level.WARNING, "Position error for panel " + panel.getName()
                    + " pos:" + panelPosition, 0L);
        }

        panelParents.add(parent);

        addToPanelsMenu(panel, parent, associatedMenu);
    }

    private void addToPanelsMenu(final LidaPanel panel, final java.awt.Container parent, javax.swing.JMenu associatedMenu) {
        final JPanel jPanel = panel.getPanel();
        javax.swing.JMenu cMenu;
        String menuItemLabel;
        int unnamedIndex = 0;

        menuItemLabel = panel.getName();
        if (menuItemLabel.equals("")) {
            menuItemLabel = "Unnamed" + (unnamedIndex++);
        }
        cMenu = new javax.swing.JMenu();
        cMenu.setText(menuItemLabel);

        javax.swing.JCheckBoxMenuItem showItem = new javax.swing.JCheckBoxMenuItem();
        showItem.setText("Show Panel");
        showItem.setSelected(true);
        showItem.addActionListener(new java.awt.event.ActionListener() {
            private LidaPanel cLidaPanel = panel;
            private JPanel cjPanel = jPanel;
            private java.awt.Container cParent = parent;
            private String[] cParameters = panelParameters.get(panels.indexOf(panel));
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int index = -1;
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
                    removePanelAt(index, false);
                } else {
                    cParent.add(cjPanel);
                    panels.add(cLidaPanel);
                    panelParents.add(cParent);
                    panelParameters.add(cParameters);
                }
            }
        });
        cMenu.add(showItem);
        cMenu.add(new javax.swing.JPopupMenu.Separator());
        javax.swing.JMenuItem editItem = new javax.swing.JMenuItem();
        editItem.setText("Edit Panel");
        final String[] panelParams = panelParameters.get(panels.indexOf(panel));
        editItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEditPanelDialog(panelParams);
            }
        });
        cMenu.add(editItem);

        associatedMenu.add(cMenu);
    }

    private void showAddPanelDialog() {
        final AddEditPanel addEditPanel = new AddEditPanel();
        addEditPanel.setName("AddPanel");
        addEditPanel.registerLida(lida);
        addEditPanel.registrerLidaGuiController(this.controller);
        if (addEditDialog != null) addEditDialog.setVisible(false);
        addEditDialog = new javax.swing.JDialog();
        addEditDialog.add(addEditPanel.getPanel());
        addEditDialog.pack();
        addEditDialog.setVisible(true);

        final javax.swing.JPanel jpanel = addEditPanel.getPanel();
        for (java.awt.Component c : jpanel.getComponents()) {
            if (c instanceof javax.swing.JButton) {
                ((javax.swing.JButton) c).addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(
                            java.awt.event.ActionEvent evt) {
                        createLidaPanel(addEditPanel.getPanelParams());
                        addEditDialog.setVisible(false);
                    }
                });
            }
        }

        addEditPanel.refresh();
    }

    private void showEditPanelDialog(final String[] panelParams) {
        final AddEditPanel addEditPanel = new AddEditPanel();
        addEditPanel.setName("EditPanel");
        addEditPanel.registerLida(lida);
        addEditPanel.registrerLidaGuiController(this.controller);
        addEditPanel.setPanelParams(panelParams);
        if (addEditDialog != null) addEditDialog.setVisible(false);
        addEditDialog = new javax.swing.JDialog();
        addEditDialog.add(addEditPanel.getPanel());
        addEditDialog.pack();
        addEditDialog.setVisible(true);

        final javax.swing.JPanel jpanel = addEditPanel.getPanel();
        for (java.awt.Component c : jpanel.getComponents()) {
            if (c instanceof javax.swing.JButton) {
                ((javax.swing.JButton) c).addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(
                            java.awt.event.ActionEvent evt) {
                        String[] params = addEditPanel.getPanelParams();
                        int index = -1;
                        for (int i = 0; i < panels.size(); i++) {
                            String className = panels.get(i).getClass().toString().replace("class ", "");
                            if (panels.get(i).getName().equals(params[PANEL_NAME])
                                    && className.equals(params[CLASS_NAME])) {
                                index = i;
                                break;
                            }
                        }   
                        if (index >= 0) {
                            removePanelAt(index);
                        }

                        createLidaPanel(addEditPanel.getPanelParams());
                        addEditDialog.setVisible(false);
                    }
                });
            }
        }

        addEditPanel.refresh();
    }

    private void removePanelAt(int panelIndex, boolean removeFromMenu) {
        if (removeFromMenu) {
            for (java.awt.Component firstLevelMenu : panelsMenu.getMenuComponents()) {
                if (firstLevelMenu instanceof javax.swing.JMenu) {
                    for (java.awt.Component secondLevelMenu : ((javax.swing.JMenu)firstLevelMenu).getMenuComponents()) {
                        if (secondLevelMenu instanceof javax.swing.JMenu) {
                            String menuText = ((javax.swing.JMenu)secondLevelMenu).getText();
                            String panelName = panels.get(panelIndex).getName();
                            if (menuText.equals(panelName))
                                ((javax.swing.JMenu)firstLevelMenu).remove(secondLevelMenu);
                        }
                    }
                }
            }
        }

        panelParents.get(panelIndex).remove(panels.get(panelIndex).getPanel());
        panelParents.remove(panelIndex);
        panels.remove(panelIndex);
        panelParameters.remove(panelIndex);
    }

    private void removePanelAt(int panelIndex) {
        removePanelAt(panelIndex, true);
    }


    public Collection<LidaPanel> getPanels() {
        return Collections.unmodifiableCollection(panels);
    }
}