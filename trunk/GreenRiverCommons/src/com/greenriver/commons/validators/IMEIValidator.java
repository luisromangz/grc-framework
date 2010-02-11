/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.validators;

/**
 * Validates an imei using Lunh's algorithm.
 * <br/><br/>
 * References:<ul><li>
 *  http://www.forensicswiki.org/wiki/SIM_Card</li><li>
 *  http://en.wikipedia.org/wiki/Luhn_algorithm</li><li>
 *  http://www.chriswareham.demon.co.uk/software/Luhn.java</li></ul>
 * @author Miguel Angel
 */
public class IMEIValidator {

    /**
     * Validates an imei.
     * @param imei to validate
     * @return true if the imei is valid or false if not.
     */
    public static boolean validate(String imei) {
        byte temp = 0;
        int sum = 0;
        boolean multiply = false;

        for (int i = imei.length() - 1; i >= 0; i--) {
            temp = Byte.parseByte(((Character)imei.charAt(i)).toString());

            if (multiply) {
                temp = (byte) ((temp > 4) ? (temp * 2) % 10 + 1 : temp * 2);
            }

            multiply = !multiply;
            sum += temp;
        }

        return (sum % 10) == 0;
    }
}
