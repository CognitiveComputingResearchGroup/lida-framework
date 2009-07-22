package edu.memphis.ccrg.lida.example.genericlida.main;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.gui.LidaGui;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiControllerImpl;

public class GenericLida {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable(){
			
	        public void run() {
	        	Lida lida = new Lida();
	        	LidaGuiController lgc = new LidaGuiControllerImpl(lida);
	            new LidaGui(lida, lgc).setVisible(true);
	        }//run
	        
	    });//invokeLater
	}//main
	
}//class
