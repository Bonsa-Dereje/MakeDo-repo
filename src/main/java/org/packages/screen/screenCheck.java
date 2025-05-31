package org.packages.screen;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;

public class screenCheck {

    public static class ScreenInfo {
        public int displayNumber;
        public int width;
        public int height;
        public int refreshRate;
        public int bitDepth;
        public int pixelSize;
        public String colorSpaceName;

        public ScreenInfo(int displayNumber, int width, int height, int refreshRate, int bitDepth, int pixelSize, String colorSpaceName) {
            this.displayNumber = displayNumber;
            this.width = width;
            this.height = height;
            this.refreshRate = refreshRate;
            this.bitDepth = bitDepth;
            this.pixelSize = pixelSize;
            this.colorSpaceName = colorSpaceName;
        }

        @Override
        public String toString() {
            return String.format(
                    "Display %d:\n  Resolution: %dx%d\n  Refresh Rate: %d Hz\n  Bit Depth: %d\n  Pixel Size: %d bits\n  Color Space: %s\n----------------------------",
                    displayNumber, width, height, refreshRate, bitDepth, pixelSize, colorSpaceName);
        }
    }

    public static ScreenInfo[] getAllScreenInfo() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        ScreenInfo[] infos = new ScreenInfo[gs.length];

        for (int i = 0; i < gs.length; i++) {
            GraphicsDevice gd = gs[i];
            DisplayMode dm = gd.getDisplayMode();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            ColorModel cm = gc.getColorModel();

            int colorSpaceType = cm.getColorSpace().getType();
            String colorSpaceName = getColorSpaceName(colorSpaceType);

            infos[i] = new ScreenInfo(
                    i + 1,
                    dm.getWidth(),
                    dm.getHeight(),
                    dm.getRefreshRate(),
                    dm.getBitDepth(),
                    cm.getPixelSize(),
                    colorSpaceName
            );
        }

        return infos;
    }

    private static String getColorSpaceName(int type) {
        return switch (type) {
            case ColorSpace.TYPE_RGB -> "RGB";
            case ColorSpace.TYPE_GRAY -> "Grayscale";
            case ColorSpace.TYPE_CMYK -> "CMYK";
            case ColorSpace.TYPE_XYZ -> "CIEXYZ";
            case ColorSpace.TYPE_HLS -> "HLS";
            case ColorSpace.TYPE_HSV -> "HSV";
            default -> "Unknown";
        };
    }

    public static void main(String[] args) {
        ScreenInfo[] screens = getAllScreenInfo();
        for (ScreenInfo si : screens) {
            System.out.println(si);
        }
    }
}
