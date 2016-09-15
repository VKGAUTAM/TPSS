/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.core;

import com.arl.chips.replicate.PerformReplication;
import com.arl.chips.components.Point;
import com.arl.chips.components.Tile;
import com.arl.chips.components.mhTile;
import com.arl.chips.components.mvTile;
import com.arl.chips.conf.Configuration;
import com.arl.chips.utils.PrintUtils;
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
public class KineticTPSWrapper {

    Configuration conf = null;
    PerformReplication pr = null;
    PrintUtils print = null;
    TileUtils tu = null;

    public KineticTPSWrapper() {
    }

    public Map<Integer, Map<Integer, Tile>> getInitialMoldPattern(Map<Integer, Map<Integer, Tile>> cmap) {
        conf = Configuration.getInstance();

        Tile mptile = tu.getRandomTile();
        Map<Integer, Tile> mapROW = cmap.get(conf.options.getROW_DEFAULT());
        mapROW.put(0, mptile);
        cmap.put(conf.options.getROW_DEFAULT(), mapROW);

        return getZeroPattern(cmap);
    }

    public Map<Integer, Map<Integer, Tile>> getZeroPattern(Map<Integer, Map<Integer, Tile>> cmap) {
        conf = Configuration.getInstance();

//        Map<Integer, Map<Integer, Tile>> cmap = new HashMap<>();
        TileMoldHorizontalUtils tmhu = TileMoldHorizontalUtils.getInstance();
        TileMoldVerticalUtils tmvu = TileMoldVerticalUtils.getInstance();

        System.out.println("Printing the PATTERN Layer:");
        print.printPATTERN(cmap, 3, 1);

        Map<Integer, Point> OnSet = new HashMap<>();
        OnSet = pr.getInitialMoldCoordinatePoints();
        print.printMapIntPoint(cmap, OnSet);

        Map<Integer, Point> OffSet = new HashMap<>();
        print.printMapIntPoint(OffSet);

        Map<Integer, Point> AdjSet = new HashMap<>();
        print.printMapIntPoint(AdjSet);

        boolean loopflag = true;
        while (loopflag) {
            int size = OnSet.keySet().size();
//            System.out.print("\n#(ON Positions): " + size);

            if (size > 0) {

                /* Choose an event: ON or OFF */
                int evtID = pr.getEvent(cmap, OnSet, OffSet);
//                System.out.print("\nEVENT ID: " + evtID);

                /* ON event */
                if (evtID == 1) {
//                    System.out.print(" - Inside ON event\n");

                    if (OnSet.size() > 0) {

                        List<Integer> hcodes = new LinkedList<>();
                        boolean addAll = hcodes.addAll(OnSet.keySet());

                        Random rand = new Random(System.nanoTime());
                        int ranum = Math.abs(rand.nextInt()) % (OnSet.keySet().size());
                        int phash = hcodes.get(ranum);
                        Point cpoint = OnSet.get(phash);
                        System.out.print("\nON event at: " + "(" + cpoint.getI() + ", " + cpoint.getJ() + ")");

                        Tile ratile = new Tile();
                        if (cpoint.getJ() == 0 && cpoint.getI() < conf.options.getROW_DEFAULT()) {
                            mvTile mvt = tmvu.getRandomTile();
                            tmvu.CopyMvtileToTile(mvt, ratile);
                        } else {
                            if (cpoint.getI() == conf.options.getROW_DEFAULT() && cpoint.getJ() <= conf.options.getCOL_DEFAULT() ) {
                                mhTile mht = tmhu.getRandomTile();
                                tmhu.CopyMhtileToTile(mht, ratile);
                            }
                        }
                        System.out.print(" - " + ratile.getNAME() + "\n");

                        /* Obtain all adjacent points */
                        List<Point> adjpoints = new LinkedList<>();
                        adjpoints = pr.getMoldAdjacentPoints(cmap, cpoint);
//                        System.out.println("\nPrinting MOLD ADJ Points:");
//                        print.printList(adjpoints);


                        /* Get Neighboring Points */
                        Map<Integer, Point> npmap = pr.getMoldNeighborsMap(cmap, cpoint);
//                        System.out.println("\nPrinting MOLD NEIGHBOUR Points:");
//                        print.printMapIntPoint(npmap);

                        int ctr = 0;

                        /* Update Tile at the current position */
                        Map<Integer, Tile> mapROW = new HashMap<>();
                        mapROW = cmap.get(cpoint.getI());
                        mapROW.put(cpoint.getJ(), ratile);
                        cmap.put(cpoint.getI(), mapROW);

                        ctr = pr.getMoldPinCount(cmap, cpoint, npmap);

                        if (ctr > 0) {

                            /* Get Pin Match Count for the current position */
//                            pr.updatePinCounts(cmap, cpoint, npmap);
                            pr.updateMoldPinCounts(cmap, cpoint, npmap);

                            Iterator<Point> ajiter = adjpoints.iterator();
                            while (ajiter.hasNext()) {
                                Point adjp = ajiter.next();
                                String hashstr = adjp.getI() + ", " + adjp.getJ();
                                int hcode = hashstr.hashCode();

                                if (!OffSet.containsKey(hcode)) {
                                    OnSet.put(hcode, adjp);
                                }
                            }

                            System.out.println("\n--------------------------" + "\nON event @ ("
                                    + cpoint.getI() + ", " + cpoint.getJ() + ") - " + ratile.getNAME());

                            /* Update HPoints */
                            OnSet.remove(phash);

                            String rimcode = cpoint.getI() + ", " + cpoint.getJ();
                            OffSet.put(rimcode.hashCode(), cpoint);

//                            System.out.println("Printing OFF set: " + OffSet.size());
//                            print.printMapIntPoint(OffSet);
                            print.printPATTERN(cmap, 3, 1);

                        } else {

                            System.out.println("No PINs Match @ (" + cpoint.getI() + ", " + cpoint.getJ() + ") - " + ratile.getNAME());

                            /* Remove the currently added Tile */
                            mapROW = cmap.get(cpoint.getI());
                            ratile = new Tile();
                            mapROW.put(cpoint.getJ(), ratile);
                            cmap.put(cpoint.getI(), mapROW);
                        }

                    } else {
//                                        System.out.println("On Set is empty! No ON event is possible!!");
                        System.out.println("No ON event is possible!!");
                    }
                    // end of ON event
                } else if (evtID == 0) {
                    /* OFF event starts from here */

                    System.out.println("Inside OFF event");

                    if (OffSet.size() > 0) {

                        /* Perform OFF event */
                        int phash = pr.getOffPoint(cmap, OffSet);
                        Point cpoint = OffSet.get(phash);

                        System.out.println("\n--------------------------"
                                + "\nOFF event @ (" + cpoint.getI() + ", " + cpoint.getJ() + ")");


                        /* Get Pin Match Count for the current position */
                        pr.updateMoldPinCountsAfterRemoval(cmap, cpoint);

                        /* Remove the currently added Tile from MAP */
                        Map<Integer, Tile> mapROW = new HashMap<>();
                        mapROW = cmap.get(cpoint.getI());
                        Tile remtile = mapROW.get(cpoint.getJ());
                        remtile.clear();
                        mapROW.put(cpoint.getJ(), remtile);
                        cmap.put(cpoint.getI(), mapROW);

                        /* Remove the current position from OFF set and add it to ON set */
                        OffSet.remove(phash);
                        OnSet.put(phash, cpoint);

                        print.printPATTERN(cmap, 3, 1);
                    }

                    // end of OFF event
                }

            } else {
                loopflag = false;
            }

        }   // end of While loop

//        Map<Integer, Tile> mapROW = cmap.get(conf.options.getROW_DEFAULT());
//        Tile ctile = mapROW.get(0);
//        ctile.setPinmatch(2);
//        mapROW.put(0, ctile);
//        cmap.put(conf.options.getROW_DEFAULT(), mapROW);
        return cmap;
    }

