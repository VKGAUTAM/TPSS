/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.replicate;

import com.arl.chips.components.Point;
import com.arl.chips.components.Tile;
import com.arl.chips.components.mhTile;
import com.arl.chips.components.mvTile;
import com.arl.chips.conf.Configuration;
import com.arl.chips.utils.PrintUtils;
import com.arl.chips.utils.SortUtils;
import com.arl.chips.utils.TileMoldHorizontalUtils;
import com.arl.chips.utils.TileMoldVerticalUtils;
import com.arl.chips.utils.TileUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author VKGautam
 */
public class PerformReplication {

    Configuration conf;

    public PerformReplication() {
        conf = Configuration.getInstance();
    }

    public PerformReplication(Configuration conf) {
        conf = Configuration.getInstance();
    }

    public Map<Integer, Map<Integer, Tile>> getPattern() {
        conf = Configuration.getInstance();
        return getPattern(conf);
    }

    public Map<Integer, Map<Integer, Tile>> getPattern(int row, int column) {
        conf = Configuration.getInstance();
        conf.options.setROW_DEFAULT(row);
        conf.options.setCOL_DEFAULT(column);
        return getPattern(conf);
    }

    public Map<Integer, Map<Integer, Tile>> getPattern(Configuration conf) {
        int row = conf.options.getROW_DEFAULT();
        int column = conf.options.getCOL_DEFAULT();

        Map<Integer, Map<Integer, Tile>> mapPAT = new HashMap<>();
        try {
            // Initialize the GRID
            for (int i = 0; i < row + 1; i++) {
                Map<Integer, Tile> colGrid = new HashMap<>();
                for (int j = 0; j < column + 1; j++) {
                    Tile ctile = new Tile();
                    colGrid.put(j, ctile);
                }
                mapPAT.put(i, colGrid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR in initializing the PATTERN !!" + e.getMessage());
        }
        return mapPAT;
    }

    public Map<Integer, Map<Integer, Tile>> getSeedTile(Map<Integer, Map<Integer, Tile>> mapPAT) {
        TileUtils tu = TileUtils.getInstance();
        try {
            // Initialize DEFAULT_CONFIG_PANEL: Get the first element at the bottom left of the grid and then
            String seedTileName = tu.getTileName();
            Tile seedTile = tu.getMatchingTileByName(seedTileName);

            Map<Integer, Tile> mapBlTile = mapPAT.get((conf.options.getROW_DEFAULT() - 1));
            mapBlTile.put(1, seedTile);
            mapPAT.put((conf.options.getROW_DEFAULT() - 1), mapBlTile);
        } catch (Exception e) {
            System.out.println("ERROR in Generating the SEED TILE !!" + e.getMessage());
        }
        return mapPAT;
    }
    
    public Map<Integer, Map<Integer, Tile>> getInitialPattern(Configuration conf) {
        
        Map<Integer, Map<Integer, Tile>> mapPATTERN = new HashMap<>();
        try {
            /* Create PATTERN */
            mapPATTERN = getPattern(conf);
            PrintUtils.getInstance().printPATTERN(mapPATTERN, 3, 1);

            /* Create SEED layer */
            mapPATTERN = createSeedLayer(mapPATTERN);
            PrintUtils.getInstance().printPATTERN(mapPATTERN, 1, 1);
            
            long timestamp = 0;

//            /* Fill the remaining TILEs */
            mapPATTERN = fillInitialMoldToPatternLayer(mapPATTERN, timestamp);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapPATTERN;
    }
    
    public Map<Integer, Map<Integer, Tile>> fillMoldFromSeedLayer(Map<Integer, Map<Integer, Tile>> myPattern) {
        conf = Configuration.getInstance();
        TileUtils tu = TileUtils.getInstance();
        TileMoldVerticalUtils tmvu  = TileMoldVerticalUtils.getInstance();
        TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();
        
        String seedTileName = tu.getTileName();
        Tile seedTile = tu.getMatchingTileByName(seedTileName);

        Map<Integer, Tile> mapBlTile = myPattern.get((conf.options.getROW_DEFAULT()));
        mapBlTile.put(0, seedTile);
        myPattern.put(conf.options.getROW_DEFAULT(), mapBlTile);
//        System.out.println("Mold PIVOT TILE: " + seedTile.toString());
        
        try {
            // Get Seed Tile
            Map<Integer, Tile> mapSeedTile = myPattern.get(conf.options.getROW_DEFAULT());
            seedTile = mapSeedTile.get(0);
            Tile left = new Tile();
            tu.CopyToTile(seedTile, left);

            // Now build other elements of the DEFAULT_CONFIG_PANEL
            for (int i = (conf.options.getROW_DEFAULT() - 1); i >= 0; i--) {
                int j = 0;
                Tile ctile = myPattern.get(i).get(j);
                
                Tile rtile = myPattern.get(i).get(j+1);
                Tile btile = myPattern.get(i+1).get(j);
                
                int rightpin = rtile.getLEFT();
                int bottompin = btile.getTOP();
                
                mvTile current = new mvTile();
                current = tmvu.getMatchingTile(rightpin, bottompin);
//                System.out.println("TILE: " + current.toString());
                tu.CopyMvTileToTile(current, left);
                
                Tile uptile = new Tile();
                tu.CopyMvTileToTile(current, uptile);

                Map<Integer, Tile> mapCP = myPattern.get(i);
                mapCP.put(j, uptile);
                myPattern.put(i, mapCP);
            }

            for (int j = 1; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                int i = conf.options.getROW_DEFAULT();
                Tile ctile = myPattern.get(i).get(j);

                Tile ltile = myPattern.get(i).get(j-1);
                Tile ttile = myPattern.get(i-1).get(j);
                
                int leftpin = ltile.getRIGHT();
                int toppin = ttile.getBOTTOM();

                mhTile current = new mhTile();
                current = tmhu.getMatchingTile(leftpin, toppin);
//                System.out.println("TILE: " + current.toString());
                tu.CopyMhTileToTile(current, left);
                

                Tile uptile = new Tile();
                tu.CopyMhTileToTile(current, uptile);

                Map<Integer, Tile> mapCP = myPattern.get(i);
                mapCP.put(j, uptile);
                myPattern.put(i, mapCP);
            }

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("ERROR in Creating the SEED LAYER !!" + e.getMessage());
        }
        
        return myPattern;
    }
    
    public Map<Integer, Map<Integer, Tile>> fillInitialPatternFromSeedLayer(Map<Integer, Map<Integer, Tile>> myPattern) {
        conf = Configuration.getInstance();
        TileUtils tu = TileUtils.getInstance();
        TileMoldVerticalUtils tmvu  = TileMoldVerticalUtils.getInstance();
        TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();
        
//        Tile seedTile = myPattern.get((conf.options.getROW_DEFAULT() - 1)).get(1);
//         = tu.getMatchingTileByName(seedTileName);

//        Map<Integer, Tile> mapBlTile = myPattern.get((conf.options.getROW_DEFAULT() - 1));
//        mapBlTile.put(1, seedTile);
//        myPattern.put(conf.options.getROW_DEFAULT() - 1, mapBlTile);
//        System.out.println("Mold PIVOT TILE: " + seedTile.toString());
        
        try {
            // Get Seed Tile
            Map<Integer, Tile> mapSeedTile = myPattern.get(conf.options.getROW_DEFAULT() - 1);
            Tile seedTile = mapSeedTile.get(1);
            Tile left = new Tile();
            tu.CopyToTile(seedTile, left);

            // Now build other elements of the DEFAULT_CONFIG_PANEL
            for (int i = (conf.options.getROW_DEFAULT() - 1); i >= 0; i--) {
                int j = 1;
                Tile ctile = myPattern.get(i).get(j);
                
                Tile rtile = myPattern.get(i).get(j+1);
                Tile btile = myPattern.get(i+1).get(j);
                
                int rightpin = rtile.getLEFT();
                int bottompin = btile.getTOP();
                
                mvTile current = new mvTile();
                current = tmvu.getMatchingTile(rightpin, bottompin);
//                System.out.println("TILE: " + current.toString());
                tu.CopyMvTileToTile(current, left);
                
                Tile uptile = new Tile();
                tu.CopyMvTileToTile(current, uptile);

                Map<Integer, Tile> mapCP = myPattern.get(i);
                mapCP.put(j, uptile);
                myPattern.put(i, mapCP);
            }

            for (int j = 2; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                int i = conf.options.getROW_DEFAULT() - 1;
                Tile ctile = myPattern.get(i).get(j);

                Tile ltile = myPattern.get(i).get(j-1);
                Tile ttile = myPattern.get(i-1).get(j);
                
                int leftpin = ltile.getRIGHT();
                int toppin = ttile.getBOTTOM();

                mhTile current = new mhTile();
                current = tmhu.getMatchingTile(leftpin, toppin);
//                System.out.println("TILE: " + current.toString());
                tu.CopyMhTileToTile(current, left);
                

                Tile uptile = new Tile();
                tu.CopyMhTileToTile(current, uptile);

                Map<Integer, Tile> mapCP = myPattern.get(i);
                mapCP.put(j, uptile);
                myPattern.put(i, mapCP);
            }

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("ERROR in Creating the SEED LAYER !!" + e.getMessage());
        }
        
        return myPattern;
    }
    
    public Map<Integer, Map<Integer, Tile>> createSeedLayer(Map<Integer, Map<Integer, Tile>> mapPAT) {
        conf = Configuration.getInstance();
        mapPAT = getSeedTile(mapPAT);
        TileUtils tu = TileUtils.getInstance();
        try {
            // Get Seed Tile
            Map<Integer, Tile> mapSeedTile = mapPAT.get((conf.options.getROW_DEFAULT() - 1));
            Tile seedTile = mapSeedTile.get(1);
            Tile prev = new Tile();
            tu.CopyToTile(seedTile, prev);

            // Now build other elements of the DEFAULT_CONFIG_PANEL
            for (int i = (conf.options.getROW_DEFAULT() - 2); i >= 0; i--) {
                int j = 1;
                Tile ctile = mapPAT.get(i).get(j);
                int toppin = prev.getTOP();
                String topname = prev.getNAME();

                Tile current = tu.getSinglePinMatchingTile(-1, toppin);
                tu.CopyToTile(current, prev);

                Map<Integer, Tile> mapCP = mapPAT.get(i);
                mapCP.put(j, current);
                mapPAT.put(i, mapCP);
            }

            tu.CopyToTile(seedTile, prev);
            int i = conf.options.getROW_DEFAULT() - 1;
            for (int j = 2; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                Tile ctile = mapPAT.get(i).get(j);

                int leftpin = prev.getRIGHT();
                String topname = prev.getNAME();

                Tile current = tu.getSinglePinMatchingTile(leftpin, -1);
                tu.CopyToTile(current, prev);

                Map<Integer, Tile> mapCP = mapPAT.get(i);
                mapCP.put(j, current);
                mapPAT.put(i, mapCP);
            }

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("ERROR in Creating the SEED LAYER !!" + e.getMessage());
        }
        return mapPAT;
    }

    public Map<Integer, Map<Integer, Tile>> performPatternFilling(Map<Integer, Map<Integer, Tile>> mapPAT) {
        TileUtils tu = TileUtils.getInstance();
        try {
            Map<Integer, Tile> mapSeedTile = new HashMap<>();
            mapSeedTile = mapPAT.get((conf.options.getROW_DEFAULT() - 1));
            Tile seedTile = mapSeedTile.get(1);
            Tile prev = new Tile();
            tu.CopyToTile(seedTile, prev);

            // Fill the remaining TILEs
            for (int i = (conf.options.getROW_DEFAULT() - 2); i >= 0; i--) {
                for (int j = 2; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                    Tile ctile = mapPAT.get(i).get(j);

                    Tile left = new Tile();
                    left = mapPAT.get(i).get(j - 1);
                    int leftpin = left.getRIGHT();
                    String topname = left.getNAME();

                    Tile bott = mapPAT.get(i + 1).get(j);
                    int bottompin = bott.getTOP();
                    String bottomname = bott.getNAME();

                    Tile current = new Tile();
                    current = tu.getMatchingTile(leftpin, bottompin);
                    tu.CopyToTile(current, prev);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    mapCP.put(j, current);
                    mapPAT.put(i, mapCP);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("ERROR in Creating the SEED LAYER !!" + e.getMessage());
        }
        return mapPAT;
    }

    public Map<Integer, Map<Integer, Tile>> fillInitialMoldToPatternLayer(Map<Integer, Map<Integer, Tile>> mapPAT, long ctstamp) {
        TileUtils tu = TileUtils.getInstance();
        try {
            Map<Integer, Tile> mapSeedTile = mapPAT.get(conf.options.getROW_DEFAULT() - 1);
            Tile seedTile = mapSeedTile.get(1);
            Tile prev = new Tile();
            tu.CopyToTile(seedTile, prev);

            // Fill the remaining TILEs
            TileMoldVerticalUtils tmvu = TileMoldVerticalUtils.getInstance();
            TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();
            int prow = conf.options.getROW_DEFAULT() - 1;

            // Create MOLD PIVOT Tile
            mvTile mvtile = new mvTile();
            Tile moldPivotTile = new Tile();
            String moldPivotName;
            Map<Integer, Tile> mapMoldPivot = new HashMap<>();
            mapMoldPivot = mapPAT.get(prow);
            if (mapMoldPivot.get(1).getNAME() == null) {
                System.out.println("Pivot Tile is NULL ! Randomly Generating the MOLD PIVOT TILE!!");
                moldPivotName = tmvu.getTileName();
                mvtile = tmvu.getMatchingTileByName(moldPivotName);
                tmvu.CopyMvtileToTile(mvtile, moldPivotTile);
//                System.out.println("\nNew MOLD PIVOT NAME: " + mvtile.getNAME());
            } else {
                moldPivotTile = mapPAT.get(prow).get(1);
//                System.out.println("Existing MOLD PIVOT NAME: " + moldPivotTile.getNAME());
            }
            mapMoldPivot.put(1, moldPivotTile);
            mapPAT.put(prow, mapMoldPivot);
//            System.out.println("i = " + prow + ", j = 0\n--------------------------");

            int col = 2;
            int row = conf.options.getROW_DEFAULT() - 1;
            int maxrow = row;
            int maxcol = conf.options.getCOL_DEFAULT();

            int i = 0, j = 0;
            int start = 0;
            while (row >= 0) {
                start = row;
                j = col;
                i = row;
                
                boolean tsflag = true;

//                System.out.println("\n-----------------");
                for (i = row; (i < maxrow) && (j <= maxcol); i++) {
//                    System.out.println("( " + i + ", " + j + " )");

                    Tile ctile = mapPAT.get(i).get(j);

                    Tile left = mapPAT.get(i).get(j - 1);
                    int rightpin = left.getRIGHT();

                    Tile bott = mapPAT.get(i + 1).get(j);
                    int toppin = bott.getTOP();

                    Tile current = new Tile();
                    current = tu.getMatchingTile(rightpin, toppin);

                    tu.CopyToTile(current, prev);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    mapCP.put(j, current);
                    mapPAT.put(i, mapCP);

                    j++;
                    
                    if (tsflag) {
                        ctstamp += 1;
//                        conf.options.setTimestamp(timestamp);
                        tsflag = false;
                        
//                        System.out.println("TIME STAMP: " + ctstamp);
                    }
                }

//                System.out.println("\n--------------------------");
//                PrintUtils.getInstance().printPATTERN(mapPAT, 3, 1);
//                System.out.println("\n--------------------------");
                row = start - 1;
            }

            row = 0;
            col++;
            while (col < maxcol + 1) {
                start = row;
                j = col;
                i = row;
                boolean tsflag = true;
                
//                System.out.println("\n-----------------");
                for (i = row; (i < maxrow) && (j <= maxcol); i++) {
//                    System.out.println("( " + i + ", " + j + " )");
                    
                    Tile ctile = mapPAT.get(i).get(j);

                    Tile left = new Tile();
                    left = mapPAT.get(i).get(j - 1);
                    int leftpin = left.getRIGHT();

                    Tile bott = mapPAT.get(i + 1).get(j);
                    int bottompin = bott.getTOP();

                    Tile current = new Tile();
                    current = tu.getMatchingTile(leftpin, bottompin);

                    tu.CopyToTile(current, prev);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    mapCP.put(j, current);
                    mapPAT.put(i, mapCP);

                    j++;
                    
                    if (tsflag) {
                        ctstamp += 1;
//                        conf.options.setTimestamp(timestamp);
                        tsflag = false;
                        
//                        System.out.println("TIME STAMP: " + ctstamp);
                    }
                }

//                System.out.println("\n--------------------------");
//                PrintUtils.getInstance().printPATTERN(mapPAT, 3, 1);
//                System.out.println("\n--------------------------");
                col++;
            }
            
            conf.options.setCtstamp(ctstamp);

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("ERROR in Creating the SEED LAYER !!" + e.getMessage());
        }
        return mapPAT;
    }

    public Map<Integer, Map<Integer, Tile>> fillInitialMoldToFullPatternLayer(Map<Integer, Map<Integer, Tile>> mapPAT, long ctstamp) {
        TileUtils tu = TileUtils.getInstance();
        try {
//            long timestamp = conf.options.getTimestamp();
            int prow = conf.options.getROW_DEFAULT();
//            System.out.println("ROW: " + prow);

            Map<Integer, Tile> mapSeedTile = mapPAT.get(prow);
            Tile seedTile = mapSeedTile.get(0);
            Tile prev = new Tile();
            tu.CopyToTile(seedTile, prev);

            // Fill the remaining TILEs
            TileMoldVerticalUtils tmvu = TileMoldVerticalUtils.getInstance();
            TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();

            // Create MOLD PIVOT Tile
            mvTile mvtile = new mvTile();
            Tile moldPivotTile = new Tile();
            String moldPivotName;
            Map<Integer, Tile> mapMoldPivot = new HashMap<>();
            mapMoldPivot = mapPAT.get(prow);
            if (mapMoldPivot.get(0).getNAME() == null) {
                System.out.println("Pivot Tile is NULL ! Randomly Generating the MOLD PIVOT TILE!!");
                moldPivotName = tmvu.getTileName();
                mvtile = tmvu.getMatchingTileByName(moldPivotName);
                tmvu.CopyMvtileToTile(mvtile, moldPivotTile);
//                System.out.println("\nNew MOLD PIVOT NAME: " + mvtile.getNAME());
            } else {
                moldPivotTile = mapPAT.get(prow).get(0);
//                System.out.println("Existing MOLD PIVOT NAME: " + moldPivotTile.getNAME());
            }
            mapMoldPivot.put(0, moldPivotTile);
            mapPAT.put(prow, mapMoldPivot);
//            System.out.println("i = " + prow + ", j = 0\n--------------------------");

            int col = 1;
            int row = conf.options.getROW_DEFAULT();
            int maxrow = row;
            int maxcol = conf.options.getCOL_DEFAULT();

            int i = 0, j = 0;
            int start = 0;
            while (row >= 0) {
                start = row;
                j = col;
                i = row;
                
                boolean tsflag = true;

//                System.out.println("\n--------------------------");
                for (i = row; (i < maxrow) && (j <= maxcol); i++) {
//                    System.out.println(" i = " + i + ", j = " + j);

                    Tile ctile = mapPAT.get(i).get(j);

                    Tile left = new Tile();
                    left = mapPAT.get(i).get(j - 1);
                    int leftpin = left.getRIGHT();
                    String topname = left.getNAME();

                    Tile bott = mapPAT.get(i + 1).get(j);
                    int bottompin = bott.getTOP();
                    String bottomname = bott.getNAME();

                    Tile current = new Tile();
                    current = tu.getMatchingTile(leftpin, bottompin);

                    tu.CopyToTile(current, prev);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    mapCP.put(j, current);
                    mapPAT.put(i, mapCP);

                    j++;

                    if (tsflag) {
                        ctstamp += 1;
                        tsflag = false;

                        System.out.println("CTIME STAMP: " + ctstamp);
                    }
                }

                PrintUtils.getInstance().printPATTERN(mapPAT, 3, 1);
//                System.out.println("\n--------------------------");

                row = start - 1;
            }

            row = 0;
            col++;
            while (col < maxcol + 1) {
                start = row;
                j = col;
                i = row;

                boolean tsflag = true;
                
//                System.out.println("\n--------------------------");
                for (i = row; (i < maxrow) && (j <= maxcol); i++) {
//                    System.out.println(" i = " + i + ", j = " + j);

                    Tile ctile = mapPAT.get(i).get(j);

                    Tile left = new Tile();
                    left = mapPAT.get(i).get(j - 1);
                    int leftpin = left.getRIGHT();
                    String topname = left.getNAME();

                    Tile bott = mapPAT.get(i + 1).get(j);
                    int bottompin = bott.getTOP();
                    String bottomname = bott.getNAME();

                    Tile current = new Tile();
                    current = tu.getMatchingTile(leftpin, bottompin);

                    tu.CopyToTile(current, prev);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    mapCP.put(j, current);
                    mapPAT.put(i, mapCP);

                    j++;
                    
                    if (tsflag) {
                        ctstamp += 1;
                        tsflag = false;

                        System.out.println("CTIME STAMP: " + ctstamp);
                    }
                }

                System.out.println("\n--------------------------");
                PrintUtils.getInstance().printPATTERN(mapPAT, 3, 1);
//                System.out.println("\n--------------------------");

                col++;
            }

            conf.options.setCtstamp(ctstamp);

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("ERROR in Creating the SEED LAYER !!" + e.getMessage());
        }
        return mapPAT;
    }

    public Map<Integer, Map<Integer, Tile>> createMoldLayer(Map<Integer, Map<Integer, Tile>> mapPAT) {
        conf = Configuration.getInstance();
        return createMoldLayer(mapPAT, conf);
    }

    public Map<Integer, Map<Integer, Tile>> createMoldLayer(Map<Integer, Map<Integer, Tile>> mapPAT, Configuration conf) {
        System.out.println("WIDTH: " + conf.options.getROW_DEFAULT() + ", HEIGHT: " + conf.options.getCOL_DEFAULT());
        System.out.println("MAP: " + mapPAT.keySet().size());// + ", WIDTH: " + mapPAT.get(0).size());
        TileUtils tu = TileUtils.getInstance();
        TileMoldVerticalUtils tmvu = TileMoldVerticalUtils.getInstance();
        TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();
        try {
            // Now we start building the MOLD LAYER - First we generate MOlD SEED TILE            
            String moldPivotName = tmvu.getTileName();
            mvTile moldPivotTile = tmvu.getMatchingTileByName(moldPivotName);
            Map<Integer, Tile> mapMoldPivot = mapPAT.get(conf.options.getROW_DEFAULT());

            if (mapMoldPivot == null) {
                System.err.println("ERROR ... NULL MOLD ... !!");
            } else {
                Tile ctile = mapMoldPivot.get(0);
                mvTile mvt = new mvTile();
                tmvu.CopyTileToMvtile(ctile, mvt);

                tmvu.CopyMvtileToTile(moldPivotTile, ctile);
                mapMoldPivot.put(0, ctile);
                mapPAT.put(conf.options.getROW_DEFAULT(), mapMoldPivot);

                // Now we start building the MOLD LAYER - Vertical
                mvTile mvPrev = new mvTile();
                tmvu.CopyToTile(moldPivotTile, mvPrev);
                for (int i = conf.options.getROW_DEFAULT() - 1; i >= 0; i--) {
                    int j = 0;
                    Tile rtile = mapPAT.get(i).get(j + 1);
                    int toppin = mvPrev.getNORTH();
                    int leftpin = rtile.getLEFT();

                    mvTile current = new mvTile();
                    current = tmvu.getMatchingTile(leftpin, toppin);
                    tmvu.CopyToTile(current, mvPrev);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    Tile replacetile = mapCP.get(j);
                    tmvu.CopyMvtileToTile(current, replacetile);
                    mapCP.put(j, replacetile);
                    mapPAT.put(i, mapCP);
                }

                // Now we start building the MOLD LAYER - Horizontal
                int i = conf.options.getROW_DEFAULT();
                mvTile leftTile = new mvTile();
                tmvu.CopyToTile(moldPivotTile, leftTile);

                for (int j = 1; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                    Tile topTile = mapPAT.get(i - 1).get(j);
                    int toppin = topTile.getBOTTOM();

                    int leftpin = leftTile.getEAST();

                    mhTile current = new mhTile();
                    current = tmhu.getSinglePinMatchingTile(leftpin, toppin);

                    Tile reptile = new Tile();
                    tmhu.CopyMhtileToTile(current, reptile);

                    Map<Integer, Tile> mapCP = mapPAT.get(i);
                    mapCP.put(j, reptile);
                    mapPAT.put(i, mapCP);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR in Creating the MOLD LAYER !!" + e.getMessage());
        }
        return mapPAT;
    }

    public Map<Integer, Map<Integer, Tile>> initializeMoldLayer(Map<Integer, Map<Integer, Tile>> mapMLayer) {
        conf = Configuration.getInstance();
        int j = 0;
        for (int i = 0; i < (conf.options.getROW_DEFAULT() + 1); i++) {
//            System.out.println("i = " + i + ", j = " + j);
            Tile ctile = new Tile();
            Map<Integer, Tile> mapLAY = new HashMap<>();
            mapLAY.put(j, ctile);
            if (i == conf.options.getROW_DEFAULT()) {
                for (j = 1; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                    Tile rtile = new Tile();
                    mapLAY.put(j, rtile);
//                    System.out.println("i = " + i + ", j = " + j);
                }
            }
            mapMLayer.put(i, mapLAY);
        }
//        System.out.println("\n----------------------");
        return mapMLayer;
    }

    public Map<Integer, Map<Integer, Tile>> copySeedLayerToIntialMold(Map<Integer, Map<Integer, Tile>> mapPAT,
            Map<Integer, Map<Integer, Tile>> mapML) {

        conf = Configuration.getInstance();

        for (int i = 0; i < conf.options.getROW_DEFAULT(); i++) {
            for (int j = 0; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                Map<Integer, Tile> mapLAY = mapML.get(i);
                if (j != 0) {
                    Tile ctile = mapPAT.get(i).get(j);
                    mapLAY.put(j, ctile);
                    mapML.put(i, mapLAY);
                }
            }
        }

        return mapML;
    }

    public Map<Integer, Map<Integer, Tile>> fillPatternToMold(Map<Integer, Map<Integer, Tile>> mapPAT) {
        boolean printflag = false;
        long timestamp = 0;
        return fillPatternToMold(mapPAT, printflag, timestamp);
    }
    
    public Map<Integer, Map<Integer, Tile>> fillPatternToMold(Map<Integer, Map<Integer, Tile>> mapPAT, boolean printflag, long ctstamp) {
//        System.out.println("FILLING MOLD LAYER  ... STARTED !!");
        Map<Integer, Map<Integer, Tile>> mapML = new HashMap<>();
        mapML = initializeMoldLayer(mapML);
        
//        long timestamp = conf.options.getTimestamp();

        mapML = copySeedLayerToIntialMold(mapPAT, mapML);
//        System.out.println("TESTING MOLD LAYER: ");
//        PrintUtils.getInstance().printPATTERN(mapML, 3, 1);

        TileUtils tu = TileUtils.getInstance();
        TileMoldVerticalUtils tmvu = TileMoldVerticalUtils.getInstance();
        TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();
        int prow = conf.options.getROW_DEFAULT();

        try {
            // Create MOLD PIVOT Tile
            mvTile mvtile = new mvTile();
            Tile moldPivotTile = new Tile();
            String moldPivotName;
            Map<Integer, Tile> mapMoldPivot = new HashMap<>();
            mapMoldPivot = mapPAT.get(prow);
            if (mapMoldPivot.get(0).getNAME() == null) {
                System.out.println("Pivot Tile is NULL ! Randomly Generating the MOLD PIVOT TILE!!");
                moldPivotName = tmvu.getTileName();
                mvtile = tmvu.getMatchingTileByName(moldPivotName);
                tmvu.CopyMvtileToTile(mvtile, moldPivotTile);
                System.out.println("\nNew MOLD PIVOT NAME: " + mvtile.getNAME());
            } else {
                Tile curtile = new Tile();
                curtile = mapPAT.get(prow).get(0);
                tu.CopyToTile(curtile, moldPivotTile);
                System.out.println("Existing MOLD PIVOT NAME: " + moldPivotTile.getNAME());
            }
            mapMoldPivot.put(0, moldPivotTile);
            mapML.put(prow, mapMoldPivot);
//            System.out.println("i = " + prow + ", j = 0\n--------------------------");

            // Now Iterate Elements Diagonally over MOLD Layer
            int row = (conf.options.getROW_DEFAULT() - 1);
            int col = conf.options.getCOL_DEFAULT();
//            System.out.println("ROW: " + row + ", COL: " + col);
            int i = 0;
            int j = 0;
            int start = i, end = j;

            
            boolean colflag = false;

            while (row >= 0) {
                
                boolean tsflag = true;
                
                for (i = row; i < (conf.options.getROW_DEFAULT() + 1); i++) {
//                    System.out.println("i = " + i + ", j = " + j);

                    // Create TILEs
                    if (i == conf.options.getROW_DEFAULT() || j == 0) {
                        colflag = true;
                        if (j == 0) { // Vertical
//                            System.out.println("VERTICAL");

                            mvTile mvPrev = new mvTile();
                            tmvu.CopyTileToMvtile(moldPivotTile, mvPrev);

                            Tile rtile = mapPAT.get(i).get(j + 1);
                            int toppin = mvPrev.getNORTH();
                            int leftpin = rtile.getLEFT();

                            mvTile current = new mvTile();
                            current = tmvu.getMatchingTile(leftpin, toppin);
                            tmvu.CopyToTile(current, mvPrev);

                            Map<Integer, Tile> mapCP = mapPAT.get(i);
                            Tile replacetile = mapCP.get(j);
                            tmvu.CopyMvtileToTile(current, replacetile);
                            mapCP.put(j, replacetile);
                            mapML.put(i, mapCP);

                        } else { // Horizontal
//                            System.out.println("HORIZONTAL");

                            mvTile leftTile = new mvTile();
                            tmvu.CopyTileToMvtile(moldPivotTile, leftTile);

                            Tile topTile = mapPAT.get(i - 1).get(j);
                            int toppin = topTile.getBOTTOM();
                            int leftpin = leftTile.getEAST();

                            mhTile current = new mhTile();
                            current = tmhu.getSinglePinMatchingTile(leftpin, toppin);

                            Tile reptile = new Tile();
                            tmhu.CopyMhtileToTile(current, reptile);

                            Map<Integer, Tile> mapCP = mapPAT.get(i);
                            mapCP.put(j, reptile);
                            mapML.put(i, mapCP);
                        }
//                        System.out.println("i = " + i + ", j = " + j);
                    }

                    start = i;
                    end = j;
                    j++;

                }
                if (colflag) {
//                    PrintUtils.getInstance().printPATTERN(mapML, 0, 1);

                    if ( i == 0 && j > 0 ) tsflag = false;
                    if (tsflag) {
                        ctstamp += 1;
                        tsflag = false;
                        System.out.println("CTIME STAMP: " + ctstamp);
                    }
                    
                    if (printflag) {                        
                        PrintUtils.getInstance().printPATTERN(mapML, 3, 1);
                    }


                    
                    colflag = false;
                }
                
                j = (row == 0) ? j++ : 0;
                
//                if ( i == 0 && j > 0 ) tsflag = false;
//                if (tsflag) {
//                    timestamp += 1;
//                    tsflag = false;
//
//                    System.out.println("CTIME STAMP: " + timestamp);
//                }

                row--;
            }

            int maxr = start;

            int maxc = conf.options.getCOL_DEFAULT();
            if (start == end) {
                row = 0;
                col = 1;
            }

            
            colflag = false;

            while (col < (conf.options.getCOL_DEFAULT() + 1)) {
                i = 0;
                j = col;
                
                boolean tsflag = true;
                while (i <= maxr && j <= maxc) {
//                    System.out.println("i = " + i + ", j = " + j);

                    // Create TILEs
                    if (i == conf.options.getROW_DEFAULT() || j == 0) {
                        colflag = true;
                        if (j == 0) { // Vertical
//                            System.out.println("VERTICAL");

                            mvTile mvPrev = new mvTile();
                            tmvu.CopyTileToMvtile(moldPivotTile, mvPrev);

                            Tile rtile = mapPAT.get(i).get(j + 1);
                            int toppin = mvPrev.getNORTH();
                            int leftpin = rtile.getLEFT();

                            mvTile current = new mvTile();
                            current = tmvu.getMatchingTile(leftpin, toppin);
                            tmvu.CopyToTile(current, mvPrev);

                            Map<Integer, Tile> mapCP = mapPAT.get(i);
                            Tile replacetile = mapCP.get(j);
                            tmvu.CopyMvtileToTile(current, replacetile);
                            mapCP.put(j, replacetile);
                            mapML.put(i, mapCP);

                        } else { // Horizontal
//                            System.out.println("HORIZONTAL");

                            mvTile leftTile = new mvTile();
                            tmvu.CopyTileToMvtile(moldPivotTile, leftTile);

                            Tile topTile = mapPAT.get(i - 1).get(j);
                            int toppin = topTile.getBOTTOM();
                            int leftpin = leftTile.getEAST();

                            mhTile current = new mhTile();
                            current = tmhu.getSinglePinMatchingTile(leftpin, toppin);

                            Tile reptile = new Tile();
                            tmhu.CopyMhtileToTile(current, reptile);

                            Map<Integer, Tile> mapCP = mapPAT.get(i);
                            mapCP.put(j, reptile);
                            mapML.put(i, mapCP);
                        }
//                        System.out.println("i = " + i + ", j = " + j);
                    }

                    i++;
                    j++;
                    
                }

                if (colflag) {
//                    PrintUtils.getInstance().printPATTERN(mapML, 0, 1);
                    
                    if ( i == 0 && j > 0 ) tsflag = false;
                    if (tsflag) {
                        ctstamp += 1;
                        tsflag = false;
                        System.out.println("CTIME STAMP: " + ctstamp);
                    }
                    
                    if (printflag) {
                        PrintUtils.getInstance().printPATTERN(mapML, 3, 1);
                    }

                    colflag = false;
                }
                

                maxr = (row == conf.options.getROW_DEFAULT() && col == conf.options.getCOL_DEFAULT()) ? maxr-- : maxr;
//                
//                if ( i == 0 && j > 0 ) tsflag = false;
//                if (tsflag) {
//                    timestamp += 1;
//                    tsflag = false;
//
//                    System.out.println("CTIME STAMP: " + timestamp);
//                }

                col++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        conf.options.setCtstamp(ctstamp);
        System.out.println("FILLING MOLD LAYER  ... ENDED !!");

        return mapML;
    }

    public boolean isValid(int i, int j) {
        if (i >= 0 || i < conf.options.getROW_DEFAULT() || j <= conf.options.getCOL_DEFAULT() || j > 0) {
            return false;
        }
        return true;
    }

    public Map<Integer, Map<Integer, Tile>> segmentSeedLayer(Map<Integer, Map<Integer, Tile>> mapPAT) {
        return segmentSeedLayer(mapPAT, 0);
    }

    public Map<Integer, Map<Integer, Tile>> segmentSeedLayer(Map<Integer, Map<Integer, Tile>> mapPAT, int printflag) {
        Map<Integer, Map<Integer, Tile>> mapSeedLayer = new HashMap<>();
        TileUtils tu = TileUtils.getInstance();
        try {
            for (int i = 0; i < (conf.options.getROW_DEFAULT() + 1); i++) {
                Map<Integer, Tile> mapTile = new HashMap<>();
                for (int j = 0; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                    Tile nutile = new Tile();
                    if (j != 0) {
                        Tile ctile = new Tile();
                        ctile = mapPAT.get(i).get(j);
                        tu.CopyToTile(ctile, nutile);
                    }
                    mapTile.put(j, nutile);
                }

                if (i == conf.options.getROW_DEFAULT()) {
                    for (int j = 0; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                        Tile nutile = new Tile();
                        mapTile.put(j, nutile);
                    }
                }
                mapSeedLayer.put(i, mapTile);
            }

            Tile pvtile = new Tile();
            Tile mpvtile = new Tile();
            mpvtile = mapPAT.get(conf.options.getROW_DEFAULT()).get(0);
            tu.CopyToTile(mpvtile, pvtile);
            Map<Integer, Tile> mapMBL = mapSeedLayer.get(conf.options.getROW_DEFAULT());
            mapMBL.put(0, pvtile);
            mapSeedLayer.put(conf.options.getROW_DEFAULT(), mapMBL);
        } catch (Exception e) {
            System.out.println("ERROR in Creating the MOLD LAYER !!" + e.getMessage());
        }
        return mapSeedLayer;
    }

    public Map<Integer, Map<Integer, Tile>> segmentMoldLayer(Map<Integer, Map<Integer, Tile>> mapPAT) {
        Map<Integer, Map<Integer, Tile>> mapMoldLayer = new HashMap<>();
        TileUtils tu = TileUtils.getInstance();
        try {
            for (int i = conf.options.getROW_DEFAULT(); i >= 0; i--) {
                Map<Integer, Tile> mapTile = new HashMap<>();
                for (int j = 0; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                    Tile nutile = new Tile();
                    if (j == 0 || i == conf.options.getROW_DEFAULT()) {
                        Tile ctile = new Tile();
                        ctile = mapPAT.get(i).get(j);
                        tu.CopyToTile(ctile, nutile);
                    }
                    mapTile.put(j, nutile);
                }
                mapMoldLayer.put(i, mapTile);
            }
        } catch (Exception e) {
            System.out.println("ERROR in Creating the MOLD LAYER !!" + e.getMessage());
        }
        return mapMoldLayer;
    }

    public Map<Integer, Map<Integer, Tile>> getFixedMoldLayer(Map<Integer, Map<Integer, Tile>> mapPAT) {
        Map<Integer, Map<Integer, Tile>> mapML = new HashMap<>();

        TileUtils tu = TileUtils.getInstance();
        String names = "CDBCDBCBCBCBCB";

        int k = 0;
        conf = Configuration.getInstance();
        int j = 0;
        for (int i = 0; i < (conf.options.getROW_DEFAULT() + 1); i++) {
//            System.out.println("i = " + i + ", j = " + j);
            Map<Integer, Tile> mapLAY = new HashMap<>();
            Tile ctile = new Tile();
            ctile = tu.getMatchingTileByName(names.charAt(k) + "");
            k++;
            mapLAY.put(j, ctile);
            if (i == conf.options.getROW_DEFAULT()) {
                for (j = 1; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                    Tile rtile = new Tile();
                    rtile = tu.getMatchingTileByName(names.charAt(k) + "");
                    k++;
                    mapLAY.put(j, rtile);
//                    System.out.println("i = " + i + ", j = " + j);
                }
            }
            mapML.put(i, mapLAY);
        }

        return mapML;
    }

    public Map<Integer, Map<Integer, Tile>> getCandiateMap(Map<Integer, Map<Integer, Tile>> mapPAT) {
        conf = Configuration.getInstance();

        Map<Integer, Map<Integer, Tile>> mapCS = new HashMap<>();
        for (int i = 0; i < conf.options.getROW_DEFAULT() + 1; i++) {
            Map<Integer, Tile> mapROW = new HashMap<>();

            for (int j = 0; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                Tile ctile;
                if (j == 0 || i == conf.options.getROW_DEFAULT()) {
                    ctile = mapPAT.get(i).get(j);
                } else {
                    ctile = new Tile();
                    ctile.setDirflag(-1);
                    ctile.setKscore(0.0f);
                    ctile.setPinmatch(0);
                    ctile.setEvtflag(-1);
                }

                mapROW.put(j, ctile);
            }
            mapCS.put(i, mapROW);
        }
        return mapCS;
    }

    public Map<Integer, Point> getInitialCoordinatePoints() {
        conf = Configuration.getInstance();

        Map<Integer, Point> canSET = new HashMap<>();
        int j = 1;
        String hcode = null;
        for (int i = 0; i < conf.options.getROW_DEFAULT(); i++) {
            Point point = new Point(i, j);
            hcode = point.getI() + ", " + point.getJ();
            canSET.put(hcode.hashCode(), point);

            if (i == (conf.options.getROW_DEFAULT() - 1)) {
                for (j = 2; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                    point = new Point(i, j);
                    hcode = point.getI() + ", " + point.getJ();
                    canSET.put(hcode.hashCode(), point);

                }
            }
        }
        return canSET;
    }
    
    public Map<Integer, Point> getInitialMoldCoordinatePoints() {
        conf = Configuration.getInstance();

        Map<Integer, Point> canSET = new HashMap<>();

        int j = 0;
        String hcode = null;
        
        for (int i = 0; i < (conf.options.getROW_DEFAULT() + 1); i++) {

            if (i < conf.options.getROW_DEFAULT() ) {
                Point point = new Point(i, j);
                hcode = point.getI() + ", " + point.getJ();
                canSET.put(hcode.hashCode(), point);
            }

            if (i == (conf.options.getROW_DEFAULT())) {
                for (j = 1; j < conf.options.getCOL_DEFAULT() + 1; j++) {
                    Point point = new Point(i, j);
                    hcode = point.getI() + ", " + point.getJ();
                    canSET.put(hcode.hashCode(), point);
                }
            }

        }

        return canSET;
    }
  
    public Map<Integer, Point> getNeighborsMap(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        Map<Integer, Point> mapNP = new HashMap<>();

        conf = Configuration.getInstance();

        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        // 2 - Pin Options
        if (cp.getI() == 0 && cp.getJ() == maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            mapNP.put(1, left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            mapNP.put(2, bottom);
        } else // 3 - Pin Options
        if (cp.getI() == 0 && cp.getJ() < maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            mapNP.put(1, left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            mapNP.put(2, bottom);

            Point right = new Point(cp.getI(), (cp.getJ() + 1));
            mapNP.put(3, right);
        } else // 3 - Pin Options
        if (cp.getI() < maxrow && cp.getJ() == maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            mapNP.put(1, left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            mapNP.put(2, bottom);

            Point top = new Point((cp.getI() - 1), cp.getJ());
            mapNP.put(4, top);
        } else { // 4 - Pin Options
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            mapNP.put(1, left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            mapNP.put(2, bottom);

            Point right = new Point(cp.getI(), (cp.getJ() + 1));
            mapNP.put(3, right);

            Point top = new Point((cp.getI() - 1), cp.getJ());
            mapNP.put(4, top);
        }

        return mapNP;
    }
    
    public Map<Integer, Point> getMoldNeighborsMap(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        Map<Integer, Point> mapNP = new HashMap<>();

        conf = Configuration.getInstance();

        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        // 2 - Pin Options
        if (cp.getI() == 0 && cp.getJ() == 0) {
            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            mapNP.put(2, bottom);
            
            Point right = new Point(cp.getI(), cp.getJ() + 1);
            mapNP.put(3, right);
            
        } else {
            if (cp.getI() == maxrow && cp.getJ() == maxcol) {
                
                Point left = new Point(cp.getI(), (cp.getJ() - 1));
                mapNP.put(1, left);
                
                Point top = new Point((cp.getI() - 1), cp.getJ());
                mapNP.put(1, top);
                
            } else {
                if (cp.getI() <= maxrow && cp.getJ() == 0) {
                    
                    if (cp.getI() > 0) {
                        Point top = new Point((cp.getI() - 1), cp.getJ());
                        mapNP.put(4, top);
                    }
                    
                    if (cp.getI() < maxrow) {
                        Point bottom = new Point((cp.getI() + 1), cp.getJ());
                        mapNP.put(2, bottom);
                    }
                    
                    Point right = new Point(cp.getI(), cp.getJ() + 1);
                    mapNP.put(3, right);

                } else { // 4 - Pin Options
                    if (cp.getI() == maxrow && cp.getJ() < maxcol) {
                        if (cp.getJ() > 0) {
                            Point left = new Point(cp.getI(), (cp.getJ() - 1));
                            mapNP.put(1, left);
                        }

                        Point right = new Point(cp.getI(), (cp.getJ() + 1));
                        mapNP.put(3, right);

                        Point top = new Point((cp.getI() - 1), cp.getJ());
                        mapNP.put(4, top);
                    }
                }
            }
        }

        return mapNP;
    }

    public List<Point> getNeighbors(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        conf = Configuration.getInstance();
        List<Point> adjpoints = new LinkedList<>();

        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        // 2 - Pin Options
        if (cp.getI() == 0 && cp.getJ() == maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            adjpoints.add(left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            adjpoints.add(bottom);
        } else // 3 - Pin Options
        if (cp.getI() == 0 && cp.getJ() < maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            adjpoints.add(left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            adjpoints.add(bottom);

            Point right = new Point(cp.getI(), (cp.getJ() + 1));
            adjpoints.add(right);
        } else // 3 - Pin Options
        if (cp.getI() < maxrow && cp.getJ() == maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            adjpoints.add(left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            adjpoints.add(bottom);

            Point top = new Point((cp.getI() - 1), cp.getJ());
            adjpoints.add(top);
        } else { // 4 - Pin Options
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            adjpoints.add(left);

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            adjpoints.add(bottom);

            Point right = new Point(cp.getI(), (cp.getJ() + 1));
            adjpoints.add(right);

            Point top = new Point((cp.getI() - 1), cp.getJ());
            adjpoints.add(top);
        }

        return adjpoints;
    }

    public boolean isOnPoint(Map<Integer, Map<Integer, Tile>> cmap, Point pt) {
        boolean flag = false;

        Tile tetile = new Tile();
        tetile = cmap.get(pt.getI()).get(pt.getJ());
        if (tetile.getNAME() == null) {
            flag = true;
        }

        return flag;
    }

    public List<Point> getAdjacentPoints(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        conf = Configuration.getInstance();
        List<Point> adjpoints = new LinkedList<>();

        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();
        
//        System.out.println("MAXROW: " + maxrow + ", MAXCOL: " + maxcol);

        // 2 - Pin Options
        if (cp.getI() == 0 && cp.getJ() == maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            if (isOnPoint(cmap, left)) {
                adjpoints.add(left);
            }

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            if (isOnPoint(cmap, bottom)) {
                adjpoints.add(bottom);
            }
        } else // 3 - Pin Options
        if (cp.getI() == 0 && cp.getJ() < maxcol) {
            if (cp.getJ() != 1) {
                Point left = new Point(cp.getI(), (cp.getJ() - 1));
                if (isOnPoint(cmap, left)) {
                    adjpoints.add(left);
                }
            }

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            if (isOnPoint(cmap, bottom)) {
                adjpoints.add(bottom);
            }

            Point right = new Point(cp.getI(), (cp.getJ() + 1));
            if (isOnPoint(cmap, right)) {
                adjpoints.add(right);
            }
        } else // 3 - Pin Options
        if (cp.getI() < maxrow && cp.getJ() == maxcol) {
            Point left = new Point(cp.getI(), (cp.getJ() - 1));
            if (isOnPoint(cmap, left)) {
                adjpoints.add(left);
            }

            if (cp.getI() != (maxrow - 1)) {
                Point bottom = new Point((cp.getI() + 1), cp.getJ());
                if (isOnPoint(cmap, bottom)) {
                    adjpoints.add(bottom);
                }
            }

            Point top = new Point((cp.getI() - 1), cp.getJ());
            if (isOnPoint(cmap, top)) {
                adjpoints.add(top);
            }
        } else { // 4 - Pin Options
            if (cp.getJ() != 1) {
                Point left = new Point(cp.getI(), (cp.getJ() - 1));
                if (isOnPoint(cmap, left)) {
                    adjpoints.add(left);
                }
            }

            if (cp.getI() != (maxrow - 1)) {
                Point bottom = new Point((cp.getI() + 1), cp.getJ());
                if (isOnPoint(cmap, bottom)) {
                    adjpoints.add(bottom);
                }
            }

            Point right = new Point(cp.getI(), (cp.getJ() + 1));
            if (isOnPoint(cmap, right)) {
                adjpoints.add(right);
            }

            Point top = new Point((cp.getI() - 1), cp.getJ());
            if (isOnPoint(cmap, top)) {
                adjpoints.add(top);
            }
        }

        return adjpoints;
    }

    public List<Point> getMoldAdjacentPoints(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        conf = Configuration.getInstance();
        List<Point> adjpoints = new LinkedList<>();

        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        // 1 - Pin Option
        if (cp.getI() == 0 && cp.getJ() == 0) {

            Point bottom = new Point((cp.getI() + 1), cp.getJ());
            if (isOnPoint(cmap, bottom)) {
                adjpoints.add(bottom);
            }

        } else { // 1 - Pin Option
            if (cp.getI() == maxrow && cp.getJ() == maxcol) {

                Point left = new Point(cp.getI(), (cp.getJ() - 1));
                if (isOnPoint(cmap, left)) {
                    adjpoints.add(left);
                }

            } else { // 2 - Pin Options
                if (cp.getI() < maxrow && cp.getJ() == 0) {

                    if (cp.getI() > 0) {
                        Point top = new Point((cp.getI() - 1), cp.getJ());

                        if (isOnPoint(cmap, top)) {
                            adjpoints.add(top);
                        }

                    }

                    Point bottom = new Point((cp.getI() + 1), cp.getJ());
                    if (isOnPoint(cmap, bottom)) {
                        adjpoints.add(bottom);
                    }

                } else { // 2 - Pin Options
                    if (cp.getI() == maxrow && cp.getJ() < maxcol) {
                        if (cp.getJ() > 0) {

                            Point left = new Point(cp.getI(), (cp.getJ() - 1));
                            if (isOnPoint(cmap, left)) {
                                adjpoints.add(left);
                            }

                        }

                        Point right = new Point(cp.getI(), (cp.getJ() + 1));
                        if (isOnPoint(cmap, right)) {
                            adjpoints.add(right);
                        }

                    }
                }
            }
        }

        return adjpoints;
    }

    public void updateMoldPinCounts(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        Map<Integer, Point> npmap = new HashMap<>();
        npmap = getMoldNeighborsMap(cmap, cp);
        updatePinCounts(cmap, cp, npmap);
    }

    public void updateNeighbourPinCounts(Map<Integer, Map<Integer, Tile>> cmap, Point cp) {
        Map<Integer, Point> npmap = new HashMap<>();
        npmap = getNeighborsMap(cmap, cp);
        updatePinCounts(cmap, cp, npmap);
    }

    public void updateMoldPinCounts(Map<Integer, Map<Integer, Tile>> cmap, Point cp, Map<Integer, Point> npmap) {
        int pincount = 0;
        conf = Configuration.getInstance();

        int i = cp.getI();
        int j = cp.getJ();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, Tile> cRow = cmap.get(i);
        Tile ctile = cRow.get(j);

//        System.out.println("C L: " + ctile.getLEFT());
//        System.out.println("C B: " + ctile.getBOTTOM());
//        System.out.println("C R: " + ctile.getRIGHT());
//        System.out.println("C T: " + ctile.getTOP());
        Iterator<Integer> diter = npmap.keySet().iterator();
        while (diter.hasNext()) {
            int di = diter.next();
            Point ap = npmap.get(di);
            Map<Integer, Tile> mapROW = cmap.get(ap.getI());
            Tile atile = mapROW.get(ap.getJ());
//            System.out.println("ATILE NAME: " + atile.getNAME());
            if (atile.getNAME() != null) {
//                System.out.println("(" + ap.getI() + ", " + ap.getJ() + ") - " + di);
                switch (di) {
                    case 1:
//                        System.out.println("A R: " + atile.getRIGHT());
                        if (ctile.getLEFT() == atile.getRIGHT()) {
                            pincount = pincount + 1;
                            if ( cp.getI() == maxrow ) {
                                if (cp.getJ() > 0) {
                                    int acount = atile.getPinmatch() + 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                    case 2:
//                        System.out.println("A T: " + atile.getTOP());
                        if (ctile.getBOTTOM() == atile.getTOP()) {
                            pincount = pincount + 1;
                            if (cp.getJ() == 0) {
                                if (cp.getI() < maxrow ) {
                                    int acount = atile.getPinmatch() + 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                    case 3:
//                        System.out.println("A L: " + atile.getLEFT());
                        if (ctile.getRIGHT() == atile.getLEFT()) {
                            pincount = pincount + 1;

                            if (cp.getI() == maxrow) {
                                if (cp.getJ() < maxcol) {
                                    int acount = atile.getPinmatch() + 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                    case 4:
//                        System.out.println("A B: " + atile.getBOTTOM());
                        if (ctile.getTOP() == atile.getBOTTOM()) {
                            pincount = pincount + 1;
                            if (cp.getI() > 0) {
                                if (cp.getJ() == 0) {
                                    int acount = atile.getPinmatch() + 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                }
                mapROW.put(ap.getJ(), atile);
                cmap.put(ap.getI(), mapROW);
            }
        }
        ctile.setPinmatch(pincount);
        cRow.put(j, ctile);
        cmap.put(i, cRow);

    }
    
    public void updatePinCounts(Map<Integer, Map<Integer, Tile>> cmap, Point cp, Map<Integer, Point> npmap) {
        int pincount = 0;
        conf = Configuration.getInstance();

        int i = cp.getI();
        int j = cp.getJ();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, Tile> cRow = cmap.get(i);
        Tile ctile = cRow.get(j);

//        System.out.println("C L: " + ctile.getLEFT());
//        System.out.println("C B: " + ctile.getBOTTOM());
//        System.out.println("C R: " + ctile.getRIGHT());
//        System.out.println("C T: " + ctile.getTOP());
        Iterator<Integer> diter = npmap.keySet().iterator();
        while (diter.hasNext()) {
            int di = diter.next();
            Point ap = npmap.get(di);
            Map<Integer, Tile> mapROW = cmap.get(ap.getI());
            Tile atile = mapROW.get(ap.getJ());
//            System.out.println("ATILE NAME: " + atile.getNAME());
            if (atile.getNAME() != null) {
//                System.out.println("(" + ap.getI() + ", " + ap.getJ() + ") - " + di);
                switch (di) {
                    case 1:
//                        System.out.println("A R: " + atile.getRIGHT());
                        if (ctile.getLEFT() == atile.getRIGHT()) {
                            pincount = pincount + 1;
                            if (ap.getJ() != 0) {
                                int acount = atile.getPinmatch() + 1;
                                atile.setPinmatch(acount);
                            }
                        }
                        break;
                    case 2:
//                        System.out.println("A T: " + atile.getTOP());
                        if (ctile.getBOTTOM() == atile.getTOP()) {
                            pincount = pincount + 1;
                            if (ap.getI() != maxrow) {
                                int acount = atile.getPinmatch() + 1;
                                atile.setPinmatch(acount);
                            }
                        }
                        break;
                    case 3:
//                        System.out.println("A L: " + atile.getLEFT());
                        if (ctile.getRIGHT() == atile.getLEFT()) {
                            pincount = pincount + 1;
                            int acount = atile.getPinmatch() + 1;
                            atile.setPinmatch(acount);
                        }
                        break;
                    case 4:
//                        System.out.println("A B: " + atile.getBOTTOM());
                        if (ctile.getTOP() == atile.getBOTTOM()) {
                            pincount = pincount + 1;
                            int acount = atile.getPinmatch() + 1;
                            atile.setPinmatch(acount);
                        }
                        break;
                }
                mapROW.put(ap.getJ(), atile);
                cmap.put(ap.getI(), mapROW);
            }
        }
        ctile.setPinmatch(pincount);
        cRow.put(j, ctile);
        cmap.put(i, cRow);
    }

    public void updatePinCountsAfterRemoval(Map<Integer, Map<Integer, Tile>> cmap, Point rpoint) {
        Map<Integer, Point> npmap = new HashMap<>();
        npmap = getNeighborsMap(cmap, rpoint);

//        System.out.println("\nprinting neighbors after removal");
//        PrintUtils.getInstance().printMapIntPoint(npmap);
        updatePinCountsAfterRemoval(cmap, rpoint, npmap);
    }

    public void updatePinCountsAfterRemoval(Map<Integer, Map<Integer, Tile>> cmap, Point cp, Map<Integer, Point> npmap) {
        int pincount = 0;
        conf = Configuration.getInstance();

        int i = cp.getI();
        int j = cp.getJ();
        int maxrow = conf.options.getROW_DEFAULT();
//        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, Tile> cRow = cmap.get(i);
        Tile ctile = cRow.get(j);
//        pincount = ctile.getPinmatch();

//        System.out.println("C L: " + ctile.getLEFT());
//        System.out.println("C B: " + ctile.getBOTTOM());
//        System.out.println("C R: " + ctile.getRIGHT());
//        System.out.println("C T: " + ctile.getTOP());
        Iterator<Integer> diter = npmap.keySet().iterator();
        while (diter.hasNext()) {
            int di = diter.next();
            Point ap = npmap.get(di);
            Map<Integer, Tile> mapROW = cmap.get(ap.getI());
            Tile atile = mapROW.get(ap.getJ());
//            System.out.println("ATILE NAME: " + atile.getNAME());
            if (atile.getNAME() != null) {
//                System.out.println("(" + ap.getI() + ", " + ap.getJ() + ") - " + di);
                switch (di) {
                    case 1:
//                        System.out.println("A R: " + atile.getRIGHT());
                        if (atile.getRIGHT() >= 0) {
                            if (ctile.getLEFT() == atile.getRIGHT()) {
                                if (ap.getJ() != 0) {
                                    if (atile.getPinmatch() > 0) {
                                        int acount = atile.getPinmatch() - 1;
                                        atile.setPinmatch(acount);

                                    }
                                }
                            }
                        }
                        break;
                    case 2:
//                        System.out.println("A T: " + atile.getTOP());
                        if (atile.getTOP() >= 0) {
                            if (ctile.getBOTTOM() == atile.getTOP()) {
                                if (ap.getI() != maxrow) {
                                    if (atile.getPinmatch() > 0) {
                                        int acount = atile.getPinmatch() - 1;
                                        atile.setPinmatch(acount);
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
//                        System.out.println("A L: " + atile.getLEFT());
                        if (atile.getLEFT() >= 0) {
                            if (ctile.getRIGHT() == atile.getLEFT()) {
                                if (atile.getPinmatch() > 0) {
                                    int acount = atile.getPinmatch() - 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                    case 4:
//                        System.out.println("A B: " + atile.getBOTTOM());
                        if (atile.getBOTTOM() >= 0) {
                            if (ctile.getTOP() == atile.getBOTTOM()) {
                                if (atile.getPinmatch() > 0) {
                                    int acount = atile.getPinmatch() - 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                }
                mapROW.put(ap.getJ(), atile);
                cmap.put(ap.getI(), mapROW);
            }
        }
//        ctile.setPinmatch(pincount);
//        cRow.put(j, ctile);
//        cmap.put(i, cRow);
    }
    

    public void updateMoldPinCountsAfterRemoval(Map<Integer, Map<Integer, Tile>> cmap, Point rpoint) {
        Map<Integer, Point> npmap = new HashMap<>();
        npmap = getMoldNeighborsMap(cmap, rpoint);
        updateMoldPinCountsAfterRemoval(cmap, rpoint, npmap);
    }
    
    public void updateMoldPinCountsAfterRemoval(Map<Integer, Map<Integer, Tile>> cmap, Point cp, Map<Integer, Point> npmap) {
        int pincount = 0;
        conf = Configuration.getInstance();

        int i = cp.getI();
        int j = cp.getJ();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, Tile> cRow = cmap.get(i);
        Tile ctile = cRow.get(j);
//        pincount = ctile.getPinmatch();

        Iterator<Integer> diter = npmap.keySet().iterator();
        while (diter.hasNext()) {
            int di = diter.next();
            Point ap = npmap.get(di);
            Map<Integer, Tile> mapROW = cmap.get(ap.getI());
            Tile atile = mapROW.get(ap.getJ());
//            System.out.println("ATILE NAME: " + atile.getNAME());
            if (atile.getNAME() != null) {
//                System.out.println("(" + ap.getI() + ", " + ap.getJ() + ") - " + di);
                switch (di) {
                    case 1:
//                        System.out.println("A R: " + atile.getRIGHT());
//                        if (atile.getRIGHT() >= 0) {

                        if (cp.getI() == maxrow) {
                            if (cp.getJ() > 0) {
                                if (ctile.getLEFT() == atile.getRIGHT()) {
                                    if (atile.getPinmatch() > 0) {
                                        int acount = atile.getPinmatch() - 1;
                                        atile.setPinmatch(acount);
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
//                        System.out.println("A T: " + atile.getTOP());
//                        if (atile.getTOP() >= 0) {
                        if (cp.getJ() == 0) {
                            if (cp.getI() < maxrow ) {
                                if (ctile.getBOTTOM() == atile.getTOP()) {
                                    if (atile.getPinmatch() > 0) {
                                        int acount = atile.getPinmatch() - 1;
                                        atile.setPinmatch(acount);
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
//                        System.out.println("A L: " + atile.getLEFT());

                        if (cp.getI() == maxrow) {
                            if (cp.getJ() < maxcol) {
                                if (ctile.getRIGHT() == atile.getLEFT()) {
                                    int acount = atile.getPinmatch() - 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                    case 4:
//                        System.out.println("A B: " + atile.getBOTTOM());

                        if (cp.getI() > 0) {
                            if (cp.getJ() == 0) {
                                if (ctile.getTOP() == atile.getBOTTOM()) {
                                    int acount = atile.getPinmatch() - 1;
                                    atile.setPinmatch(acount);
                                }
                            }
                        }
                        break;
                }
                mapROW.put(ap.getJ(), atile);
                cmap.put(ap.getI(), mapROW);
            }
        }
//        ctile.setPinmatch(pincount);
//        cRow.put(j, ctile);
//        cmap.put(i, cRow);
    }

    public int getPinCount(Map<Integer, Map<Integer, Tile>> cmap, Point cp, Map<Integer, Point> npmap) {
        int pincount = 0;
        conf = Configuration.getInstance();

        int i = cp.getI();
        int j = cp.getJ();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, Tile> cRow = cmap.get(i);
        Tile ctile = cRow.get(j);

        Iterator<Integer> diter = npmap.keySet().iterator();
        while (diter.hasNext()) {
            int di = diter.next();
            Point ap = npmap.get(di);
            Map<Integer, Tile> mapROW = cmap.get(ap.getI());
            Tile atile = mapROW.get(ap.getJ());
            if (atile.getNAME() != null) {
                switch (di) {
                    case 1:
//                        System.out.println("A R: " + atile.getRIGHT());
                        if (ctile.getLEFT() == atile.getRIGHT()) {

                            pincount = pincount + 1;
                        }
                        break;
                    case 2:
//                        System.out.println("A T: " + atile.getTOP());
                        if (ctile.getBOTTOM() == atile.getTOP()) {
                            pincount = pincount + 1;
                        }
                        break;
                    case 3:
//                        System.out.println("A L: " + atile.getLEFT());
                        if (ctile.getRIGHT() == atile.getLEFT()) {
                            pincount = pincount + 1;
                        }
                        break;
                    case 4:
//                        System.out.println("A B: " + atile.getBOTTOM());
                        if (ctile.getTOP() == atile.getBOTTOM()) {
                            pincount = pincount + 1;
                        }
                        break;
                }
            }
        }

        return pincount;
    }
    
    public int getMoldPinCount(Map<Integer, Map<Integer, Tile>> cmap, Point cp, Map<Integer, Point> npmap) {
        int pincount = 0;
        conf = Configuration.getInstance();

        int i = cp.getI();
        int j = cp.getJ();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, Tile> cRow = cmap.get(i);
        Tile ctile = cRow.get(j);

        Iterator<Integer> diter = npmap.keySet().iterator();
        while (diter.hasNext()) {
            int di = diter.next();
            Point ap = npmap.get(di);
            Map<Integer, Tile> mapROW = cmap.get(ap.getI());
            Tile atile = mapROW.get(ap.getJ());
            if (atile.getNAME() != null) {
                switch (di) {
                    case 1:
//                        System.out.println("A R: " + atile.getRIGHT());
                        if (ctile.getLEFT() == atile.getRIGHT()) {
                            pincount = pincount + 1;
                        }
                        break;
                    case 2:
//                        System.out.println("A T: " + atile.getTOP());
                        if (ctile.getBOTTOM() == atile.getTOP()) {
                            pincount = pincount + 1;
                        }
                        break;
                    case 3:
//                        System.out.println("A L: " + atile.getLEFT());
                        if (ctile.getRIGHT() == atile.getLEFT()) {
                            pincount = pincount + 1;
                        }
                        break;
                    case 4:
//                        System.out.println("A B: " + atile.getBOTTOM());
                        if (ctile.getTOP() == atile.getBOTTOM()) {
                            pincount = pincount + 1;
                        }
                        break;
                }
            }
        }

        return pincount;
    }

    public int getPCount(Tile ctile, Tile atile, String flag) {
        int count = 0;
        if (atile.getNAME() != null) {
            if (flag == "L") {
                if (ctile.getLEFT() == atile.getRIGHT()) {
                    count++;
                }
            } else if (flag == "R") {
                if (ctile.getRIGHT() == atile.getLEFT()) {
                    count++;
                }
            } else if (flag == "T") {
                if (ctile.getTOP() == atile.getBOTTOM()) {
                    count++;
                }
            } else if (flag == "B") {
                if (ctile.getBOTTOM() == atile.getTOP()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getOffPoint(Map<Integer, Map<Integer, Tile>> cmap, Map<Integer, Point> offSet) {
        int rehash = 0;

        Map<Integer, Map<Integer, Double>> mapPHS = new HashMap<>();
        try {

            Map<Integer, Double> mapAPs = new HashMap<>();
            double K_off = 0.0f;
            for (int v = 1; v < 5; v++) {
                int N = 0;
                double score = 0.0f;

                List<Integer> P = new LinkedList<>();
                N = getSumofSpecificPinMatch(cmap, offSet, P, v);
                score = N * (conf.options.getK_f() * Math.exp(-1.0 * v * conf.options.getG_se()));
                K_off += score;

                Map<Integer, Double> mapR = new HashMap<>();
                Iterator<Integer> potiter = P.iterator();
                while (potiter.hasNext()) {
                    int phash = potiter.next();
                    mapR.put(phash, score);
                    mapAPs.put(phash, score);
                }

//                PrintUtils.getInstance().printMapID(mapR);
                mapPHS.put(v, mapR);
            }

//            /* Normalize score for b bonds: score = k_{off, b} / k_{off} */
//            Iterator<Integer> iter = mapAPs.keySet().iterator();
//            while (iter.hasNext()) {
//                int chash = iter.next();
//                double cscore = mapAPs.get(chash);
//                System.out.print("\n" + cscore + " - ");
//                cscore = (K_off > 0.0f) ? cscore / K_off : cscore;
//                System.out.print(cscore);
//                mapAPs.put(chash, cscore);
//            }
            mapPHS = SortUtils.sortMapIntMapIntDouByKeyValue(mapPHS);
//            System.out.println("\n------------------\nSorted Points:");
//            PrintUtils.getInstance().printMapIID(mapPHS);
//            System.out.println("\n------------------\n");

            mapAPs = SortUtils.sortMapIDByValue(mapAPs);
            if (mapAPs.keySet().size() > 0) {
                rehash = mapAPs.keySet().iterator().next();
            }

            System.out.println("Chosen Point TO REMOVE: (" + offSet.get(rehash).getI() + ", " + offSet.get(rehash).getJ() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rehash;
    }

    public int getEvent() {
        int evtID = -1;

        Random rand = new Random(System.nanoTime());
        evtID = rand.nextInt() % 2;

        return evtID;
    }

    public int getEvent(Map<Integer, Map<Integer, Tile>> cmap, Map<Integer, Point> onSet, Map<Integer, Point> offSet) {
        int evtID = -1;

        try {
            /* Count the number of elements in the candidate set with event flag = 1 */
            int m_count = onSet.keySet().size();

            /* Compute K_ON for add ON event */
            double K_on = m_count * (conf.options.getK_f() * Math.exp(-1.0 * conf.options.getG_mc()));
//            System.out.println("\nK_ON = " + K_on);

            Map<Integer, Map<Integer, Double>> mapPHS = new HashMap<>();

            double K_off = 0.0f;
            for (int v = 1; v < 5; v++) {
                int N = 0;
                double score = 0.0f;

                List<Integer> P = new LinkedList<>();
                N = getSumofSpecificPinMatch(cmap, offSet, P, v);
                score = N * (conf.options.getK_f() * Math.exp(-1.0 * v * conf.options.getG_se()));
                K_off += score;

                Map<Integer, Double> mapR = new HashMap<>();
                Iterator<Integer> potiter = P.iterator();
                while (potiter.hasNext()) {
                    int phash = potiter.next();
                    mapR.put(phash, score);
                }

//                PrintUtils.getInstance().printMapID(mapR);
                mapPHS.put(v, mapR);
            }
            mapPHS = SortUtils.sortMapIntMapIntDouByKeyValue(mapPHS);
//            PrintUtils.getInstance().printMapIID(mapPHS);

            /* Print K_OFF */
//            System.out.println("K_OFF = " + K_off);

            double K_total = K_on + K_off;

            Random rand = new Random(System.nanoTime());

            double random = 0.0f;
            random = rand.nextDouble();
            while (random < 0 && random >= 1.0f) {
                random = rand.nextDouble();
            }

            K_total = (K_total == 0) ? (1.0f + K_total) : K_total;
            double deltaT = (-1.0f * Math.log(random)) / K_total;
//            System.out.println("DELTA TIME: " + deltaT);
            long waittime = new Double(deltaT).longValue();
            Thread.sleep(waittime);
//            System.out.println("DELTA T: " + deltaT);

            double P_on = K_on / K_total;
            double P_off = K_off / K_total;
//            System.out.println("P_ON: " + P_on + ",  P_OFF: " + P_off);

//            evtID = ( P_on > P_off ) ? 1 : 0;
            evtID = (P_on > 0.5f) ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return evtID;
    }

    public int getSumofSpecificPinMatch(Map<Integer, Map<Integer, Tile>> canMAP, Map<Integer, Point> offSet,
            List<Integer> pmpoints, int pinmatch) {
        int count = 0;
        Iterator<Integer> iter = offSet.keySet().iterator();
        while (iter.hasNext()) {
            int phash = iter.next();
            Point p = offSet.get(phash);
            Tile ctile = new Tile();
            ctile = canMAP.get(p.getI()).get(p.getJ());
            if (ctile.getPinmatch() == pinmatch) {
                pmpoints.add(phash);
                count++;
            }
        }
        return count;
    }
    
    
    public Map<Integer, List<Point>> getPatternPinMatchStats(Map<Integer, Map<Integer, Tile>> cmap) {
        conf = Configuration.getInstance();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();

        Map<Integer, List<Point>> mplist = new HashMap<>();

        int pcount = 0;
        for (int i = 0; i < conf.options.getROW_DEFAULT(); i++) {
            for (int j = 1; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                List<Point> lpoints = new LinkedList<>();

                Map<Integer, Tile> mapLAY = cmap.get(i);
                Tile tile = mapLAY.get(j);

                Point cp = new Point(i, j);
                pcount = tile.getPinmatch();
//                System.out.println("(" + i + ", " + j + ") - " + pcount);

                if (mplist.get(pcount) != null) {
                    lpoints = mplist.get(pcount);
                } else {
                    lpoints = new LinkedList<>();
                }

                lpoints.add(cp);
                mplist.put(pcount, lpoints);

            }
        }
        
        return mplist;
    }

    public Map<Integer, List<Point>> getMoldPinMatchStats(Map<Integer, Map<Integer, Tile>> cmap) {
        conf = Configuration.getInstance();
        int maxrow = conf.options.getROW_DEFAULT();
        int maxcol = conf.options.getCOL_DEFAULT();
        
        Map<Integer, List<Point>> mplist = new HashMap<>();
        
        int j = 0, pcount = 0;
        for (int i = 0; i < (conf.options.getROW_DEFAULT() + 1); i++) {
            List<Point> lpoints = new LinkedList<>();
            
            Map<Integer, Tile> mapLAY = cmap.get(i);
            Tile tile = mapLAY.get(j);
//            System.out.println("(" + i + ", " + j + ") - " + tile.getPinmatch());
            
            Point cp = new Point(i, j);
            pcount = tile.getPinmatch();
            
            if (mplist.get(pcount) != null) {
                lpoints = mplist.get(pcount);
            } else {
                lpoints = new LinkedList<>();
            }

            lpoints.add(cp);
            pcount = tile.getPinmatch();
            mplist.put(pcount, lpoints);
            
            if (i == conf.options.getROW_DEFAULT()) {
                for (j = 1; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                    mapLAY = cmap.get(i);
                    tile = mapLAY.get(j);
                    
                    cp = new Point(i, j);
                    pcount = tile.getPinmatch();
                    
                    if (mplist.get(pcount) != null) {
                        lpoints = mplist.get(pcount);
                    } else {
                        lpoints = new LinkedList<>();
                    }
                    lpoints.add(cp);
                    mplist.put(pcount, lpoints);
                    
//                    System.out.println("(" + i + ", " + j + ") - " + tile.getPinmatch());
                }
            }
            
            
        }
        
        return mplist;
    }

}
