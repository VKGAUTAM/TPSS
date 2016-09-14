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
public class TileUtils {
    
    private TileUtils() {
    }
    
    public static TileUtils getInstance() {
        return TileUtilsHolder.INSTANCE;
    }
    
    private static class TileUtilsHolder {

        private static final TileUtils INSTANCE = new TileUtils();
    }
    
    public int CheckValue(int value, String piname) {
        if (value == 0 || value == 1) {
            return value;
        } else {
            System.err.println("ERROR: TU - Check the value of " + piname.toUpperCase().trim() + " pin!!");
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
    public Tile getTile(int north, int east, int south, int west) {
        Tile tile = new Tile();
        tile.setTOP(isValid(north));
        tile.setBOTTOM(isValid(south));
        tile.setRIGHT(isValid(east));
        tile.setLEFT(isValid(west));
        
        
        // XOR
        if ( ( north == 0 ) && ( east == 0 ) && ( south == 0 ) && ( west == 0 ) ) {
            tile.setNAME("A");
        } else if ( ( north == 0 ) && ( east == 0 ) && ( south == 1 ) && ( west == 1 ) ) {
            tile.setNAME("B");
        } else if ( ( north == 1 ) && ( east == 1 ) && ( south == 1 ) && ( west == 0 ) ) {
            tile.setNAME("C");
        } else if ( ( north == 1 ) && ( east == 1 ) && ( south == 0 ) && ( west == 1 ) ) {
            tile.setNAME("D");
        } else {
            tile.setNAME("N");
        }
        
        return tile;
    }
    
    public Tile getMatchingTileByName(String tilename) {
//        System.out.println("TILE NAME: " + tilename);
        Tile tile = new Tile();
        switch (tilename) {
            case "A": tile.setTOP(0); tile.setRIGHT(0); tile.setBOTTOM(0); tile.setLEFT(0);tile.setNAME("A");
                      break;
            case "B": tile.setTOP(0); tile.setRIGHT(0); tile.setBOTTOM(1); tile.setLEFT(1); tile.setNAME("B");
                      break;
            case "C": tile.setTOP(1); tile.setRIGHT(1); tile.setBOTTOM(1); tile.setLEFT(0); tile.setNAME("C");
                      break;
            case "D": tile.setTOP(1); tile.setRIGHT(1); tile.setBOTTOM(0); tile.setLEFT(1); tile.setNAME("D");
                      break;
            default:  tile.setTOP(-1); tile.setRIGHT(-1); tile.setBOTTOM(-1); tile.setLEFT(-1); tile.setNAME("N");
                      break;
        }
//        PrintTile(this);
        return tile;
    }
    
    public Tile getMatchingTile(int lr, int tb) {
        Tile tile = new Tile();
        
        //   XOR Table
        //================
        // 0 XOR 0 = 0 0
        // 1 XOR 1 = 0 0
        // 0 XOR 1 = 0 1
        // 1 XOR 0 = 0 1
        if ( ( lr == 0 ) && ( tb == 0 ) ) {
            tile.setLEFT(0);
            tile.setBOTTOM(0);
            tile.setRIGHT(0);
            tile.setTOP(0);
            tile.setNAME("A");
        } else if ( ( lr == 1 ) && ( tb == 1 ) ) {
            tile.setLEFT(1);
            tile.setBOTTOM(1);
            tile.setRIGHT(0);
            tile.setTOP(0);
            tile.setNAME("B");
        } else if ( ( lr == 0 ) && ( tb == 1 ) ) {
            tile.setLEFT(0);
            tile.setBOTTOM(1);
            tile.setRIGHT(1);
            tile.setTOP(1);
            tile.setNAME("C");
        } else if ( ( lr == 1 ) && ( tb == 0 ) ) {
            tile.setLEFT(1);
            tile.setBOTTOM(0);
            tile.setRIGHT(1);
            tile.setTOP(1);
            tile.setNAME("D");
        } 
        else {
            tile = getSinglePinMatchingTile(lr, tb);
        }
//        else {
//            tile.setLEFT(-1);
//            tile.setBOTTOM(-1);
//            tile.setRIGHT(-1);
//            tile.setTOP(-1);
//            tile.setNAME("N");
//        } 
        return tile;
    }

    public Tile getSinglePinMatchingTile(int left, int bottom) {
        Tile tile = new Tile();
        //   XOR Table
        //================
        // 0 XOR 0 = 0 0
        // 1 XOR 1 = 0 0
        // 0 XOR 1 = 0 1
        // 1 XOR 0 = 0 1
        
        String tilename = "";
        
        if (left >= 0 && bottom >= 0) {
//            System.out.println("4 - LEFT: " + left + ", BOTTOM: " + bottom);
//            Tile ltile = getMatchingTile(left, bottom);
            Tile ltile = new Tile();
            
//            if ( ltile.getNAME().equalsIgnoreCase("N")) {
                if ( bottom == 0 ) {
                    tilename = getTileName("AD", 2);
                } else if ( bottom == 1 ) {
                    tilename = getTileName("BC", 2);
                } else if ( left == 0 ) {
                    tilename = getTileName("AC", 2);
                } else if ( left == 1 ) {
                    tilename = getTileName("BD", 2);
                }
//            }
            return getMatchingTileByName(tilename);
        } else if ( left == -1 && bottom == -1 ) {
//            System.out.println("1 - LEFT: " + left + ", BOTTOM: " + bottom);
            tilename = getTileName();
        } else if (left == -1 && bottom >= 0) {
//            System.out.println("2 - LEFT: " + left + ", BOTTOM: " + bottom);
            if ( bottom == 0 ) {
                tilename = getTileName("AD", 2);
            } else if ( bottom == 1 ) {
                tilename = getTileName("BC", 2);
            }
//            System.out.println("2 - TILENAME: " + tilename);
//            Tile tile = getMatchingTileByName(tilename);
//            return tile;
        } else if (left >= 0 && bottom == -1) {
//            System.out.println("3 - LEFT: " + left + ", BOTTOM: " + bottom);
            if ( left == 0 ) {
                tilename = getTileName("AC", 2);
            } else if ( left == 1 ) {
                tilename = getTileName("BD", 2);
            }
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
    
    public Tile getRandomTile() {
        String tname = getTileName();
        Tile retile = getMatchingTileByName(tname);
        return retile;
    }
    
    public String getTileName() {
        String tilenames = "ABCD";
        Random rand = new Random(System.nanoTime());
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

    public void clear(Tile tile) {
        tile.setTOP(-1); tile.setRIGHT(-1);
        tile.setBOTTOM(-1); tile.setLEFT(-1);
        tile.setNAME("NULL");
    }
    
    public void CopyToTile(Tile A, Tile B) {
        B.setNAME(A.getNAME());
        B.setTOP(A.getTOP());
        B.setRIGHT(A.getRIGHT());
        B.setBOTTOM(A.getBOTTOM());
        B.setLEFT(A.getLEFT());
    }
    
    public void CopyMvTileToTile(mvTile A, Tile B) {
        B.setNAME(A.getNAME());
        B.setTOP(A.getNORTH());
        B.setRIGHT(A.getEAST());
        B.setBOTTOM(A.getSOUTH());
        B.setLEFT(A.getWEST());
    }

    public void CopyMhTileToTile(mhTile A, Tile B) {
        B.setNAME(A.getNAME());
        B.setTOP(A.getNORTH());
        B.setRIGHT(A.getEAST());
        B.setBOTTOM(A.getSOUTH());
        B.setLEFT(A.getWEST());
    }

    public static void main(String[] args) {
        TileUtils tu = TileUtils.getInstance();
        int top = 1;
        Tile current = tu.getSinglePinMatchingTile(-1, top);
        tu.PrintTile(current);
    }
    
}
