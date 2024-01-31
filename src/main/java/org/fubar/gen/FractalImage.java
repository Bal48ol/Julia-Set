package org.fubar.gen;

import org.fubar.math.ComplexNumber;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class FractalImage {

    public static void generateJuliaFractalSingleThread(BufferedImage image, int width, int height, double realPart, double imaginaryPart) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = calculatePixelColor(x, y, width, height, realPart, imaginaryPart);
                image.setRGB(x, y, color);
            }
        }
    }

    public static void generateJuliaFractalFixedThreadPool(BufferedImage image, int width, int height, double realPart, double imaginaryPart) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
        try (ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors())) {
            forkJoinPool.submit(() -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int color = calculatePixelColor(x, y, width, height, realPart, imaginaryPart);
                        image.setRGB(x, y, color);
                    }
                }
            }).invoke();
        }
    }

    public static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
            if (terminated) {
                System.out.println("Потоки успешно завершились.");
            } else {
                System.err.println("Истекло время ожидания завершения потоков.");
            }
        } catch (InterruptedException e) {
            System.err.println("Произошло прерывание при ожидании завершения потоков.");
        }
    }

    public static void saveImageToFile(BufferedImage image, String fileName) {
        try {
            File output = new File(fileName);
            ImageIO.write(image, "png", output);
            System.out.println("Изображение сохранено в файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Изображение не сохранено в файл: " + e.getMessage());
        }
    }

    private static int calculatePixelColor(int x, int y, int width, int height, double realPart, double imaginaryPart) {
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