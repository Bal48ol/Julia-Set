package org.fubar.gen;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static org.fubar.gen.FractalGenPixelCalc.calculatePixelColor;
import static org.fubar.gen.FractalGenTermination.shutdownExecutor;
import static org.fubar.gen.FractalGenTermination.shutdownForkJoinPool;

public class FractalGenMethods {

    public static void generateJuliaFractalSingleThread(BufferedImage image, int width, int height, double realPart, double imaginaryPart) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = calculatePixelColor(x, y, width, height, realPart, imaginaryPart);
                image.setRGB(x, y, color);
            }
        }
    }

    public static void generateJuliaFractalFixedThreadPool(BufferedImage image, int width, int height, double realPart, double imaginaryPart) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("\nДоступные ресурсы процессора: " + availableProcessors);
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        for (int y = 0; y < height; y++) {
            final int row = y;
            executor.execute(() -> {
                for (int x = 0; x < width; x++) {
                    int color = calculatePixelColor(x, row, width, height, realPart, imaginaryPart);
                    image.setRGB(x, row, color);
                }
            });
        }
        shutdownExecutor(executor);
    }

    public static void generateJuliaFractalForkJoinPool(BufferedImage image, int width, int height, double realPart, double imaginaryPart) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        try {
            forkJoinPool.submit(() -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int color = calculatePixelColor(x, y, width, height, realPart, imaginaryPart);
                        image.setRGB(x, y, color);
                    }
                }
            }).invoke();
        } finally {
            shutdownForkJoinPool(forkJoinPool);
        }
    }
}