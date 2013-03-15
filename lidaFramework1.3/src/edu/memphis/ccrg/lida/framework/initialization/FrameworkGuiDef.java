package edu.memphis.ccrg.lida.framework.initialization;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The overall panel configuration from either the GUI configuration XML file or the GUI panels properties file. The XML
 * extends the capabilities of the legacy panels properties file by providing more control over the presentation of the
 * GUI frame, in particular the frame bounds and its extended state.
 * 
 * @author Matthew Lohbihler
 */
public class FrameworkGuiDef {
    /**
     * Recreation of the Swing extended state values to conveniently map between names and ids.
     * 
     * @author Matthew Lohbihler
     */
    public enum ExtendedState {
        /**
         * Normal
         */
        NORMAL(0),
        /**
         * Iconified
         */
        ICONIFIED(1),
        /**
         * Maximized horizontally
         */
        MAXIMIZED_HORIZ(2),
        /**
         * Maximized vertically
         */
        MAXIMIZED_VERT(4),
        /**
         * Maximized both horizontally and vertically.
         */
        MAXIMIZED_BOTH(6);

        /**
         * The Swing-defined id for the state
         */
        public final int id;

        private ExtendedState(int id) {
            this.id = id;
        }

        /**
         * Get the extended state value for the given id.
         * 
         * @param id
         *            the Swing-defined id
         * @return the corresponding extended state value for the id
         */
        public static ExtendedState forId(int id) {
            for (ExtendedState e : values()) {
                if (e.id == id)
                    return e;
            }
            return null;
        }
    }

    private Point frameLocation;
    private Dimension frameSize;
    private ExtendedState frameExtendedState = ExtendedState.MAXIMIZED_BOTH;
    private List<FrameworkGuiPanelDef> panels = new ArrayList<FrameworkGuiPanelDef>();

    /**
     * @return the frame location
     */
    public Point getFrameLocation() {
        return frameLocation;
    }

    /**
     * @param frameLocation
     *            the frame location to set
     */
    public void setFrameLocation(Point frameLocation) {
        this.frameLocation = frameLocation;
    }

    /**
     * @return the frame size
     */
    public Dimension getFrameSize() {
        return frameSize;
    }

    /**
     * @param frameSize
     *            the frame size to set
     */
    public void setFrameSize(Dimension frameSize) {
        this.frameSize = frameSize;
    }

    /**
     * @return the frame extended state
     */
    public ExtendedState getFrameExtendedState() {
        return frameExtendedState;
    }

    /**
     * @param frameExtendedState
     *            the frame extended state to set
     */
    public void setFrameExtendedState(ExtendedState frameExtendedState) {
        this.frameExtendedState = frameExtendedState;
    }

    /**
     * @return the panel definitions
     */
    public List<FrameworkGuiPanelDef> getPanels() {
        return panels;
    }

    /**
     * @param panels
     *            the panes to set
     */
    public void setPanels(List<FrameworkGuiPanelDef> panels) {
        this.panels = panels;
    }
}
