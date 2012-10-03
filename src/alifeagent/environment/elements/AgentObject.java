/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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
