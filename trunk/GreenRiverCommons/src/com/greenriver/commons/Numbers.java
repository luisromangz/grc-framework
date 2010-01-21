package com.greenriver.commons;

import java.text.NumberFormat;
import java.util.HashMap;
import javax.swing.text.NumberFormatter;

/**
 * This class is intended to hold static methods to be applied to numbers.
 * @author luis
 */
public class Numbers {

    private static HashMap<Float,String> fractions;

    static {
        fractions=new HashMap<Float, String>();
        fractions.put(0.25f, "¼");
        fractions.put(0.50f,"½");
        fractions.put(0.75f,"¾");

        // TODO: Add more equivaliencies here
    }

    /**
     * Calculates the ceiling of a number, in steps of quarter of a unit.
     *
     * @param number The number we want to calculate the ceiling for.
     * @param epsilon A range to consider above each quarter that will be
     * included in the quarter, e.g epsilon=0.01, 2.26 -> 2.25;
     *
     * An small number is therefore recommended.
     * @return
     */
    // TODO: Generalize this, should accept the step used.
    public static float ceilInQuarterSteps(float number, float epsilon) {
        epsilon = Math.abs(epsilon);
        float integerPart = new Float(number).intValue();
        float decimalPart = Math.abs(number - integerPart);

        if (integerPart >= 0) {
            if (decimalPart < epsilon) {
                return integerPart;
            } else if (decimalPart <= (0.25f + epsilon)) {
                return integerPart + 0.25f;
            } else if (decimalPart <= (0.50f + epsilon)) {
                return integerPart + 0.50f;
            } else if (decimalPart <= (0.75f + epsilon)) {
                return integerPart + 0.75f;
            } else {
                return integerPart + 1;
            }
        } else {
            if (decimalPart < (0.25f - epsilon)) {
                return integerPart;
            } else if (decimalPart < (0.50f - epsilon)) {
                return integerPart - 0.25f;
            } else if (decimalPart < (0.75f - epsilon)) {
                return integerPart - 0.5f;
            } else {
                return integerPart -0.75f;
            }
        }
    }

    /**
     * Tries to format the given number with fractions, and if the number
     * doesn't have a quarter then its formatted with the number of decimals
     * told.
     * @param number The number to be formatted using fractions for the decimal part.
     * @param decimalsIfNotFractionAvalaible The decimals used, if fractions for the decimal part of the number are not avalaible.
     * @return
     */
    public static String formatWithFractions(float number, int decimalsIfNotFractionAvalaible) {
        float integerPart = Math.abs(new Float(number).intValue());
        float decimalPart = number - integerPart;
        String resultString = Integer.toString((int) integerPart);

        if(fractions.containsKey(decimalPart)){
            resultString+="+"+fractions.get(decimalPart);
        } else {
            // We dont show quarters
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(decimalsIfNotFractionAvalaible);
            return numberFormat.format(number);
        }

        if(number< 0) {
            return "-("+resultString+")";
        }

        return resultString;
    }
}
