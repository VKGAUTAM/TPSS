/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.utils;

import com.arl.chips.components.Tile;
import com.arl.chips.components.mvTile;
import java.util.Random;

/**
 *
 * @author VKGautam
 */
public class TileMoldVerticalUtils {
    
    private TileMoldVerticalUtils() {
    }
    
    public static TileMoldVerticalUtils getInstance() {
        return TileUtilsHolder.INSTANCE;
    }
    
    private static class TileUtilsHolder {

        private static final TileMoldVerticalUtils INSTANCE = new TileMoldVerticalUtils();
    }
    
//    public int CheckValue(int value, String piname) {
//        if (value == 0 || value == 1) {
//            return value;
//        } else {
//            System.err.println("ERROR: Check the value of " + piname.toUpperCase().trim() + " pin!!");
//            System.exit(-1);
//            return value;
//        }
//    }
    
    public int isValid(int value) {
        value = (value == 0 || value == 1) ? value : -1;
        return value;
    }
    
    /**
     *
     * @param north
     * @param south
     * @param east
     * @param west
     * @return
     */
    public mvTile getTile(int north, int east, int south, int west) {
        mvTile tile = new mvTile();
        tile.setNORTH(isValid(north));
        tile.setSOUTH(isValid(south));
        tile.setEAST(isValid(east));
        tile.setWEST(isValid(west));
        
        
        // XOR
        if ( ( north == 0 ) && ( east == 0 ) && ( south == 0 ) && ( west == 0 ) ) {
            tile.setNAME("A");
        } else if ( ( north == 0 ) && ( east == 1 ) && ( south == 1 ) && ( west == 0 ) ) {
            tile.setNAME("B");
        } else if ( ( north == 1 ) && ( east == 1 ) && ( south == 0 ) && ( west == 1 ) ) {
            tile.setNAME("C");
        } else if ( ( north == 1 ) && ( east == 0 ) && ( south == 1 ) && ( west == 1 ) ) {
            tile.setNAME("D");
        } else {
            tile.setNAME(null);
        }
        
        return tile;
    }
    
    public mvTile getMatchingTileByName(String tilename) {
//        System.out.println("TILE NAME: " + tilename);
        mvTile tile = new mvTile();
        switch (tilename) {
            case "A": tile.setNORTH(0); tile.setEAST(0); tile.setSOUTH(0); tile.setWEST(0);tile.setNAME("A");
                      break;
            case "B": tile.setNORTH(0); tile.setEAST(1); tile.setSOUTH(1); tile.setWEST(0); tile.setNAME("B");
                      break;
            case "C": tile.setNORTH(1); tile.setEAST(1); tile.setSOUTH(0); tile.setWEST(1); tile.setNAME("C");
                      break;
            case "D": tile.setNORTH(1); tile.setEAST(0); tile.setSOUTH(1); tile.setWEST(1); tile.setNAME("D");
                      break;
            default:  tile.setNORTH(-1); tile.setEAST(-1); tile.setSOUTH(-1); tile.setWEST(-1); tile.setNAME(null);
                      break;
        }
//        PrintTile(this);
        return tile;
    }
    
    public mvTile getMatchingTile(int lr, int tb) {
        mvTile tile = new mvTile();
        
        //   XOR Table
        //================
        // 0 XOR 0 = 0 0
        // 1 XOR 1 = 0 0
        // 0 XOR 1 = 0 1
        // 1 XOR 0 = 0 1
        if ( ( lr == 0 ) && ( tb == 0 ) ) {
            tile.setWEST(0);
            tile.setSOUTH(0);
            tile.setEAST(0);
            tile.setNORTH(0);
            tile.setNAME("A");
        } else if ( ( lr == 1 ) && ( tb == 1 ) ) {
            tile.setWEST(0);
            tile.setSOUTH(1);
            tile.setEAST(1);
            tile.setNORTH(0);
            tile.setNAME("B");
        } else if ( ( lr == 0 ) && ( tb == 1 ) ) {
            tile.setWEST(1);
            tile.setSOUTH(1);
            tile.setEAST(0);
            tile.setNORTH(1);
            tile.setNAME("D");
        } else if ( ( lr == 1 ) && ( tb == 0 ) ) {
            tile.setWEST(1);
            tile.setSOUTH(0);
            tile.setEAST(1);
            tile.setNORTH(1);
            tile.setNAME("C");
        } else {
            tile.setWEST(-1);
            tile.setSOUTH(-1);
            tile.setEAST(-1);
            tile.setNORTH(-1);
            tile.setNAME(null);
        } 
        return tile;
    }

