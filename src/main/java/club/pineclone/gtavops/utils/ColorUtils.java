package club.pineclone.gtavops.utils;

import javafx.scene.paint.Color;

public class ColorUtils {

    private ColorUtils() {}

    public static String formatAsHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }

}
