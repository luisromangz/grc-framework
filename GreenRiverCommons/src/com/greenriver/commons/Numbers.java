package com.greenriver.commons;

import java.text.NumberFormat;
import java.util.HashMap;

/**
 * This class is intended to hold static methods to be applied to numbers.
 * @author luis
 */
public class Numbers {

    private static HashMap<Double, String> fractions;

    static {
        fractions = new HashMap<Double, String>();
        fractions.put(0.25, "¼");
        fractions.put(0.50, "½");
        fractions.put(0.75, "¾");

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
    @Deprecated
    public static double ceilInQuarterSteps(float number) {
        return ceilingToSteps(number, 4,3);
    }

    /**
     * Tries to format the given number with fractions, and if the number
     * doesn't have a quarter then its formatted with the number of decimals
     * told.
     * @param number The number to be formatted using fractions for the decimal part.
     * @param maxDecimalPlaces The decimals used, if fractions for the decimal part of the number are not avalaible.
     * @return
     */
    public static String formatWithFractions(double number, int maxDecimalPlaces) {
        int integerPart = Math.abs(new Float(number).intValue());
        double decimalPart = number - integerPart;
        String resultString = Integer.toString((int) integerPart);

        if (fractions.containsKey(decimalPart)) {
            if (integerPart > 0) {
                resultString += "+" + fractions.get(decimalPart);
            } else {
                resultString = fractions.get(decimalPart);
            }
        } else {
            // We dont show quarters
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(maxDecimalPlaces);
            return numberFormat.format(number);
        }

        if (number < 0 && integerPart > 0) {
            return "-(" + resultString + ")";
        } else if (number < 0) {
            return "- " + resultString;
        }

        return resultString;
    }

    /**
     * Formats a number with the given maximun decimal places.
     *
     * @param maxDecimalPlaces
     * @return
     */
    public static String format(Number number, int maxDecimalPlaces) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(maxDecimalPlaces);
        return numberFormat.format(number);
    }

    /**
     * Rounds a floating point number at the given number of decimal places
     * @param number
     * @param places
     * @return
     */
    public static double round(double number, int places) {
        long integerPart = (long) number;
        double decimalPart = number-integerPart;
        double decimalsDivider = Math.pow(10, places);
        double result = Math.round(decimalPart * decimalsDivider) / decimalsDivider;
        return result+integerPart;
    }

    /**
     * Truncates a floating point number at the given number of decimal places
     * @param number
     * @param places
     * @return
     */
    public static float trunc(float number, int places) {
        double decimalsDivider = Math.pow(10, places);
        return (float) (Math.floor(number * decimalsDivider) / decimalsDivider);
    }

    /**
     * Compares to floats using a deviation. The floats will be equal if the
     * difference between them is less or equal than epsilon parameter.
     * @param floatA
     * @param floatB
     * @param epsilon Maximum allowed difference between floatA and floatB
     * @return
     */
    public static boolean equals(double floatA, double floatB, double epsilon) {
        return Math.abs(floatA - floatB) <= epsilon;
    }

    /**
     * Compares to floats using a deviation. The floats will be equal if the
     * difference between them is less or equal than epsilon parameter. Otherwise
     * a regular arithmetic comparison is used to determine what is floatA is
     * greater than floatB.
     * @param floatA
     * @param floatB
     * @param epsilon
     * @return
     */
    public static boolean greaterOrEqual(float floatA, float floatB, double epsilon) {
        float result = Math.abs(floatA - floatB);
        return result <= epsilon || floatA > floatB;
    }

    /**
     * Compares to floats using a deviation. The floats will be equal if the
     * difference between them is less or equal than epsilon parameter. Otherwise
     * a regular arithmetic comparison is used to determine what is floatA is
     * lesser than floatB.
     * @param floatA
     * @param floatB
     * @param epsilon
     * @return
     */
    public static boolean lessOrEqual(float floatA, float floatB, double epsilon) {
        float result = Math.abs(floatA - floatB);
        return result <= epsilon || floatA < floatB;
    }

    /**
     * Sums all the digits of a number until there is only a single-digit number.
     * @param number
     * @return
     */
    public static int sumDigits(int number) {
        return Strings.sumDigits(number + "");
    }

    public static double ceilingToSteps(double number, int roundingSteps, int maxPlaces) {
        if(roundingSteps==0){
            return number;
        }
                
        int integerPart = new Float(number).intValue();

        double decimalPart = Math.abs(number - integerPart);

        double delta =round(1.0f / roundingSteps,maxPlaces);


        double accum = 0;
        if (integerPart >= 0) {
            for (int i = 0; i < roundingSteps; i++) {
                if (decimalPart == accum) {
                    return round(integerPart +accum,maxPlaces);
                } else if (decimalPart > accum && decimalPart < accum + delta) {
                    return round(integerPart + accum + delta,maxPlaces);
                }

                accum += delta;
            }

            return integerPart + 1;

        } else {
            for (int i = 0; i < roundingSteps - 1; i++) {
                if (decimalPart == accum) {
                    return round(integerPart -accum,maxPlaces);
                } else if (decimalPart >= accum && decimalPart < accum + delta) {
                    return round(integerPart - accum,maxPlaces);
                }

                accum += delta;
            }

            return integerPart - 1;
        }
    }
}
