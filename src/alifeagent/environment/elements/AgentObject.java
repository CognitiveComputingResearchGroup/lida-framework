/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alifeagent.environment.elements;

import edu.memphis.ccrg.alife.elements.ALifeObjectImpl;

/**
 *
 * @author Javier Snaider
 */
public class AgentObject extends ALifeObjectImpl {

    @Override
    public int getIconId() {
        char dir = (Character)getAttribute("direction");
        int icon = 3;
        switch(dir){
            case 'S':
                icon = 3;
                break;
            case 'N':
                icon = 4;
                break;
            case 'E':
                icon = 5;
                break;
            case 'W':
                icon = 6;
                break;
        }
        return icon;
    }
}
