/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.number;
import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * Champ pour la saisie de pourcentage.
 * 
 * <p>
 * L'utilisateur saisie un nombre entre 0,00 et 100,00. Le nombre représenté est divisé
 * par 100. Le composant affiche "50,00 %" mais la methode getNumber renvoie "0.5"
 * </p>
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class PercentageField extends NumberField {
    public PercentageField() {
        this(4);
    }


    public PercentageField(int maxDbFractionDigit) {
        setRenderer(new NumberFieldRenderer(
                new DecimalFormat(buildPattern(maxDbFractionDigit))));
        setMaxValue(1);
        setMinValue(0);
        setMaximumFractionDigits(maxDbFractionDigit);
    }

    @Override
    protected Translater newTranslater() {
        return new PercentageTranslater();
    }


    private String buildPattern(int maxDbFractionDigit) {
        StringBuffer buffer = new StringBuffer("#0.");

        for (int i = 2; i < maxDbFractionDigit; i++) {
            buffer.append("0");
        }

        return buffer.append(" %").toString();
    }

    static class PercentageTranslater extends Translater {
        private static final int SHIFT = 2;

        @Override
        public String numberToString(Number nb) {
            if (nb == null) {
                return "";
            }
            return toBigDecimal(nb).movePointRight(SHIFT).toString();
        }


        @Override
        public BigDecimal stringToNumber(final String txt) {
            try {
                return new BigDecimal(txt).movePointLeft(SHIFT);
            }
            catch (Exception ex) {
                return null;
            }
        }


        private BigDecimal toBigDecimal(final Number nb) {
            BigDecimal val;
            if (nb instanceof BigDecimal) {
                val = ((BigDecimal)nb);
            }
            else {
                val = new BigDecimal(nb.toString());
            }

            return val;
        }
    }
}
