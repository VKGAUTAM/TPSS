/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.utils;

import com.arl.chips.components.Point;
import com.arl.chips.components.Tile;
import com.arl.chips.components.mhTile;
import com.arl.chips.components.mvTile;
import com.arl.chips.conf.Configuration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author VKGautam
 */
public class PrintUtils {

    Configuration conf;

    private PrintUtils() {
        conf = Configuration.getInstance();
    }

    public static PrintUtils getInstance() {
        return PrintUtilsHolder.INSTANCE;
    }

    private static class PrintUtilsHolder {

        private static final PrintUtils INSTANCE = new PrintUtils();
    }

    public void printMap(Map<Object, Object> map) {
        Set<Object> keySet = map.keySet();
        Iterator<Object> iter = keySet.iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = map.get(key);
            System.out.println("KEY: " + key + " - " + value.toString());
        }
    }
    
    public void printList(List<Point> points) {
        Iterator<Point> liter = points.iterator();
        while (liter.hasNext()) {
            Point p = liter.next();
            System.out.println("(" + p.getI() + ", " + p.getJ() + ")");
        }
    }
    
    public void printListTile(List<Point> points, Map<Integer, Map<Integer, Tile>> cmap) {
        Iterator<Point> liter = points.iterator();
        while (liter.hasNext()) {
            Point p = liter.next();
            Tile tile = cmap.get(p.getI()).get(p.getJ());
            System.out.println("(" + p.getI() + ", " + p.getJ() + ") - " + tile.toString());
        }
    }
    
    public void printMapIntPoint(Map<Integer, Point> map) {
        if (map.size() > 0) {
            Iterator<Integer> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                int key = iter.next();
                Point p = map.get(key);
                System.out.println("KEY: " + key + " - (" + p.getI() + ", " + p.getJ() + ")");
            }
        } else {
            System.out.println("STATUS: Set is EMPTY!");
        }
    }

    public void printMapIntPoint(Map<Integer, Map<Integer, Tile>> mapSL, Map<Integer, Point> map) {
        if (map.size() > 0) {
            Iterator<Integer> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                int key = iter.next();
                Point p = map.get(key);
                String tilename = mapSL.get(p.getI()).get(p.getJ()).getNAME();
                
                System.out.println("KEY: " + key + " - (" + p.getI() + ", " + p.getJ() + ") - " + tilename );
            }
        } else {
            System.out.println("STATUS: Set is EMPTY!");
        }
    }

    public void printMapID(Map<Integer, Double> map) {
        Set<Integer> keySet = map.keySet();
        Iterator<Integer> iter = keySet.iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            double value = map.get(key);
            System.out.println("KEY: " + key + " - " + value);
        }
    }
    
    public void printMapDoublePoint(Map<Double, Point> map) {
        Iterator<Double> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            double key = iter.next();
            Point p = map.get(key);
            System.out.println("KEY: " + key + " - (" + p.getI() + ", " + p.getJ() + ")");
        }
    }
    
    public void printMapDoubleListPoint(Map<Double, Set<Point>> map) {
        Iterator<Double> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            double key = iter.next();
//            System.out.print("\nKEY: " + key + " - ");
            Set<Point> points = map.get(key);
            Iterator<Point> liter = points.iterator();
            while (liter.hasNext()) {
                Point p = liter.next();
                System.out.println(" (" + p.getI() + ", " + p.getJ() + ")");
            }
        }
    }
    
    public void printMapIID(Map<Integer, Map<Integer, Double>> mapID) {
        Set<Integer> keySet = mapID.keySet();
        Iterator<Integer> iter = keySet.iterator();
        while (iter.hasNext()) {
            int i = iter.next();
            
            Map<Integer, Double> mapinner = mapID.get(i);
            Iterator<Integer> initer = mapinner.keySet().iterator();
            while (initer.hasNext()) {
                int j = initer.next();
                System.out.println("(" + i + " , " + j + ") - " + mapinner.get(j));
            }
//            System.out.println("\n");
        }
    }

    public void PrintTile(Tile tile) {
        System.out.println(getTileInfo(tile));
    }

    public static String getTileInfo(Tile tile) {
        String tilename = "";
        tilename = tile.getNAME();
        tilename = (tilename.equalsIgnoreCase(null)) ? "N" : tilename ;
        if (tilename.equalsIgnoreCase(null)) tilename = "";
        return "{" + tilename + ", " + tile.getTOP() + ", " + tile.getRIGHT() + ", " + tile.getBOTTOM()
                + ", " + tile.getLEFT() + ", " + tile.getDirflag() + ", " + tile.getKscore() + ", " + tile.getPinmatch() + " } ";

//        return " NAME: " + tile.getNAME() + ", NORTH: " + tile.getNORTH()
//                + ", EAST: " + tile.getEAST() + ", SOUTH: " + tile.getSOUTH()
//                + ", WEST: " + tile.getWEST() + "\n";
    }

    public void PrintTile(mvTile tile) {
        System.out.println(getTileMVInfo(tile));
    }

    public static String getTileMVInfo(mvTile tile) {
        return "{" + tile.getNAME() + ", " + tile.getNORTH() + ", " + tile.getEAST() + ", " + tile.getSOUTH()
                + ", " + tile.getWEST() + "} ";

//        return " NAME: " + tile.getNAME() + ", NORTH: " + tile.getNORTH()
//                + ", EAST: " + tile.getEAST() + ", SOUTH: " + tile.getSOUTH()
//                + ", WEST: " + tile.getWEST() + "\n";
    }

    public void PrintTile(mhTile tile) {
        System.out.println(getTileMHInfo(tile));
    }

    public static String getTileMHInfo(mhTile tile) {
        return "{" + tile.getNAME() + ", " + tile.getNORTH() + ", " + tile.getEAST() + ", " + tile.getSOUTH()
                + ", " + tile.getWEST() + "} ";
    }

    public static void printMVGrid(Map<Integer, Map<Integer, mvTile>> map) {
        System.out.println("\n-------------------");
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            System.out.print(key + ": ");

            Map<Integer, mvTile> value = map.get(key);
            Iterator<Integer> viter = value.keySet().iterator();
            while (viter.hasNext()) {
                int cval = (int) viter.next();
                mvTile tile = value.get(cval);
                System.out.print(cval + " - " + getTileMVInfo(tile));
            }
            System.out.println("\n");
        }
        System.out.println("-------------------\n");
    }

    public static void printMHGrid(Map<Integer, Map<Integer, mhTile>> map) {
        System.out.println("\n-------------------");
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            System.out.print(key + ": ");

            Map<Integer, mhTile> value = map.get(key);
            Iterator<Integer> viter = value.keySet().iterator();
            while (viter.hasNext()) {
                int cval = (int) viter.next();
                mhTile tile = value.get(cval);
                System.out.print(cval + " - " + getTileMHInfo(tile));
            }
            System.out.println("\n");
        }
        System.out.println("-------------------");
    }

    public static void printGridOnlyNames(Map<Integer, Map<Integer, Tile>> map) {
        System.out.println();
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            int key = iter.next();
//            System.out.print(key + ": ");
            System.out.println();

            Map<Integer, Tile> value = map.get(key);
            Iterator<Integer> viter = value.keySet().iterator();
            while (viter.hasNext()) {
                int cval = (int) viter.next();
                Tile tile = value.get(cval);
//                System.out.print( cval + " - " + getTileInfo(tile) );

//                System.out.print( " " + tile.getNAME() + " " );
                if (tile.getNAME() == "A") {
                    ANSIColor.magenta("Ⓐ");
                } else if (tile.getNAME() == "C") {
                    ANSIColor.red("Ⓒ");
                } else if (tile.getNAME() == "B") {
                    ANSIColor.green("Ⓑ");
                } else if (tile.getNAME() == "D") {
                    ANSIColor.blue("Ⓓ");
                } else {
//                    ANSIColor.yellow(" " + tile.getNAME() + " ");
                    ANSIColor.yellow("  ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("\n-------------------");
    }

    public static void printGridMHOnlyNames(Map<Integer, Map<Integer, mhTile>> map) {
        System.out.println();
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            int key = iter.next();
//            System.out.print(key + ": ");
            System.out.println("\n");

            Map<Integer, mhTile> value = map.get(key);
            Iterator<Integer> viter = value.keySet().iterator();
            while (viter.hasNext()) {
                int cval = (int) viter.next();
                mhTile tile = value.get(cval);
//                System.out.print( cval + " - " + getTileInfo(tile) );

//                System.out.print( " " + tile.getNAME() + " " );
                if (tile.getNAME() == "A") {
                    ANSIColor.magenta("  Ⓐ  ");
                } else if (tile.getNAME() == "C") {
                    ANSIColor.red("  Ⓒ  ");
                } else if (tile.getNAME() == "B") {
                    ANSIColor.green("  Ⓑ  ");
                } else if (tile.getNAME() == "D") {
                    ANSIColor.blue("  Ⓓ  ");
                } else {
//                    ANSIColor.yellow(" " + tile.getNAME() + " ");
                    ANSIColor.yellow("    ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("\n-------------------");
    }

    public static void printMVGridOnlyNames(Map<Integer, Map<Integer, mvTile>> map) {
        System.out.println();
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            int key = iter.next();
//            System.out.print(key + ": ");
            System.out.println();

            Map<Integer, mvTile> value = map.get(key);
            Iterator<Integer> viter = value.keySet().iterator();
            while (viter.hasNext()) {
                int cval = (int) viter.next();
                mvTile tile = value.get(cval);
//                System.out.print( cval + " - " + getTileInfo(tile) );

//                System.out.print( " " + tile.getNAME() + " " );
                if (tile.getNAME() == "A") {
                    ANSIColor.magenta("Ⓐ");
                } else if (tile.getNAME() == "C") {
                    ANSIColor.red("Ⓒ");
                } else if (tile.getNAME() == "B") {
                    ANSIColor.green("Ⓑ");
                } else if (tile.getNAME() == "D") {
                    ANSIColor.blue("Ⓓ");
                } else {
//                    ANSIColor.yellow(" " + tile.getNAME() + " ");
                    ANSIColor.yellow("    ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("\n-------------------");
    }

    public static void printColor(Tile tile) {
        String tilename = tile.getNAME();
        if (tilename == null) 
//            tilename = "Ⓝ";
            tilename = "N";
        
//        String others = "(" + tile.toString() + ")";
        String others = "(" + tile.getPinmatch() + ")";
        printColor(tilename, others);
    }

    public static void printColor(String tilename) {
        if (tilename == "A") {
            ANSIColor.magenta("Ⓐ");
        } else if (tilename == "C") {
            ANSIColor.red("Ⓒ");
        } else if (tilename == "B") {
            ANSIColor.green("Ⓑ");
        } else if (tilename == "D") {
            ANSIColor.blue("Ⓓ");
        } else {
            ANSIColor.teal(tilename);
        }
    }

    public static void printColor(String tilename, String others) {
        if (tilename == "A") {
            ANSIColor.magenta("Ⓐ" + others);
        } else if (tilename == "C") {
            ANSIColor.red("Ⓒ" + others);
        } else if (tilename == "B") {
            ANSIColor.green("Ⓑ" + others);
        } else if (tilename == "D") {
            ANSIColor.blue("Ⓓ" + others);
        } else {
            ANSIColor.teal(tilename + others);
        }
    }

    public static void printAllGridOnlyNames(Map<Integer, Map<Integer, Tile>> paGrid, Map<Integer, Map<Integer, mvTile>> moGrid) {
        System.out.println();

        int i = 1, j = 0;

        Iterator<Integer> iter = paGrid.keySet().iterator();
        while (iter.hasNext()) {
            int key = iter.next();
//            System.out.print(key + ": ");
            System.out.println();

            String moelename = moGrid.get(i).get(j).getNAME();
            printColor(moelename);
            i++;

            Map<Integer, Tile> value = paGrid.get(key);
            Iterator<Integer> viter = value.keySet().iterator();

            while (viter.hasNext()) {
                int cval = (int) viter.next();
                Tile tile = value.get(cval);
//                System.out.print( cval + " - " + getTileInfo(tile) );
                printColor(tile.getNAME());
//                System.out.print( " " + tile.getNAME() + " " );
            }

            if (i == moGrid.keySet().size()) {
                System.out.println("\n");
                j = 0;
                Map<Integer, mvTile> mhtiles = moGrid.get(i);
                for (j = 0; j < mhtiles.size(); j++) {
                    printColor(mhtiles.get(j).getNAME());
                }
            }

            System.out.println("\n");
        }

        System.out.println("\n");
    }

    public void printOnlySeedLayer(Map<Integer, Map<Integer, Tile>> mapPAT, int type) {
//        System.out.println("\nPrinting the SEED LAYER\n-------------------");
        int i = 0;
        int j = 1;
        for (i = 0; i < conf.options.getROW_DEFAULT(); i++) {
            Map<Integer, Tile> value = mapPAT.get(i);
            Tile tile = value.get(j);
            if (type == 1) {
                printColor(tile);
            } else {
                System.out.print(getTileInfo(tile));
            }
            if (i == (conf.options.getROW_DEFAULT() - 1)) {
                for (j = 2; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                    tile = value.get(j);
                    if (type == 1) {
                        printColor(tile);
                    } else {
                        System.out.print(getTileInfo(tile));
                    }
                }
            }
            System.out.println("\n");
        }

        System.out.println("\n");
    }

    public void printOnlyPattern(Map<Integer, Map<Integer, Tile>> mapPAT, int type) {
//        System.out.println("\nPrinting the Target PATTERN\n-------------------");
        int i, j;
        for (i = 0; i < conf.options.getROW_DEFAULT(); i++) {
            Map<Integer, Tile> value = mapPAT.get(i);
            for (j = 1; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                Tile tile = value.get(j);
                if (type == 1) {
                    printColor(tile);
                } else {
                    System.out.print(getTileInfo(tile));
                }
            }
            System.out.println("\n");
        }

        System.out.println("\n");
    }

    public void printOnlyMoldLayer(Map<Integer, Map<Integer, Tile>> mapPAT, int type) {
        if (mapPAT != null) {
//            System.out.println("\nPrinting the MOLD\n-------------------");
            int i = 0;
            int j = 0;
            for (i = 0; i < (conf.options.getROW_DEFAULT() + 1); i++) {
                Tile tile;
                Map<Integer, Tile> value = mapPAT.get(i);
                if (value != null) {
                    tile = value.get(j);
                    if (type == 1) {
                        printColor(tile);
                    } else {
                        System.out.print(getTileInfo(tile));
                    }
                } else {
                    System.out.println("ERROR ... !!");
                }
                if (i == conf.options.getROW_DEFAULT()) {
                    for (j = 1; j < (conf.options.getCOL_DEFAULT() + 1); j++) {
                        Tile ctile = new Tile();
                        ctile = value.get(j);

                        if (type == 1) {
                            printColor(ctile);
                        } else {
                            System.out.print(getTileInfo(ctile));
                        }
                    }
                }
                System.out.println("\n");
            }

            System.out.println("\n");
        } else {
            System.out.println("NULL MOLD");
        }
    }

    public void printAllLayer(Map<Integer, Map<Integer, Tile>> mapPAT, int type) {
        if (mapPAT == null) {
            System.out.println("NULL PATTERN!!");
        } else {
//        System.out.println("\nPrinting ALL Layers\n-------------------");
            Iterator<Integer> iter = mapPAT.keySet().iterator();
            while (iter.hasNext()) {
                int key = iter.next();
                Map<Integer, Tile> value = mapPAT.get(key);
                Iterator<Integer> viter = value.keySet().iterator();
                while (viter.hasNext()) {
                    int cval = (int) viter.next();
                    Tile tile = value.get(cval);

                    if (type == 1) {
                        printColor(tile);
                    } else {
                        System.out.println(getTileInfo(tile));
                    }
                }
                System.out.println("\n");
            }
            System.out.println("\n");
        }
    }

    public void printPATTERN(Map<Integer, Map<Integer, Tile>> mapPAT, int tag, int tiledetails) {
        if (mapPAT != null) {
            /* Type = 0, all values of Tiles, and = 1, only names of tiles */
            int TYPE = (tiledetails < 0) ? 0 : 1;

            // Print Only MOLD LAYER
            if (tag == 0) {
                printOnlyMoldLayer(mapPAT, TYPE);
            } else if (tag == 1) { // Print Only SEED LAYER
                printOnlySeedLayer(mapPAT, TYPE);
            } else if (tag == 2) { // Print THE PATTERN
                printOnlyPattern(mapPAT, TYPE);
            } else { // Print MOLD + PATTERN
                printAllLayer(mapPAT, TYPE);
            }
        } else {
            System.out.println("ERROR: NULL PATTERN !!");
        }
    }
    
    public void printMapStrInt(Map<String, Integer> mapSI) {
        Iterator<String> iter = mapSI.keySet().iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            double kscore = mapSI.get(str);
            System.out.println( str + " " + kscore );
        }
    }

    public void printMapPointScore(Map<Point, Double> mapPD) {
        Iterator<Point> iter = mapPD.keySet().iterator();
        while (iter.hasNext()) {
            Point p = iter.next();
            double kscore = mapPD.get(p);
            System.out.println( "(" + p.getI() + ", " + p.getJ() + ") - " + kscore );
        }
    }
    
    
    public void printPinCountStats(Map<Integer, List<Point>> mapPS) {
        System.out.println("\nMold Layer Pin Statistics\n--------------------------------------");
        System.out.println("Pin Match - #Tiles");
        Iterator<Integer> iter = mapPS.keySet().iterator();
        while (iter.hasNext()) {
            int pc = iter.next();
            List<Point> lpoints = mapPS.get(pc);
            System.out.println( "    " + pc + "    -     " + lpoints.size());
        }
        System.out.println("--------------------------------------");
    }

}
