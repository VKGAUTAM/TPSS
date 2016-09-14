/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.components;

import com.arl.chips.utils.TileUtils;

/**
 *
 * @author VKGautam
 */
public final class Tile {
    
    private int TOP = -1;
    private int BOTTOM = -1;
    private int RIGHT = -1;
    private int LEFT = -1;
    
    private String NAME = null;
    private int tflag = -1;
    private int evtflag = -1;
    private int dirflag = 0;
    private int nst = 0;
    private int pinmatch = 0;
    private double kscore = 0.0f;

    public Tile() {
        this.TOP = -1;
        this.RIGHT = -1;
        this.BOTTOM = -1;
        this.LEFT = -1;
        this.NAME = null;
        this.tflag = -1;
        this.evtflag = -1;
        this.dirflag = 0;
        this.nst = 0;
        this.pinmatch = 0;
        this.kscore = 0.0f;
    }

    /**
     * @return the TOP
     */
    public int getTOP() {
        return TOP;
    }
    
    public Tile(int north, int east, int south, int west) {
        this.tflag = -1;
        TileUtils tu = TileUtils.getInstance();
        Tile tile = tu.getTile(north, east, south, west);
    }

    /**
     * @param north
     */
    public void setTOP(int north) {
        this.TOP = north;
    }

    /**
     * @return the BOTTOM
     */
    public int getBOTTOM() {
        return BOTTOM;
    }

    /**
     * @param south
     */
    public void setBOTTOM(int south) {
        this.BOTTOM = south;
    }

    /**
     * @return the RIGHT
     */
    public int getRIGHT() {
        return RIGHT;
    }

    /**
     * @param east
     */
    public void setRIGHT(int east) {
        this.RIGHT = east;
    }

    /**
     * @return the LEFT
     */
    public int getLEFT() {
        return LEFT;
    }

    /**
     * @param west
     */
    public void setLEFT(int west) {
        this.LEFT = west;
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
            System.err.println("ERROR: TILE - Check the value of " + piname.toUpperCase().trim() + " pin!!");
            System.exit(-1);
            return value;
        }
    }
    
    public int isValid(int value) {
        value = (value == 0 || value == 1) ? value : -1;
        return value;
    }

    /**
     * @return the evtflag
     */
    public int getEvtflag() {
        return evtflag;
    }

    /**
     * @param evtflag the evtflag to set
     */
    public void setEvtflag(int evtflag) {
        this.evtflag = evtflag;
    }

    /**
     * @return the pinmatch
     */
    public int getPinmatch() {
        return pinmatch;
    }

    /**
     * @param pinmatch the pinmatch to set
     */
    public void setPinmatch(int pinmatch) {
        this.pinmatch = pinmatch;
    }

    /**
     * @return the dirflag
     */
    public int getDirflag() {
        return dirflag;
    }

    /**
     * @param dirflag the dirflag to set
     */
    public void setDirflag(int dirflag) {
        this.dirflag = dirflag;
    }

    /**
     * @return the kscore
     */
    public double getKscore() {
        return kscore;
    }

    /**
     * @param kscore the kscore to set
     */
    public void setKscore(double kscore) {
        this.kscore = kscore;
    }

    /**
     * @return the nst
     */
    public int getNst() {
        return nst;
    }

    /**
     * @param nst the nst to set
     */
    public void setNst(int nst) {
        this.nst = nst;
    }
    
    @Override
    public String toString() {
        return "Tile NAME: " +this.getNAME() + ", T: " + this.TOP + ", R: " + this.RIGHT + ", B: " +  this.BOTTOM + ", L:" + this.LEFT + ", PIN MATCH:" + this.pinmatch;
    }
    
    public void clear() {
        this.TOP = -1;
        this.RIGHT = -1;
        this.BOTTOM = -1;
        this.LEFT = -1;
        this.NAME = null;
        this.tflag = -1;
        this.evtflag = -1;
        this.dirflag = 0;
        this.nst = 0;
        this.pinmatch = 0;
        this.kscore = 0.0f;
    }
}
