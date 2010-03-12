
package com.greenriver.commons.validators;

/**
 * Validator for the widely used 13-digit bar code.
 * @author Miguel Angel
 */
public class EAN13Validator implements DataValidator<String> {

    @Override
    public boolean validate(String data) {

        //The checksum is a Modulo 10 calculation:
        //
        //  - Add the values of the digits in the even-numbered positions: 2, 4, 6, etc.
        //  - Multiply this result by 3.
        //  - Add the values of the digits in the odd-numbered positions: 1, 3, 5, etc.
        //  - Sum the results of steps 2 and 3.
        //  - The check character is the smallest number which, when added to the result in step 4, produces a multiple of 10

        if (data.length() != 13 || !data.matches("[0-9]+")) {
            return false;
        }
        
        int evenSum = 0;
        int oddSum = 0;
        int checkDigit = Integer.parseInt(data.charAt(12) + "");

        data = data.substring(0, 12);

        for (int i=0; i< 12; i++) {
            int digit = Integer.parseInt(data.charAt(i) + "");
            if (i%2 == 0) {
                evenSum += digit;
            } else {
                oddSum += digit;
            }
        }

        int checksum = 10 - (evenSum * 3 + oddSum) % 10;

        return checksum == checkDigit;
    }
}
