/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.utils;

/**
 *
 * @author VKGautam
 */
public class ANSIColor {

    private ANSIColor() {
    }

    public static ANSIColor getInstance() {
        return ANSIColorHolder.INSTANCE;
    }

    private static class ANSIColorHolder {

        private static final ANSIColor INSTANCE = new ANSIColor();
    }
    private static boolean ansi_enabled = true;

    /**
     * Enable ANSI color codes (codes are enabled by default).
     */
    public static void enable() {
        ansi_enabled = true;
    }

    /**
     * Disable ANSI color codes. When disabled, all other methods won't do
     * anything until ANSI colors are re-enabled.
     */
    public static void disable() {
        ansi_enabled = false;
    }

    /**
     * Alias for color("red").
     */
    public static void red() {
        color("red");
    }

    /**
     * Alias for color("maroon").
     */
    public static void maroon() {
        color("maroon");
    }

    /**
     * Alias for color("lime").
     */
    public static void lime() {
        color("lime");
    }

    /**
     * Alias for color("green").
     */
    public static void green() {
        color("green");
    }

    /**
     * Alias for color("yellow").
     */
    public static void yellow() {
        color("yellow");
    }

    /**
     * Alias for color("gold").
     */
    public static void gold() {
        color("gold");
    }

    /**
     * Alias for color("blue").
     */
    public static void blue() {
        color("blue");
    }

    /**
     * Alias for color("navy").
     */
    public static void navy() {
        color("navy");
    }

    /**
     * Alias for color("magenta").
     */
    public static void magenta() {
        color("magenta");
    }

    /**
     * Alias for color("purple").
     */
    public static void purple() {
        color("purple");
    }

    /**
     * Alias for color("cyan").
     */
    public static void cyan() {
        color("cyan");
    }

    /**
     * Alias for color("teal").
     */
    public static void teal() {
        color("teal");
    }

    /**
     * Alias for color("white").
     */
    public static void white() {
        color("white");
    }

    /**
     * Alias for color("silver").
     */
    public static void silver() {
        color("silver");
    }

    /**
     * Alias for color("reset").
     */
    public static void reset() {
        color("reset");
    }

    public static void ColorText(String text, String colour) {
//        System.out.println();
        color(colour);
        System.out.print(text);
        color("reset");
//        System.out.println();
    }
    
    /**
     * Print a single line of text in red. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void red(String text) {
        ColorText(text, "red");
    }
    

    /**
     * Print a single line of text in maroon. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void maroon(String text) {
        ColorText(text, "maroon");
    }

    /**
     * Print a single line of text in lime. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void lime(String text) {
        ColorText(text, "lime");
    }

    /**
     * Print a single line of text in green. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void green(String text) {
        ColorText(text, "green");
    }

    /**
     * Print a single line of text in yellow. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void yellow(String text) {
        ColorText(text, "yellow");
    }

    /**
     * Print a single line of text in gold. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void gold(String text) {
        ColorText(text, "gold");
    }

    /**
     * Print a single line of text in blue. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void blue(String text) {
        ColorText(text, "blue");
    }

    /**
     * Print a single line of text in navy. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void navy(String text) {
        ColorText(text, "navy");
    }

    /**
     * Print a single line of text in magenta. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void magenta(String text) {
        ColorText(text, "magenta");
    }

    /**
     * Print a single line of text in purple. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void purple(String text) {
        ColorText(text, "purple");
    }

    /**
     * Print a single line of text in cyan. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void cyan(String text) {
        ColorText(text, "cyan");
    }

    /**
     * Print a single line of text in teal. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void teal(String text) {
        ColorText(text, "teal");
    }

    /**
     * Print a single line of text in white. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void white(String text) {
        ColorText(text, "white");
    }

    /**
     * Print a single line of text in silver. No line breaks are used. Color is
     * reset afterward.
     *
     * @param text The text to print in this color.
     */
    public static void silver(String text) {
        ColorText(text, "silver");
    }

    /**
     * Print an ANSI color escape code to the terminal.
     *
     * @param name The name of a color to use (or a reset sequence is sent).
     */
    public static void color(String name) {
        // Don't do it if ANSI colors aren't enabled.
        if (ansi_enabled == false) {
            return;
        }
        System.out.print(" ");
        switch (name) {
            case "red":
                System.out.print((char) 27 + "[31;1m");
                break;
            case "maroon":
                System.out.print((char) 27 + "[31m");
                break;
            case "lime":
                System.out.print((char) 27 + "[32;1m");
                break;
            case "green":
                System.out.print((char) 27 + "[32m");
                break;
            case "yellow":
                System.out.print((char) 27 + "[33;1m");
                break;
            case "gold":
                System.out.print((char) 27 + "[33m");
                break;
            case "blue":
                System.out.print((char) 27 + "[34;1m");
                break;
            case "navy":
                System.out.print((char) 27 + "[34m");
                break;
            case "magenta":
                System.out.print((char) 27 + "[35;1m");
                break;
            case "purple":
                System.out.print((char) 27 + "[35m");
                break;
            case "cyan":
                System.out.print((char) 27 + "[36;1m");
                break;
            case "teal":
                System.out.print((char) 27 + "[36m");
                break;
            case "white":
                System.out.print((char) 27 + "[37;1m");
                break;
            case "silver":
                System.out.print((char) 27 + "[37m");
                break;
            default:
                System.out.print((char) 27 + "[0m");
                break;
        }
        System.out.print(" ");
    }
    
    public static void main(String[] args) {
        String text = "Welcome to India";
        ANSIColor.maroon(text);
        ANSIColor.blue("Ⓐ █ █ █	Ⓑ 	Ⓒ 	Ⓓ");
        ANSIColor.green(text);
    }
    
    
}
