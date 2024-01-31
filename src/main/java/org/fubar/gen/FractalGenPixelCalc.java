package org.fubar.gen;

import org.fubar.math.ComplexNumber;

import java.awt.*;

public class FractalGenPixelCalc {

    static int calculatePixelColor(int x, int y, int width, int height, double realPart, double imaginaryPart) {
        double zx = 1.5 * (x - (double) width / 2) / (0.5 * width);
        double zy = (y - (double) height / 2) / (0.5 * height);

        ComplexNumber c = new ComplexNumber(realPart, imaginaryPart);
        ComplexNumber z = new ComplexNumber(zx, zy);

        int maxIterations = 300;
        int iteration = 0;
        while (z.abs() < 2 && iteration < maxIterations) {
            z = z.multiply(z).add(c);
            iteration++;
        }

        Color color;
        if (iteration < maxIterations) {
            float hue = (float) iteration / maxIterations;
            color = Color.getHSBColor(hue, 1, 1);
        } else {
            color = Color.BLACK;
        }

        return color.getRGB();
    }
}