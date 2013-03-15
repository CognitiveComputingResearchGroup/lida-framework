package edu.memphis.ccrg.lida.framework.initialization;

/**
 * A representation of a panel configuration from either the GUI configuration XML file or the GUI panels properties
 * file. The name for this class was chosen so as not to clash with GuiPanel in the panels package.
 * 
 * @author Matthew Lohbihler
 */
public class FrameworkGuiPanelDef {
    private String name;
    private String title;
    private String className;
    private String position;
    private int tabOrder;
    private boolean refreshAfterLoad;
    private String[] options;

    /**
     * @return the panel name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the panel title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the panel class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the class name to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the panel position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position
     *            the name to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the panel tab order
     */
    public int getTabOrder() {
        return tabOrder;
    }

    /**
     * @param tabOrder
     *            the tab order to set
     */
    public void setTabOrder(int tabOrder) {
        this.tabOrder = tabOrder;
    }

    /**
     * @return the panel refresh after load
     */
    public boolean isRefreshAfterLoad() {
        return refreshAfterLoad;
    }

    /**
     * @param refreshAfterLoad
     *            the refresh after load value to set
     */
    public void setRefreshAfterLoad(boolean refreshAfterLoad) {
        this.refreshAfterLoad = refreshAfterLoad;
    }

    /**
     * @return the panel options
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * @param options
     *            the panel options to set
     */
    public void setOptions(String[] options) {
        this.options = options;
    }
}
