/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.core;

import com.arl.chips.replicate.PerformReplication;
import com.arl.chips.components.Tile;
import com.arl.chips.conf.Configuration;
import com.arl.chips.utils.PrintUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author VKGautam
 */
public class AbstractTPSWrapper {

    int MOLD_ONLY = 0;
    int SEED_ONLY = 1;
    int PATT_ONLY = 2;
    int MOLD_PATTERN_ALL = 3;

    int MAX_ITERATION = 5;
    PrintUtils print;
    PerformReplication pr;
    Configuration conf;

    /**
     * Default Constructor
     */
    public AbstractTPSWrapper() {
        conf = Configuration.getInstance();
        print = PrintUtils.getInstance();
        pr = new PerformReplication();
    }

    public static void main(String[] args) {
        long stTime, enTime, timestamp = 0;
        stTime = System.nanoTime();

        int row = 6, column = 8, MAX_ITERATIONS = 3;

        AbstractTPSWrapper bcw = new AbstractTPSWrapper();

        Configuration conf = Configuration.getInstance();
        conf.options.setGrid(row, column);

        PerformReplication pr = new PerformReplication(conf);
        PrintUtils print = PrintUtils.getInstance();

        try {
            /* Initialize PATTERN */
            Map<Integer, Map<Integer, Tile>> myPattern = new HashMap<>();

            /* Step - C0: Create Initial PATTERN */
            myPattern = pr.getPattern(conf);
            System.out.println("Initial Configuration - Pattern Layer");
            print.printPATTERN(myPattern, 3, 1);    /* Print ALL LAYERS */
            
            myPattern = pr.createSeedLayer(myPattern);
            print.printPATTERN(myPattern, 1, 1);
            
            Map<Integer, Map<Integer, Tile>> mapMLSL = new HashMap<>();
            myPattern = pr.fillInitialMoldToPatternLayer(myPattern, timestamp);
            System.out.println("Printing PATTERN Layer");
            print.printPATTERN(myPattern, 2, 1);
            System.out.println("TIME STAMP: " + conf.options.getTimestamp());
            
            Map<Integer, Map<Integer, Tile>> mapML = new HashMap<>();
            mapML = pr.fillMoldFromSeedLayer(myPattern);
            System.out.println("Printing MOLD Layer");
            print.printPATTERN(mapML, 0, 1);

            System.out.println("Printing the FULL PATTERN");
            print.printPATTERN(myPattern, 3, 1);
            
            
            /* Step - C5: Intialize the MAP Data Structure to store the output of ALL CYCLEs */
            Map<Integer, Map<Integer, Map<Integer, Map<Integer, Tile>>>> MapReplicate = new HashMap<>();
            for (int i = 1; i < (MAX_ITERATIONS + 1); i++) {
                int niter = (int) Math.pow(2, i);
                for (int j = 0; j < niter; j++) {
                    int prevcol = (j % 2 == 0) ? (j / 2) : ((j - 1) / 2);
                    int prevrow = (i - 1);
                    
                    Map<Integer, Map<Integer, Map<Integer, Tile>>> mapRow = new HashMap<>();
                    Map<Integer, Map<Integer, Tile>> mapPatt = new HashMap<>();
                    mapRow.put(j, mapPatt);
                    MapReplicate.put(i, mapRow);
                }
            }

            Map<Integer, Map<Integer, Map<Integer, Tile>>> MapRepl = new HashMap<>();
            MapRepl.put(0, myPattern);
            MapReplicate.put(0, MapRepl);

            /* C6: Start performing the REPLICATION process */
            for (int i = 1; i < (MAX_ITERATIONS + 1); i++) {

                long ctimestamp1 = 0, ctimestamp2 = 0;
                
                int niter = (int) Math.pow(2, i);
                for (int j = 0; j < niter; j++) {
                    int prevcol = (j % 2 == 0) ? (j / 2) : ((j - 1) / 2);
                    int prevrow = (i - 1);
                    System.out.println("(" + prevrow + ", " + prevcol + ") ==> (" + i + ", " + j + ")");
                    
                    Map<Integer, Map<Integer, Tile>> mapR = new HashMap<>();
                    mapR = MapReplicate.get(prevrow).get(prevcol);
                    
                    
                    if ( ( j % 2 ) == 0 ) {
                        
                        ctimestamp1 = 0;
                        
                        Map<Integer, Map<Integer, Tile>> mapSeedLayer = new HashMap<>();
                        mapSeedLayer = pr.segmentSeedLayer(mapR);
                        System.out.println("\n---------------------\nPrinting SEED Layer");
                        print.printPATTERN(mapSeedLayer, 2, 1);
                        
                        Map<Integer, Map<Integer, Tile>> mapZeroPattern = new HashMap<>();
                        mapZeroPattern = pr.fillPatternToMold(mapSeedLayer, true, ctimestamp1);
                        System.out.println("\n---------------------\nAfter Filling MOLD Layer");
                        print.printPATTERN(mapZeroPattern, 3, 1);
                        
                        Map<Integer, Map<Integer, Map<Integer, Tile>>> mapNewRow = new HashMap<>();
                        mapNewRow = MapReplicate.get(i);
                        mapNewRow.put(j, mapZeroPattern);
                        MapReplicate.put(i, mapNewRow);
                        
                        ctimestamp1 = conf.options.getCtstamp();
                    }
                    
                    if ( ( j % 2 ) == 1 ) {
                        
                        ctimestamp2 = 0;

                        Map<Integer, Map<Integer, Tile>> mapMoldLayer = new HashMap<>();
                        mapMoldLayer = pr.segmentMoldLayer(mapR);
                        System.out.println("\n---------------------\nPrinting MOLD Layer");
                        print.printPATTERN(mapMoldLayer, 0, 1);
                        
                        Map<Integer, Map<Integer, Tile>> mapOnePattern = new HashMap<>();
                        mapOnePattern = pr.fillInitialMoldToFullPatternLayer(mapMoldLayer, ctimestamp2);
                        System.out.println("\n---------------------\nAfter Filling PATTERN Layer");
                        print.printPATTERN(mapOnePattern, 3, 1);
                        
                        Map<Integer, Map<Integer, Map<Integer, Tile>>> mapNewRow = new HashMap<>();
                        mapNewRow = MapReplicate.get(i);
                        mapNewRow.put(j, mapOnePattern);
                        MapReplicate.put(i, mapNewRow);
                        
                        ctimestamp2 = conf.options.getCtstamp();
                    }
                }
                
                timestamp = conf.options.getTimestamp();
                timestamp += Math.max(ctimestamp1, ctimestamp2);
                
                System.out.println("TS - CHECK: " + timestamp);
                
                conf.options.setTimestamp(timestamp);
                
                System.out.println("\n------------------------------\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR: " + e.getMessage());
        }

        System.out.println("\n-------------------------");
        
        System.out.println("TOTAL TIME STAMP: " + conf.options.getTimestamp());

        enTime = System.nanoTime();
        long elapsedTime = enTime - stTime;
        double seconds = (double) elapsedTime / 1000000000.0;
        System.out.println("TIME TAKEN: " + seconds + " seconds!!");
    }

}
