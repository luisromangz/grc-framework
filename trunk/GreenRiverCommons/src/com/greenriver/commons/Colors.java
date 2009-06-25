/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Color utilities
 */
public class Colors {

    /**
     * Gets a list of colors that are distributed to be the most different ones.
     * @param amount Number of colors to get
     * @return a list with the colors expressed in rgb hex.
     */
    public static List<String> generate(int amount) {
        float h_delta = (float) (1.0 / amount);
        int color = 0;
        List<String> seriesColors = new ArrayList<String>();

        for (float h = 0; h < 1; h += h_delta) {
            color = Color.HSBtoRGB(h, 1, 0.8f);
            seriesColors.add(Integer.toHexString(color));
        }

        return seriesColors;
    }
}
