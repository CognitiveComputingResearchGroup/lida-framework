/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ControlPanel.java
 *
 * Created on 12/07/2009, 09:26:00
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JSlider;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;
import edu.memphis.ccrg.lida.framework.gui.commands.SetTimeScaleCommand;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

/**
 * 
 * @author Javier Snaider
 */
public class SystemControlPanel extends LidaPanelImpl implements FrameworkGuiEventListener {

	private static final long serialVersionUID = 1L;

	boolean isPaused = true;
	private int sliderMin = 0;
	private int sliderMax = 50;
	private int sliderStartValue = (sliderMax - sliderMin) / 2;

	/** Creates new form ControlPanel */
	public SystemControlPanel() {
		initComponents();
		setSupportedModule(ModuleName.NoModule);

		minSleepTimeLabel.setText(sliderMin + " ms");
		maxSleepTimeLabel.setText(sliderMax + " ms");
		sleepTimeTextField.setText(this.sliderStartValue + "");
	}
	
	 /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        startPauseButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sleepTimeTextField = new javax.swing.JTextField();
        speedSlider = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        addTicksButton = new javax.swing.JButton();
        sleepTimeTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TicksEnabledCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        minSleepTimeLabel = new javax.swing.JLabel();
        maxSleepTimeLabel = new javax.swing.JLabel();

        startPauseButton.setText("Start/Pause LIDA");
        startPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPauseButtonActionPerformed(evt);
            }
        });

        quitButton.setText("Quit");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        statusLabel.setText("Paused");

        jLabel2.setText("Ticks Scale (ms)");

        sleepTimeTextField.setText("--");

        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        addTicksButton.setText("add ticks");
        addTicksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTicksButtonActionPerformed(evt);
            }
        });

        sleepTimeTextField1.setText("--");

        jLabel5.setText("Ticks:");

        TicksEnabledCheckBox.setText("Ticks Mode Enabled");
        TicksEnabledCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TicksEnabledCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sleepTimeTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTicksButton))
                    .addComponent(TicksEnabledCheckBox))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(TicksEnabledCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sleepTimeTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTicksButton)
                    .addComponent(jLabel5)))
        );

        jLabel3.setText("Task Manager Status ");

        minSleepTimeLabel.setText("Min");

        maxSleepTimeLabel.setText("Max");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(minSleepTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(7, 7, 7)
                        .addComponent(sleepTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(maxSleepTimeLabel)
                        .addGap(21, 21, 21)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startPauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(quitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(maxSleepTimeLabel)
                            .addGap(9, 9, 9))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(sleepTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minSleepTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startPauseButton)
                    .addComponent(quitButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>

	private void startPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
		isPaused = !isPaused;
		if (isPaused) {
			statusLabel.setText("PAUSED");
			controller.executeCommand("pauseRunningThreads", null);
		} else {
			statusLabel.setText("RUNNING");
			controller.executeCommand("resumeRunningThreads", null);
		}
		refresh();
	}

	private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {
		statusLabel.setText("QUITTING");
		controller.executeCommand("quitAll", null);
	}

	private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {
		JSlider source = (JSlider) evt.getSource();
		if (!source.getValueIsAdjusting()) {
			int sleepTime = (int) source.getValue();
			sleepTimeTextField.setText(sleepTime + "");

			// Another way to execute commands
			Command command = new SetTimeScaleCommand();
			command.setParameter("timeScale", sleepTime);
			controller.executeCommand(command);
			refresh();
		}
	}

	private void TicksEnabledCheckBoxActionPerformed(
			java.awt.event.ActionEvent evt) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("enable", TicksEnabledCheckBox.isSelected());
		controller.executeCommand("EnableTicksMode", parameters);
	}

	private void addTicksButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		int ticks;
		try {
			ticks = Integer.parseInt(sleepTimeTextField1.getText());
		} catch (NumberFormatException e) {
			ticks = 0;
		}
		parameters.put("ticks", ticks);
		controller.executeCommand("AddTicks", parameters);
	}

	// Variables declaration - do not modify
    private javax.swing.JCheckBox TicksEnabledCheckBox;
    private javax.swing.JButton addTicksButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel maxSleepTimeLabel;
    private javax.swing.JLabel minSleepTimeLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JTextField sleepTimeTextField;
    private javax.swing.JTextField sleepTimeTextField1;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton startPauseButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration

    
	public void refresh() {
		isPaused = lida.getTaskManager().isTasksPaused();
		if (isPaused)
			statusLabel.setText("PAUSED");
		else
			statusLabel.setText("RUNNING");
	}

	public void receiveGuiEvent(FrameworkGuiEvent event) {
	}

}// class