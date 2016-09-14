/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.conf;

/**
 *
 * @author VKGautam
 */
public class OptionsCHIPS {
    private int ROW_DEFAULT;
    private int COL_DEFAULT;
    
    /* Default VALUES */
    private double K_f = 0.0f;
    private double G_mc = 0; 
    private double G_se = 0;
    private long timestamp = 0;
    private long ctstamp = 0;
    private int MAX_ITERATIONS = 0;

    public OptionsCHIPS() {
        this.K_f = Math.pow(10, 6);
        this.G_mc = 15; /* Range: [0, 30]  */
        this.G_se = 8; /* Range: [0, 8]  */
        this.MAX_ITERATIONS = 4;
    }

    /**
     * @return the ROW_DEFAULT
     */
    public int getROW_DEFAULT() {
        return ROW_DEFAULT;
    }

    /**
     * @param ROW_DEFAULT the ROW_DEFAULT to set
     */
    public void setROW_DEFAULT(int ROW_DEFAULT) {
        this.ROW_DEFAULT = ROW_DEFAULT;
    }

    /**
     * @return the COL_DEFAULT
     */
    public int getCOL_DEFAULT() {
        return COL_DEFAULT;
    }

    /**
     * @param COL_DEFAULT the COL_DEFAULT to set
     */
    public void setCOL_DEFAULT(int COL_DEFAULT) {
        this.COL_DEFAULT = COL_DEFAULT;
    }
    
    public String getGrid() {
        return getROW_DEFAULT() + " x " + getCOL_DEFAULT();
    }
    
    public void setGrid(int m, int n) {
        this.setROW_DEFAULT(m);
        this.setCOL_DEFAULT(n);
    }

    /**
     * @return the K_f
     */
    public double getK_f() {
        return K_f;
    }

    /**
     * @param K_f the K_f to set
     */
    public void setK_f(double K_f) {
        this.K_f = K_f;
    }

    /**
     * @return the G_mc
     */
    public double getG_mc() {
        return G_mc;
    }

    /**
     * @param G_mc the G_mc to set
     */
    public void setG_mc(double G_mc) {
        this.G_mc = G_mc;
    }

    /**
     * @return the G_se
     */
    public double getG_se() {
        return G_se;
    }

    /**
     * @param G_se the G_se to set
     */
    public void setG_se(double G_se) {
        this.G_se = G_se;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the ctstamp
     */
    public long getCtstamp() {
        return ctstamp;
    }

    /**
     * @param ctstamp the ctstamp to set
     */
    public void setCtstamp(long ctstamp) {
        this.ctstamp = ctstamp;
    }

    /**
     * @return the MAX_ITERATIONS
     */
    public int getMAX_ITERATIONS() {
        return MAX_ITERATIONS;
    }

    /**
     * @param MAX_ITERATIONS the MAX_ITERATIONS to set
     */
    public void setMAX_ITERATIONS(int MAX_ITERATIONS) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
    }
    
    
}
