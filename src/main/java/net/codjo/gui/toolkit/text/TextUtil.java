/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.text;
import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * Classe utilitaire pour normaliser les affichages des numériques (décimaux....).
 */
public class TextUtil {
    private static final DecimalFormat FORMAT = new DecimalFormat("#0.00");


    private TextUtil() {}


    public static String formatAmount(double number) {
        return FORMAT.format(number).replace(',', '.');
    }


    public static String formatAmount(BigDecimal number) {
        return FORMAT.format(number.doubleValue()).replace(',', '.');
    }
}
