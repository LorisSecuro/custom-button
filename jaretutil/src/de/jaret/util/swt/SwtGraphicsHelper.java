/*
 *  File: SwtGraphicsHelper.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;

/**
 * A simple class containing several static methods for convenient painting with a SWT gc.
 * 
 * @author Peter Kliem
 * @version $Id: SwtGraphicsHelper.java 726 2008-03-18 21:06:14Z kliem $
 */
public class SwtGraphicsHelper {

    /**
     * Draw a string centered between x,y at top y.
     * @param gc gc
     * @param string string
     * @param left left bound
     * @param right right bound
     * @param y top y
     */
    public static void drawStringCentered(GC gc, String string, int left, int right, int y) {
        Point extent = gc.textExtent(string);
        int width = right - left;
        int xx = (int) ((width - extent.x) / 2);
        gc.drawString(string, left+xx, y);
    }

    public static void drawStringCenteredMidX(GC gc, String string, int midx, int y) {
        Point extent = gc.textExtent(string);
        int xx = (int) (midx - (extent.x / 2));
        gc.drawString(string, xx, y);
    }

    public static void drawStringCenteredVCenter(GC gc, String string, int left, int right, int yCenter) {
        Point extent = gc.textExtent(string);
        // int descent = graphics.getFontMetrics().getDescent();
        int descent = 0;
        int width = right - left;
        int xx = (int) ((width - extent.x) / 2);
        int y = yCenter - (int) (extent.y / 2 - descent);
        gc.drawString(string, left + xx, y);

    }

    public static void drawStringRightAlignedVCenter(GC gc, String string, int x, int y) {
        Point extent = gc.textExtent(string);

        int xx = (int) (x - extent.x);
        int yy = (int) (y - (extent.y / 2));
        gc.drawString(string, xx, yy);
    }

    public static void drawStringLeftAlignedVCenter(GC gc, String string, int x, int y) {
        Point extent = gc.textExtent(string);

        int xx = x;
        int yy = (int) (y - (extent.y / 2));
        gc.drawString(string, xx, yy);
    }

    public static void drawStringCentered(GC gc, String string, int xCenter, int yBase) {
        Point extent = gc.textExtent(string);
        int xx = xCenter - (int) ((extent.x) / 2);
        gc.drawText(string, xx, yBase - extent.y, true);
    }

    public static void drawStringCenteredAroundPoint(GC gc, String string, int xCenter, int yCenter) {
        Point extent = gc.textExtent(string);
        int xx = xCenter - (int) ((extent.x) / 2);
        int yy = yCenter - (int) ((extent.y) / 2);
        gc.drawText(string, xx, yy, true);
    }

    /**
     * Draw the string centered in the given rectangle (specified by plain values)
     * 
     * @param gc
     * @param string
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void drawStringCentered(GC gc, String string, int x, int y, int width, int height) {
        Point extent = gc.textExtent(string);
        int xx = x + (width - extent.x) / 2;
        int yy = y + (height - extent.y) / 2;
        gc.drawText(string, xx, yy, true);
    }

    /**
     * Draw String centered in the given rectangle.
     * 
     * @param gc
     * @param string
     * @param rect
     */
    public static void drawStringCentered(GC gc, String string, Rectangle rect) {
        drawStringCentered(gc, string, rect.x, rect.y, rect.width, rect.height);
    }

    public static int getStringDrawingWidth(GC gc, String string) {
        return gc.textExtent(string).x;
    }

    public static int getStringDrawingHeight(GC gc, String string) {
        return gc.textExtent(string).y;
    }

    /**
     * Draws a String right aligned with the y coordinate denoting the top of the string.
     * @param gc GC
     * @param string string to draw
     * @param x right x position
     * @param yTop top y position
     */
    public static void drawStringRightAlignedVTop(GC gc, String string, int x, int yTop) {
        Point extent = gc.textExtent(string);
        int xx = (int) (x - extent.x);
        gc.drawText(string, xx, yTop, true);
    }

