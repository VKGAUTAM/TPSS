/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.components;

/**
 *
 * @author VKGautam
 */
public class Point {
    private int i = -1;
    private int j = -1;

    public Point() {
//        this.i = -1;
//        this.j = -1;
    }
    
    public Point(int x, int y) {
        this.i = x;
        this.j = y;
    }
    
    /**
     * @return the i
     */
    public int getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * @return the j
     */
    public int getJ() {
        return j;
    }

    /**
     * @param j the j to set
     */
    public void setJ(int j) {
        this.j = j;
    }
    
}
