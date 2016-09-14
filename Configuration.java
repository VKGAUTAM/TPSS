/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.conf;

import com.arl.chips.components.Tile;
import com.arl.chips.utils.TileUtils;

/**
 *
 * @author VKGautam
 */
public class Configuration {
    
    public OptionsCHIPS options;
    
    private Configuration() {
        options = new OptionsCHIPS();
        LoadDefaults(options);
    }
    
    /**
     * This will load all the default values:
     * 
     * @param options 
     */
    private void LoadDefaults(OptionsCHIPS options) {
        options.setROW_DEFAULT(4);
        options.setCOL_DEFAULT(6);
    }
    
    public static Configuration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }
    
    private static class ConfigurationHolder {

        private static final Configuration INSTANCE = new Configuration();
    }
    
    public static void main(String[] args) {
        Configuration conf = Configuration.getInstance();
        TileUtils tu = TileUtils.getInstance();
        System.out.println("GRID: " + conf.options.getGrid());
        
        //
        //   XOR Table
        //================
        // 0 XOR 0 = 0 0
        // 1 XOR 1 = 0 0
        // 0 XOR 1 = 1 1
        // 1 XOR 0 = 1 1
        //
        // North, East, South, West
        Tile tile = new Tile();
        tile = tu.getTile(1, 1, 1, 0);
        tu.PrintTile(tile);
        
        tile = tu.getMatchingTile(1, 0);
        tu.PrintTile(tile);
    }
}
