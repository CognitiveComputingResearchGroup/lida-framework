/*
 * @(#)ParametersFileTest.java  1.0  June 7, 2008
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.util;

/**
 *
 * @author Rodrigo Silva <rsilval@acm.org>
 */
public class ParametersFileTest {
    public static void main(String[] args) {
        ParametersFile file = new ParametersFile("num-param.txt");
        System.out.println(file.toString());
    }
}