    public static void drawArrowLine(GC gc, int x1, int y1, int x2, int y2, int dist, int height, boolean arrowLeft,
            boolean arrowRight) {
        int off = height;
        gc.drawLine(x1 + off + 1, y1, x2 - off - 1, y2);
        if (arrowLeft) {
            gc.drawLine(x1, y1, x1 + dist, y1 - off);
            gc.drawLine(x1, y1, x1 + dist, y1 + off);
            gc.drawLine(x1 + dist, y1 - off, x1 + dist, y1 + off);
        }
        if (arrowRight) {
            gc.drawLine(x2, y2, x2 - dist, y2 - off);
            gc.drawLine(x2, y2, x2 - dist, y2 + off);
            gc.drawLine(x2 - dist, y2 - off, x2 - dist, y2 + off);
        }
    }

    public static void drawArrowLineVertical(GC gc, int x1, int y1, int x2, int y2, int dist, int height,
            boolean arrowUp, boolean arrowDown) {
        int off = height;
        gc.drawLine(x1, y1 + off + 1, x2, y2 - off - 1);
        if (arrowUp) {
            gc.drawLine(x1, y1, x1 - off, y1 + dist);
            gc.drawLine(x1, y1, x1 + off, y1 + dist);
            gc.drawLine(x1 - off, y1 + dist, x1 + off, y1 + dist);
        }
        if (arrowDown) {
            gc.drawLine(x2, y2, x2 - off, y2 - dist);
            gc.drawLine(x2, y2, x2 + off, y2 - dist);
            gc.drawLine(x2 - off, y2 - dist, x2 + off, y2 - dist);
        }
    }

    /**
     * Draw a string vertical centered beetween upper and lower y left aligned to x.
     * 
     * @param gc GC
     * @param label label to draw
     * @param x left x
     * @param upperY upper y bound
     * @param lowerY lower y bound
     */
    public static void drawStringVCentered(GC gc, String label, int x, int upperY, int lowerY) {
        Point extent = gc.textExtent(label);

        int yy = (int) upperY + (lowerY - upperY - extent.y) / 2;
        gc.drawText(label, x, yy, true);
    }

