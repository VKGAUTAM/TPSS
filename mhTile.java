/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.components;

import com.arl.chips.utils.TileMoldHorizontalUtils;

/**
 *
 * @author VKGautam
 */
public final class mhTile {
    
    private int NORTH = -1;
    private int SOUTH = -1;
    private int EAST = -1;
    private int WEST = -1;
    
    private String NAME = null;
    private int tflag = -1;
    private int evtflag = -1;
    private int count = 0;

    public mhTile() {
    }

    /**
     * @return the NORTH
     */
    public int getNORTH() {
        return NORTH;
    }
    
    public mhTile(int north, int east, int south, int west) {
        this.tflag = -1;
        TileMoldHorizontalUtils tu = TileMoldHorizontalUtils.getInstance();
        mhTile tile = tu.getTile(north, east, south, west);
    }

    /**
     * @param north
     */
    public void setNORTH(int north) {
//        this.NORTH = CheckValue(north, "NORTH");
        this.NORTH = north;
    }

    /**
     * @return the SOUTH
     */
    public int getSOUTH() {
        return SOUTH;
    }

    /**
     * @param south
     */
    public void setSOUTH(int south) {
//        this.SOUTH = CheckValue(south, "SOUTH");
        this.SOUTH = south;
    }

    /**
     * @return the EAST
     */
    public int getEAST() {
        return EAST;
    }

    /**
     * @param east
     */
    public void setEAST(int east) {
//        this.EAST = CheckValue(east, "EAST");
        this.EAST = east;
    }

    /**
     * @return the WEST
     */
    public int getWEST() {
        return WEST;
    }

    /**
     * @param west
     */
    public void setWEST(int west) {
//        this.WEST = CheckValue(west, "WEST");
        this.WEST = west;
    }
    
    /**
     * @return the NAME
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * @param NAME the NAME to set
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
    
    /**
     * @return the tflag
     */
    public int getTflag() {
        return tflag;
    }

    /**
     * @param tflag the tflag to set
     */
    public void setTflag(int tflag) {
        this.tflag = tflag;
    }
    
    public int CheckValue(int value, String piname) {
        if (value == 0 || value == 1) {
            return value;
        } else {
            System.err.println("ERROR: MH TILE - Check the value of " + piname.toUpperCase().trim() + " pin!!");
//            System.exit(-1);
            return value;
        }
    }
    
    public int isValid(int value) {
        value = (value == 0 || value == 1) ? value : -1;
        return value;
    }
}