    public mvTile getSinglePinMatchingTile(int left, int bottom) {
        mvTile tile = new mvTile();
        //   XOR Table
        //================
        // 0 XOR 0 = 0 0
        // 1 XOR 1 = 0 0
        // 0 XOR 1 = 0 1
        // 1 XOR 0 = 0 1
        
        String tilename = "";
        
        if (left >= 0 && bottom >= 0) {
//            System.out.println("4 - LEFT: " + left + ", BOTTOM: " + bottom);
            mvTile ltile = getMatchingTile(left, bottom);
            
            if ( ltile.getNAME().equalsIgnoreCase("NULL")) {
                if ( bottom == 0 ) {
                    tilename = getTileName("AC", 2);
                } else if ( bottom == 1 ) {
                    tilename = getTileName("BD", 2);
                } else if ( left == 0 ) {
                    tilename = getTileName("AD", 2);
                } else if ( left == 1 ) {
                    tilename = getTileName("BC", 2);
                }
            }
            return getMatchingTileByName(tilename);
        }
//        else if ( left == -1 && bottom == -1 ) {
////            System.out.println("1 - LEFT: " + left + ", BOTTOM: " + bottom);
//            tilename = getTileName();
//        } 
        else if (left >= 0 && bottom == -1) {
//            System.out.println("2 - LEFT: " + left + ", BOTTOM: " + bottom);
            if ( left == 0 ) {
                tilename = getTileName("AD", 2);
            } else if ( left == 1 ) {
                tilename = getTileName("BC", 2);
            }
//            System.out.println("2 - TILENAME: " + tilename);
//            Tile tile = getMatchingTileByName(tilename);
//            return tile;
//        } else if (left >= 0 && bottom == -1) {
////            System.out.println("3 - LEFT: " + left + ", BOTTOM: " + bottom);
//            if ( left == 0 ) {
//                tilename = getTileName("AC", 2);
//            } else if ( left == 1 ) {
//                tilename = getTileName("BD", 2);
//            }
//            Tile tile = getMatchingTileByName(tilename);
//            return tile;
        } else {
            System.err.println("ERROR!!");
        }
        
//        System.out.println("TILE CHosen: " + tilename);
        return getMatchingTileByName(tilename);
    }
    
    public String getTileName(String tilenames, int total) {
//        String tilenames = "ABCD";
        Random rand = new Random(System.nanoTime());
        int random = Math.abs(rand.nextInt()) % total;
//        System.out.println("RAND: " + random);

        String tname = tilenames.charAt(random) + "";
//        System.out.println("CHAR: " + tname);
        return tname.trim();
    }
    
    public String getTileName() {
        String tilenames = "ABCD";
        Random rand = new Random(System.currentTimeMillis());
        int random = Math.abs(rand.nextInt()) % 4;
//        System.out.println("RAND: " + random);

        String tname = tilenames.charAt(random) + "";
//        System.out.println("CHAR: " + tname);
        return tname.trim();
    }
    
    public void PrintTile(Tile tile) { 
        System.out.println("\n--------------------\nNAME: " + tile.getNAME() + "\nNORTH: " + tile.getTOP()
                + "\nEAST: " + tile.getRIGHT() + "\nSOUTH: " + tile.getBOTTOM()
                + "\nWEST: " + tile.getLEFT() + "\n--------------------");
    }

    public void clear(mvTile tile) {
        tile.setNORTH(-1); tile.setEAST(-1);
        tile.setSOUTH(-1); tile.setWEST(-1);
        tile.setNAME(null);
    }
    
    public void CopyToTile(mvTile A, mvTile B) {
        B.setNAME(A.getNAME());
        B.setNORTH(A.getNORTH());
        B.setEAST(A.getEAST());
        B.setSOUTH(A.getSOUTH());
        B.setWEST(A.getWEST());
    }

    public void CopyTileToMvtile(Tile A, mvTile B) {
        B.setNAME(A.getNAME());
        B.setNORTH(A.getTOP());
        B.setEAST(A.getRIGHT());
        B.setSOUTH(A.getBOTTOM());
        B.setWEST(A.getLEFT());
    }
    public void CopyMvtileToTile(mvTile A, Tile B) {
        B.setNAME(A.getNAME());
        B.setTOP(A.getNORTH());
        B.setRIGHT(A.getEAST());
        B.setBOTTOM(A.getSOUTH());
        B.setLEFT(A.getWEST());
    }
    
    
    public mvTile getRandomTile() {
        String tname = getTileName();
        mvTile retile = getMatchingTileByName(tname);
        return retile;
    }


    public static void main(String[] args) {
        TileMoldVerticalUtils tu = TileMoldVerticalUtils.getInstance();
        int top = 1;
        mvTile current = tu.getSinglePinMatchingTile(-1, top);
        System.out.println("MV TILE: " + PrintUtils.getTileMVInfo(current));
    }
    
}