    /**
     * Draw a String vertical. This method might be quite costly since it uses an image to buffer.
     * 
     * @param gc gc
     * @param string strin gto to draw
     * @param x upper left x
     * @param y upper left y
     */
    public static void drawStringVertical(GC gc, String string, int x, int y) {
        Point extent = gc.textExtent(string);
        Image img = new Image(gc.getDevice(), extent.x, extent.y);
        GC imageGC = new GC(img);
        imageGC.drawString(string, 0, 0);
        imageGC.dispose();

        
        Image vertImg = new Image(gc.getDevice(), extent.y, extent.x);
        ImageData iData = img.getImageData();
        ImageData destIData = vertImg.getImageData();

        for (int xx = 0; xx < iData.width; xx++) {
            for (int yy = 0; yy < iData.height; yy++) {
                destIData.setPixel(yy, iData.width-xx-1, iData.getPixel(xx, yy));
            }
        }

        img.dispose();
        Image destImg = new Image(gc.getDevice(), destIData);
        
        gc.drawImage(destImg, x, y);
        vertImg.dispose();
        destImg.dispose();

    }
    /**
     * Create an image with a drop shadow. This method is (c) 2007 Nicholas Rajendram, IBM Canada, see
     * http://www.eclipse.org/articles/article.php?file=Article-SimpleImageEffectsForSWT/index.html.
     * 
     * @param originalImageData The original image. Transparency information will be ignored.
     * @param color The color of the drop shadow
     * @param radius The radius of the drop shadow in pixels
     * @param highlightRadius The radius of the highlight area
     * @param opacity The opacity of the drop shadow
     * @return The drop shadowed image. This image data will be larger than the original. The same image data will be
     * returned if the shadow radius is 0, or null if an error occured.
     */
    public static ImageData dropShadow(ImageData originalImageData, Color color, int radius, int highlightRadius,
            int opacity) {
        /*
         * This method will create a drop shadowto the bottom-right of an existing image. This drop shadow is created by
         * creating an altered one-sided glow, and shifting its position around the image. See the Glow class for more
         * details of how the glow is calculated.
         */
        if (originalImageData == null)
            return null;
        if (color == null)
            return null;
        if (radius == 0)
            return originalImageData;
        int shift = (int) (radius * 1.5); // distance to shift "glow" from image
        // the percent increase in color intensity in the highlight radius
        double highlightRadiusIncrease = radius < highlightRadius * 2 ? .15 : radius < highlightRadius * 3 ? .09 : .02;
        opacity = opacity > 255 ? 255 : opacity < 0 ? 0 : opacity;
        // prepare new image data with 24-bit direct palette to hold shadowed copy of image
        ImageData newImageData = new ImageData(originalImageData.width + radius * 2, originalImageData.height + radius
                * 2, 24, new PaletteData(0xFF, 0xFF00, 0xFF0000));
        int[] pixels = new int[originalImageData.width];
        // copy image data
        for (int row = radius; row < radius + originalImageData.height; row++) {
            originalImageData.getPixels(0, row - radius, originalImageData.width, pixels, 0);
            for (int col = 0; col < pixels.length; col++)
                pixels[col] = newImageData.palette.getPixel(originalImageData.palette.getRGB(pixels[col]));
            newImageData.setPixels(radius, row, originalImageData.width, pixels, 0);
        }
        // initialize glow pixel data
        int colorInt = newImageData.palette.getPixel(color.getRGB());
        pixels = new int[newImageData.width];
        for (int i = 0; i < newImageData.width; i++) {
            pixels[i] = colorInt;
        }
        // initialize alpha values
        byte[] alphas = new byte[newImageData.width];
        // deal with alpha values on rows above and below the photo
        for (int row = 0; row < newImageData.height; row++) {
            if (row < radius) {
                // only calculate alpha values for top border. they will reflect to the bottom border
                byte intensity = (byte) (opacity * ((((row + 1)) / (double) (radius))));
                for (int col = 0; col < alphas.length / 2 + alphas.length % 2; col++) {
                    if (col < radius) {
                        // deal with corners:
                        // calculate pixel's distance from image corner
                        double hypotenuse = Math
                                .sqrt(Math.pow(radius - col - 1, 2.0) + Math.pow(radius - 1 - row, 2.0));
                        // calculate alpha based on percent distance from image
                        alphas[col + shift] = alphas[alphas.length - col - 1] = (byte) (opacity * Math.max(
                                ((radius - hypotenuse) / radius), 0));
                        // add highlight radius
                        if (hypotenuse < Math.min(highlightRadius, radius * .5)) {
                            alphas[col + shift] = alphas[alphas.length - col - 1] = (byte) Math.min(255, (alphas[col
                                    + shift] & 0x0FF)
                                    * (1 + highlightRadiusIncrease * Math.max(((radius - hypotenuse) / radius), 0)));
                        }
                    } else {
                        alphas[col + shift] = alphas[alphas.length - col - 1] = (byte) ((row > Math.max(radius
                                - highlightRadius - 1, radius * .5)) ? Math.min(255, (intensity & 0x0FF)
                                * (1 + highlightRadiusIncrease * row / radius)) : intensity);
                    }
                }
                if (row + shift < newImageData.height) {
                    newImageData.setAlphas(newImageData.width - radius, row + shift, radius, alphas, alphas.length
                            - radius);
                    newImageData.setPixels(newImageData.width - radius, row + shift, radius, pixels, alphas.length
                            - radius);
                }
                newImageData.setAlphas(0, newImageData.height - 1 - row, newImageData.width, alphas, 0);
                newImageData.setPixels(0, newImageData.height - 1 - row, newImageData.width, pixels, 0);
            }
            // deal with rows the image resides on
            else if (row <= newImageData.height / 2) {
                // calculate alpha values
                double intensity = 0;
                for (int col = 0; col < alphas.length; col++) {
                    if (col < radius) {
                        intensity = (opacity * ((col + 1) / (double) radius));
                        if (col > Math.max(radius - highlightRadius - 1, radius * .5)) {
                            intensity = Math.min(255, (intensity) * (1 + highlightRadiusIncrease * col / radius));
                        }
                        alphas[newImageData.width - col - 1] = (byte) (int) (intensity);
                        alphas[col] = 0;
                    } else if (col <= newImageData.width / 2 + newImageData.width % 2) {
                        // original image pixels are full opacity
                        alphas[col] = alphas[newImageData.width - col - 1] = (byte) (255);
                    }
                }
                newImageData.setPixels(0, newImageData.height - 1 - row, radius, pixels, 0);
                newImageData.setPixels(originalImageData.width + radius, newImageData.height - 1 - row, radius, pixels,
                        0);
                newImageData.setAlphas(0, newImageData.height - 1 - row, newImageData.width, alphas, 0);
                if (row >= shift + radius) {
                    newImageData.setPixels(0, row, radius, pixels, 0);
                    newImageData.setPixels(originalImageData.width + radius, row, radius, pixels, 0);
                    newImageData.setAlphas(0, row, newImageData.width, alphas, 0);
                } else {
                    newImageData.setPixels(0, row, radius, pixels, 0);
                    newImageData.setAlphas(0, row, newImageData.width - radius, alphas, 0);
                }
            }
        }
        return newImageData;
    }

