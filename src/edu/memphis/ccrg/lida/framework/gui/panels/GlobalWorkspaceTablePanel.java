package edu.memphis.ccrg.lida.framework.gui.panels;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.panels.LidaPanelImpl;
import edu.memphis.ccrg.lida.framework.gui.panels.NodeStructureTable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;

public class GlobalWorkspaceTablePanel extends LidaPanelImpl {
	private static final Logger logger = Logger.getLogger(NodeStructureTable.class.getCanonicalName());
	//private NodeStructure nodeStructure;
	private Collection<Coalition> coalitions;
    
	/** Creates new form NodeStructureTable */
    public GlobalWorkspaceTablePanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();        

        jToolBar1.setRollover(true);

        refreshButton.setText("refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );
        
        table = new javax.swing.JTable();
       table.setModel(new NodeStructureTableModel());       
        jScrollPane1.setViewportView(table);

       /* org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
        );*/
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton refreshButton;
    // End of variables declaration
	private LidaModule module;
    
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
    	refresh();
    }//GEN-LAST:event_refreshButtonActionPerformed
    
    
	@Override
	public void initPanel(String[]param){
		ModuleName moduleType=null;
		
		if (param==null || param.length==0){
		logger.log(Level.WARNING,"Error initializing NodeStructure Panel, not enough parameters.",0L);
		return;
		}
		String[] modules = param[0].split("\\.");		
		try{
		 moduleType= ModuleName.valueOf(modules[0]);		 
		}catch (Exception e){
			logger.log(Level.WARNING,"Error initializing NodeStructure Panel, Parameter is not a ModuleType.",0L);
			return;
		}
		module = lida.getSubmodule(moduleType);
		if (module==null){
			logger.log(Level.WARNING,"Error initializing NodeStructure Panel, Module does not exist in LIDA.",0L);
			return;			
		}
				
		display(module.getModuleContent());
		
		//draw();
	}

    @Override
	public void refresh(){
    	display(module.getModuleContent());
    }
    
    @Override
	public void registerLida(Lida lida){
		super.registerLida(lida);
		
	}
	private class NodeStructureTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3902918248689475445L;
		private String[] columNames ={"Coalition","Activation","AttentionCodelet","No.of Nodes/links","Content Nodes"};
		@Override
		public int getColumnCount() {
			return columNames.length;
		}

		@Override
		public int getRowCount() {
			return coalitions.size();
		}

		@Override
		public String getColumnName(int column){
			if(column<columNames.length){
				return columNames[column];
			}
			return "";
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			//Node node=null;
			Coalition coal=null;
			if (rowIndex>coalitions.size() || columnIndex > columNames.length
					||rowIndex<0||columnIndex<0){
				return null;
			}
			//Collection<Coalition> coals = coalitions.getNodes();
			Iterator<Coalition> it = coalitions.iterator();
			for (int i=0;i<=rowIndex;i++){
				coal=it.next();
			}
			DecimalFormat df = new DecimalFormat("#.###");        
			switch(columnIndex){
			case 0:
				return coal.hashCode();
			case 1:
				return df.format(coal.getActivation());
			case 2:{ 
				return coal.getAttentionCodelet();				
				}			
			case 3:{
				String number;
				number = ((NodeStructure)coal.getContent()).getNodes().size()+" / "+((NodeStructure)coal.getContent()).getLinks().size();
				return number;
				}			
			case 4:{
				Collection<Node> nodes = ((NodeStructure)coal.getContent()).getNodes();
				String nodesString="";
				for (Node n: nodes){
					nodesString = nodesString+n.getLabel()+"; ";					
				}
				return nodesString;
				
			}
			default:
				return "";
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void display(Object o) {
		//Collections.unmodifiableCollection(coalitions)
		coalitions = (Collection<Coalition>) o;
			//nodeStructure = (NodeStructure) o;
			((AbstractTableModel) table.getModel()).fireTableStructureChanged();
			//((AbstractTableModel) table.getModel()).fireTableDataChanged();
		
	}
}
