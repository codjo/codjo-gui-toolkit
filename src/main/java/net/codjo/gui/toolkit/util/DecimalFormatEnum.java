package net.codjo.gui.toolkit.util;
/**
 *
 */
public enum DecimalFormatEnum {
    SIMPLE('#'),
    FULL('0');

    private final char formatSymbol;


    DecimalFormatEnum(char formatSymbol) {
        this.formatSymbol = formatSymbol;
    }


    public char getFormatSymbol() {
        return formatSymbol;
    }
}

