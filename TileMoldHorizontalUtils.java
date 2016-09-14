/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.utils;

import com.arl.chips.components.Tile;
import com.arl.chips.components.mhTile;
import com.arl.chips.components.mvTile;
import java.util.Random;

/**
 *
 * @author VKGautam
 */
public class TileMoldHorizontalUtils {
    
    private TileMoldHorizontalUtils() {
    }
    
    public static TileMoldHorizontalUtils getInstance() {
        return TileUtilsHolder.INSTANCE;
    }
    
    private static class TileUtilsHolder {

        private static final TileMoldHorizontalUtils INSTANCE = new TileMoldHorizontalUtils();
    }
    
    public int CheckValue(int value, String piname) {
        if (value == 0 || value == 1) {
            return value;
        } else {
            System.err.println("ERROR: MH - Check the value of " + piname.toUpperCase().trim() + " pin!!");
            System.exit(-1);
            return value;
        }
    }
    
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
    public mhTile getTile(int north, int east, int south, int west) {
        mhTile tile = new mhTile();
        tile.setNORTH(isValid(north));
        tile.setSOUTH(isValid(south));
        tile.setEAST(isValid(east));
        tile.setWEST(isValid(west));
        
        
        // XOR
        if ( ( north == 0 ) && ( east == 0 ) && ( south == 0 ) && ( west == 0 ) ) {
            tile.setNAME("A");
        } else if ( ( north == 1 ) && ( east == 0 ) && ( south == 0 ) && ( west == 1 ) ) {
            tile.setNAME("B");
        } else if ( ( north == 0 ) && ( east == 1 ) && ( south == 1 ) && ( west == 1 ) ) {
            tile.setNAME("C");
        } else if ( ( north == 1 ) && ( east == 1 ) && ( south == 1 ) && ( west == 0 ) ) {
            tile.setNAME("D");
        } else {
            tile.setNAME("N");
        }
        
        return tile;
    }
    
    public mhTile getMatchingTileByName(String tilename) {
//        System.out.println("TILE NAME: " + tilename);
        mhTile tile = new mhTile();
        switch (tilename) {
            case "A": tile.setNORTH(0); tile.setEAST(0); tile.setSOUTH(0); tile.setWEST(0);tile.setNAME("A");
                      break;
            case "B": tile.setNORTH(1); tile.setEAST(0); tile.setSOUTH(0); tile.setWEST(1); tile.setNAME("B");
                      break;
            case "C": tile.setNORTH(0); tile.setEAST(1); tile.setSOUTH(1); tile.setWEST(1); tile.setNAME("C");
                      break;
            case "D": tile.setNORTH(1); tile.setEAST(1); tile.setSOUTH(1); tile.setWEST(0); tile.setNAME("D");
                      break;
            default:  tile.setNORTH(-1); tile.setEAST(-1); tile.setSOUTH(-1); tile.setWEST(-1); tile.setNAME("N");
                      break;
        }
//        PrintTile(this);
        return tile;
    }
    
    public mhTile getMatchingTile(int lr, int tb) {
        mhTile tile = new mhTile();
        
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
            tile.setWEST(1);
            tile.setSOUTH(0);
            tile.setEAST(0);
            tile.setNORTH(1);
            tile.setNAME("B");
        } else if ( ( lr == 1 ) && ( tb == 0 ) ) {
            tile.setWEST(1);
            tile.setSOUTH(1);
            tile.setEAST(1);
            tile.setNORTH(0);
            tile.setNAME("C");
        } else if ( ( lr == 0 ) && ( tb == 1 ) ) {
            tile.setWEST(0);
            tile.setSOUTH(1);
            tile.setEAST(1);
            tile.setNORTH(1);
            tile.setNAME("D");
        } else {
            tile.setWEST(-1);
            tile.setSOUTH(-1);
            tile.setEAST(-1);
            tile.setNORTH(-1);
            tile.setNAME("N");
        } 
        return tile;
    }

    public mhTile getSinglePinMatchingTile(int left, int top) {
        mhTile tile = new mhTile();
        //   XOR Table
        //================
        // 0 XOR 0 = 0 0
        // 1 XOR 1 = 0 0
        // 0 XOR 1 = 0 1
        // 1 XOR 0 = 0 1
        
        String tilename = "";
        
        if (left >= 0 && top >= 0) {
//            System.out.println("4 - TOP: " + top + ",  LEFT: " + left);
            tilename = getMatchingTile(left, top).getNAME();
            
            if ( tilename.equalsIgnoreCase("NULL")) {
                if ( top == 0 ) {
                    tilename = getTileName("AC", 2);
                } else if ( top == 1 ) {
                    tilename = getTileName("BD", 2);
                } else if ( left == 0 ) {
                    tilename = getTileName("AD", 2);
                } else if ( left == 1 ) {
                    tilename = getTileName("BC", 2);
                }
//                tile = getMatchingTileByName(tilename);
            }
//            return tile;
        } else if (left == -1 && top >= 0) {
//            System.out.println("2 - LEFT: " + left + ", BOTTOM: " + bottom);
            if ( top == 0 ) {
                tilename = getTileName("AC", 2);
            } else if ( top == 1 ) {
                tilename = getTileName("BD", 2);
            }
        }
        
        tile = getMatchingTileByName(tilename);
        return tile;
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
    
    public void PrintTile(mhTile tile) { 
        System.out.println("\n--------------------\nNAME: " + tile.getNAME() + "\nNORTH: " + tile.getNORTH()
                + "\nEAST: " + tile.getEAST() + "\nSOUTH: " + tile.getSOUTH()
                + "\nWEST: " + tile.getWEST() + "\n--------------------");
    }

    public void clear(mhTile tile) {
        tile.setNORTH(-1); tile.setEAST(-1);
        tile.setSOUTH(-1); tile.setWEST(-1);
        tile.setNAME("NULL");
    }
    
    public void CopyToTile(mhTile A, mhTile B) {
        B.setNAME(A.getNAME());
        B.setNORTH(A.getNORTH());
        B.setEAST(A.getEAST());
        B.setSOUTH(A.getSOUTH());
        B.setWEST(A.getWEST());
    }

    public void CopyMHToMVTile(mhTile A, mvTile B) {
        B.setNAME(A.getNAME());
        B.setNORTH(A.getNORTH());
        B.setEAST(A.getEAST());
        B.setSOUTH(A.getSOUTH());
        B.setWEST(A.getWEST());
    }

    public void CopyMhtileToTile(mhTile A, Tile B) {
        B.setNAME(A.getNAME());
        B.setTOP(A.getNORTH());
        B.setRIGHT(A.getEAST());
        B.setBOTTOM(A.getSOUTH());
        B.setLEFT(A.getWEST());
    }

    public mhTile getRandomTile() {
        String tname = getTileName();
        mhTile retile = getMatchingTileByName(tname);
        return retile;
    }

    
    public static void main(String[] args) {
        TileMoldHorizontalUtils tu = TileMoldHorizontalUtils.getInstance();
        int top = 1;
        mhTile current = tu.getSinglePinMatchingTile(1, 0);
        System.out.println("MH TILE: " + PrintUtils.getTileMHInfo(current));
    }
    
}