    public Map<Integer, Map<Integer, Tile>> getOnePattern(Map<Integer, Map<Integer, Tile>> cmap) {
        cmap = pr.getCandiateMap(cmap);

        System.out.println("Initial CANDIATE MAP:");
        print.printPATTERN(cmap, 3, 1);

        Map<Integer, Point> OnSet = new HashMap<>();
        OnSet = pr.getInitialCoordinatePoints();
        System.out.println("Priting ON SET:");
        print.printMapIntPoint(OnSet);

        Map<Integer, Point> OffSet = new HashMap<>();
        System.out.println("\nPriting OFF SET:");
        print.printMapIntPoint(OffSet);

        Map<Integer, Point> AdjSet = new HashMap<>();
        System.out.println("\nPriting Adjacent SET:");
        print.printMapIntPoint(AdjSet);

        boolean loopflag = true;
        while (loopflag) {
            int size = OnSet.keySet().size();
            System.out.print("\n#(ON Positions): " + size);

            if (size > 0) {

                /* Choose an event: ON or OFF */
                int evtID = pr.getEvent(cmap, OnSet, OffSet);

//                    int evtID = 1;
                System.out.print("\nEVENT ID: " + evtID);

                /* ON event */
                if (evtID == 1) {
//                    System.out.print(" - Inside ON event\n");
                    if (OnSet.size() > 0) {

                        List<Integer> hcodes = new LinkedList<>();
                        boolean addAll = hcodes.addAll(OnSet.keySet());

                        Random rand = new Random(System.nanoTime());
                        int ranum = Math.abs(rand.nextInt()) % (OnSet.keySet().size());
                        int phash = hcodes.get(ranum);
                        Point cpoint = OnSet.get(phash);

                        /* Obtain all adjacent points */
                        List<Point> adjpoints = new LinkedList<>();
                        adjpoints = pr.getAdjacentPoints(cmap, cpoint);
//                            System.out.println("\nPrinting ADJ Points:");
//                            print.printList(adjpoints);
//                            System.out.println("\n-----------------");

                        /* Get Neighboring Points */
                        Map<Integer, Point> npmap = pr.getNeighborsMap(cmap, cpoint);
//                        print.printMapIntPoint(npmap);

                        int ctr = 0;

                        Tile ratile = tu.getRandomTile();
                        System.out.println(OnSet.keySet().size() + " - ON event at: " + "(" + cpoint.getI() + ", " + cpoint.getJ() + ") - " + ratile.getNAME());

                        /* Update Tile at the current position */
                        Map<Integer, Tile> mapROW = new HashMap<>();
                        mapROW = cmap.get(cpoint.getI());
                        mapROW.put(cpoint.getJ(), ratile);
                        cmap.put(cpoint.getI(), mapROW);

                        ctr = pr.getPinCount(cmap, cpoint, npmap);

                        if (ctr > 0) {

                            int pincount = 0;

                            /* Get Pin Match Count for the current position */
                            pr.updatePinCounts(cmap, cpoint, npmap);

                            Iterator<Point> ajiter = adjpoints.iterator();
                            while (ajiter.hasNext()) {
                                Point adjp = ajiter.next();
                                String hashstr = adjp.getI() + ", " + adjp.getJ();
                                int hcode = hashstr.hashCode();

                                if (!OffSet.containsKey(hcode)) {
                                    OnSet.put(hcode, adjp);
                                }
                            }

                            /* Update HPoints */
                            OnSet.remove(phash);

                            String rimcode = cpoint.getI() + ", " + cpoint.getJ();
                            OffSet.put(rimcode.hashCode(), cpoint);

//                                System.out.println("Printing OFF set: " + OffSet.size());
//                                print.printMapIntPoint(OffSet);
                            print.printPATTERN(cmap, 3, 1);

                        } else {

//                            System.out.println("No PINs Match @ (" + cpoint.getI() + ", " + cpoint.getJ() + ") - " + ratile.getNAME());

                            /* Remove the currently added Tile */
                            mapROW = cmap.get(cpoint.getI());
                            ratile = new Tile();
                            mapROW.put(cpoint.getJ(), ratile);
                            cmap.put(cpoint.getI(), mapROW);
                        }

                    } else {
                        System.out.println("On Set is empty! No ON event is possible!!");
                    }
                    // end of ON event
                } else if (evtID == 0) {
                    /* OFF event starts from here */
                    System.out.println("Inside OFF event");

                    if (OffSet.size() > 0) {

                        /* Perform OFF event */
                        int phash = pr.getOffPoint(cmap, OffSet);
                        Point cpoint = OffSet.get(phash);

                        System.out.println("\n--------------------------"
                                + "\nOFF event @ (" + cpoint.getI() + ", " + cpoint.getJ() + ")");

                        /* Get Pin Match Count for the current position */
                        pr.updatePinCountsAfterRemoval(cmap, cpoint);

                        /* Remove the currently added Tile from MAP */
                        Map<Integer, Tile> mapROW = new HashMap<>();
                        mapROW = cmap.get(cpoint.getI());
                        Tile remtile = mapROW.get(cpoint.getJ());
                        remtile.clear();
                        mapROW.put(cpoint.getJ(), remtile);
                        cmap.put(cpoint.getI(), mapROW);

                        /* Remove the current position from OFF set and add it to ON set */
                        OffSet.remove(phash);
                        OnSet.put(phash, cpoint);

//                            System.out.println("Inside OFF: Priting ON set: ");
//                            print.printMapIntPoint(OnSet);
//                            System.out.println("Inside OFF: Priting OFF set: ");
//                            print.printMapIntPoint(OffSet);
                        print.printPATTERN(cmap, 3, 1);
                    }

                    // end of OFF event
                }

            } else {
                loopflag = false;
            }

        }   // end of While loop

        return cmap;
    }

