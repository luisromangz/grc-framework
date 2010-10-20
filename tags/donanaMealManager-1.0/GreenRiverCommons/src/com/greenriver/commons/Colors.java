
package com.greenriver.commons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Color utilities
 * @author Miguel Angel
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