    /**
     * Create an image with a glow effect. This method is (c) 2007 Nicholas Rajendram, IBM Canada, see
     * http://www.eclipse.org/articles/article.php?file=Article-SimpleImageEffectsForSWT/index.html.
     * 
     * @param originalImageData The original image. Transparency information will be ignored.
     * @param color The color of the glow
     * @param radius The radius of the glow in pixels
     * @param highlightRadius The radius of the highlight area
     * @param opacity The opacity of the glow
     * @return The glowing image. This image data will be larger than the original. The same image data will be returned
     * if the glow radius is 0, or null if an error occured.
     */
    public static ImageData glow(ImageData originalImageData, Color color, int radius, int highlightRadius, int opacity) {
        /*
         * This method will surround an existing image with a glowing border. This glow is created by adding a solid
         * colored border around an image. Alpha values are then manipulated in order to blend the border with its
         * background. This gives a glowing appearance.
         * 
         * To obtain the alpha value of a glow pixel, its position in the border radius as a percent of the radius'
         * total width is first calculated. This percentage is multipled by the maximum opacity level, giving pixels an
         * outward linear blend from the image from opaque to transparent.
         * 
         * A highlight radius increases the intensity of a given radius of pixels surrounding the image to better
         * highlight it. When there is a highlight radius, the entire glow's overall alpha blending is non-linear.
         */
        if (originalImageData == null)
            return null;
        if (color == null)
            return null;
        if (radius == 0)
            return originalImageData;
        // the percent increase in color intensity in the highlight radius
        double highlightRadiusIncrease = radius < highlightRadius * 2 ? .15 : radius < highlightRadius * 3 ? .09 : .02;
        opacity = opacity > 255 ? 255 : opacity < 0 ? 0 : opacity;
        // prepare new image data with 24-bit direct palette to hold glowing copy of image
        ImageData newImageData = new ImageData(originalImageData.width + radius * 2, originalImageData.height + radius
                * 2, 24, new PaletteData(0xFF, 0xFF00, 0xFF0000));
        int[] pixels = new int[originalImageData.width];
        // copy image data
        for (int row = radius; row < radius + originalImageData.height; row++) {
            originalImageData.getPixels(0, row - radius, originalImageData.width, pixels, 0);
            for (int col = 0; col < pixels.length; col++)
                pixels[col] = newImageData.palette.getPixel(originalImageData.palette.getRGB(pixels[col]));
            newImageData.setPixels(radius, row, originalImageData.width, pixels, 0);
        }
        // initialize glow pixel data
        int colorInt = newImageData.palette.getPixel(color.getRGB());
        pixels = new int[newImageData.width];
        for (int i = 0; i < newImageData.width; i++) {
            pixels[i] = colorInt;
        }
        // initialize alpha values
        byte[] alphas = new byte[newImageData.width];
        // deal with alpha values on rows above and below the photo
        for (int row = 0; row < newImageData.height; row++) {
            if (row < radius) {
                // only calculate alpha values for top border. they will reflect to the bottom border
                byte intensity = (byte) (opacity * ((((row + 1)) / (double) (radius))));
                for (int col = 0; col < alphas.length / 2 + alphas.length % 2; col++) {
                    if (col < radius) {
                        // deal with corners:
                        // calculate pixel's distance from image corner
                        double hypotenuse = Math
                                .sqrt(Math.pow(radius - col - 1, 2.0) + Math.pow(radius - 1 - row, 2.0));
                        // calculate alpha based on percent distance from image
                        alphas[col] = alphas[alphas.length - col - 1] = (byte) (opacity * Math.max(
                                ((radius - hypotenuse) / radius), 0));
                        // add highlight radius
                        if (hypotenuse < Math.min(highlightRadius, radius * .5)) {
                            alphas[col] = alphas[alphas.length - col - 1] = (byte) Math.min(255, (alphas[col] & 0x0FF)
                                    * (1 + highlightRadiusIncrease * Math.max(((radius - hypotenuse) / radius), 0)));
                        }
                    } else {
                        alphas[col] = alphas[alphas.length - 1 - col] = (byte) ((row > Math.max(radius
                                - highlightRadius - 1, radius * .5)) ? Math.min(255, (intensity & 0x0FF)
                                * (1 + highlightRadiusIncrease * row / radius)) : intensity);
                    }
                }
                newImageData.setAlphas(0, row, newImageData.width, alphas, 0);
                newImageData.setAlphas(0, newImageData.height - 1 - row, newImageData.width, alphas, 0);
                newImageData.setPixels(0, row, newImageData.width, pixels, 0);
                newImageData.setPixels(0, newImageData.height - 1 - row, newImageData.width, pixels, 0);
            }
            // deal with rows the image resides on
            else if (row <= newImageData.height / 2) {
                // calculate alpha values
                double intensity = 0;
                for (int col = 0; col < alphas.length; col++) {
                    if (col < radius) {
                        intensity = (opacity * ((col + 1) / (double) radius));
                        if (col > Math.max(radius - highlightRadius - 1, radius * .5)) {
                            intensity = Math.min(255, (intensity) * (1 + highlightRadiusIncrease * col / radius));
                        }
                        alphas[col] = alphas[newImageData.width - col - 1] = (byte) (intensity);
                    } else if (col <= newImageData.width / 2 + newImageData.width % 2) {
                        // original image pixels are full opacity
                        alphas[col] = alphas[newImageData.width - col - 1] = (byte) (255);
                    }
                }
                newImageData.setPixels(0, row, radius, pixels, 0);
                newImageData.setPixels(originalImageData.width + radius, row, radius, pixels, 0);
                newImageData.setAlphas(0, row, newImageData.width, alphas, 0);
                newImageData.setPixels(0, newImageData.height - 1 - row, radius, pixels, 0);
                newImageData.setPixels(originalImageData.width + radius, newImageData.height - 1 - row, radius, pixels,
                        0);
                newImageData.setAlphas(0, newImageData.height - 1 - row, newImageData.width, alphas, 0);
            }
        }
        return newImageData;
    }
    /**
     * Create a reflection image (Idea taken from Daniel Spiewak: see
     * http://www.eclipsezone.com/eclipse/forums/t91013.html?start=0).
     * 
     * @param img image to reflect
     * @param device device
     * @return image (larger than the original) containing a reflectd version of the original image
     */
    public static Image reflect(Image img, Device device) {

        int height = img.getImageData().height;
        int width = img.getImageData().width;

        Image reflect = new Image(device, width, height / 2);
        GC imageGC = new GC(reflect);

        Transform rTransform = new Transform(imageGC.getDevice(), 1, 0, 0, -.5f, 0, height / 2);
        imageGC.setTransform(rTransform);

        imageGC.setAlpha(100);

        imageGC.drawImage(img, 0, 0);
        // imageGC.setTransform(null);
        // // shade it
        // int alpha =100;
        // imageGC.setAlpha(100);
        // imageGC.setForeground(imageGC.getDevice().getSystemColor(SWT.COLOR_WHITE));
        // for (int y = 0;y<height/2;y++) {
        // imageGC.setAlpha(alpha);
        // imageGC.drawLine(0, y, width, y);
        // alpha = alpha + 255/height;
        // System.out.println("ALPHA "+alpha+" y "+y);
        // }

        imageGC.dispose();
        return reflect;
    }

}