    public void PerformKineticTPS(Configuration conf) {
        long timestamp = 0;

        pr = new PerformReplication(conf);
        print = PrintUtils.getInstance();
        tu = TileUtils.getInstance();

        System.out.println("TILES:\n-----------------");
        System.out.println(tu.getMatchingTileByName("A"));
        System.out.println(tu.getMatchingTileByName("B"));
        System.out.println(tu.getMatchingTileByName("C"));
        System.out.println(tu.getMatchingTileByName("D"));
        System.out.println();

        try {

            /* Initialize PATTERN */
            Map<Integer, Map<Integer, Tile>> myPattern = new HashMap<>();
            myPattern = pr.getPattern(conf);
            System.out.println("Initial Configuration - Pattern Layer");
            print.printPATTERN(myPattern, 3, 1);
            /* Print ALL LAYERS */

 /* Create SEED layer */
            myPattern = pr.createSeedLayer(myPattern);
            print.printPATTERN(myPattern, 1, 1);

            myPattern = pr.fillInitialMoldToPatternLayer(myPattern, timestamp);
            print.printPATTERN(myPattern, 2, 1);

            Map<Integer, Map<Integer, Tile>> mapML = new HashMap<>();
//            mapML = pr.fillMoldFromSeedLayer(myPattern);

            mapML = getInitialMoldPattern(myPattern);
            print.printPATTERN(mapML, 0, 1);
            print.printPATTERN(myPattern, 3, 1);

            int MAX_ITERATIONS = conf.options.getMAX_ITERATIONS();
            System.out.println("MAX ITERATIONS: " + MAX_ITERATIONS);

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
                    System.out.println("PROCESSING ... (" + prevrow + ", " + prevcol + ") ==> (" + i + ", " + j + ")");

                    Map<Integer, Map<Integer, Tile>> mapR = new HashMap<>();
                    mapR = MapReplicate.get(prevrow).get(prevcol);

                    if ((j % 2) == 0) {

                        System.out.println("CREATING MOLD FROM PATTERN LAYER: ");

                        ctimestamp1 = 0;

                        Map<Integer, Map<Integer, Tile>> mapSeedLayer = new HashMap<>();
                        mapSeedLayer = pr.segmentSeedLayer(mapR);
                        System.out.println("\n---------------------\nPrinting PATTERN");
                        print.printPATTERN(mapSeedLayer, 2, 1);

                        Map<Integer, Map<Integer, Tile>> mapZeroPattern = new HashMap<>();
                        mapZeroPattern = getZeroPattern(mapSeedLayer);
                        System.out.println("\n---------------------\nAfter Filling MOLD");
                        print.printPATTERN(mapZeroPattern, 3, 1);
                        
                        Map<Integer, List<Point>> mapMLStats = new HashMap<>();
                        mapMLStats = pr.getMoldPinMatchStats(mapZeroPattern);
                        print.printPinCountStats(mapMLStats);

                        Map<Integer, Map<Integer, Map<Integer, Tile>>> mapNewRow = new HashMap<>();
                        mapNewRow = MapReplicate.get(i);
                        mapNewRow.put(j, mapZeroPattern);
                        MapReplicate.put(i, mapNewRow);
//
                        ctimestamp1 = conf.options.getCtstamp();

                    }
                    if ((j % 2) == 1) {
                        ctimestamp2 = 0;
                        System.out.println("CREATING PATTERN FROM MOLD LAYER: ");

                        Map<Integer, Map<Integer, Tile>> mapMoldLayer = new HashMap<>();
                        mapMoldLayer = pr.segmentMoldLayer(myPattern);
                        System.out.println("\n---------------------\nPrinting MOLD");
                        print.printPATTERN(mapMoldLayer, 0, 1);

                        Map<Integer, Map<Integer, Tile>> mapOnePattern = new HashMap<>();
                        mapOnePattern = getOnePattern(mapMoldLayer);
                        System.out.println("\n---------------------\nAfter Filling PATTERN");
                        print.printPATTERN(mapOnePattern, 3, 1);
                        
                        Map<Integer, List<Point>> mapPLStats = new HashMap<>();
                        mapPLStats = pr.getPatternPinMatchStats(mapOnePattern);
                        print.printPinCountStats(mapPLStats);

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
        System.out.println("\n");

    }

    public static void main(String[] args) {
        long stTime, enTime;
        stTime = System.nanoTime();

        KineticTPSWrapper ktps = new KineticTPSWrapper();
        Configuration conf = Configuration.getInstance();

        /* Only PATTERN Size */
        int row = 6, column = 8;
        conf.options.setGrid(row, column);
        conf.options.setG_mc(16.0f);
        conf.options.setG_se(7.8f);
        conf.options.setK_f(1000000);

        conf.options.setGrid(row, column);

        /* Perform Self REPLICATION Process */
        ktps.PerformKineticTPS(conf);

        enTime = System.nanoTime();
        long elapsedTime = enTime - stTime;
        double seconds = (double) elapsedTime / 1000000000.0;
        System.out.println("TIME TAKEN: " + seconds + " seconds!!");

    }

}
