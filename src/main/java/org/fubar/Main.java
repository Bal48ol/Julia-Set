package org.fubar;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // значения по умолчанию:
        int width = 512;
        int height = 512;
        double realPart = -0.75;
        double imaginaryPart = 0.11;
        String outputFileName = "123.png";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-d" -> {
                    String[] dimensions = args[i + 1].split(";");
                    width = Integer.parseInt(dimensions[0]);
                    height = Integer.parseInt(dimensions[1]);
                }
                case "-c" -> {
                    String[] parts = args[i + 1].split(";");
                    realPart = Double.parseDouble(parts[0]);
                    imaginaryPart = Double.parseDouble(parts[1]);
                }
                case "-o" -> outputFileName = args[i + 1];
            }
        }

        generateJuliaFractal(width, height, realPart, imaginaryPart, outputFileName);
    }

    private static void generateJuliaFractal(int width, int height, double realPart, double imaginaryPart, String outputFileName) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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

        String fileExtension = outputFileName.substring(outputFileName.lastIndexOf('.'));
        String uniqueOutputFileName = outputFileName.replace(fileExtension, "_" + "JuliaSet" + fileExtension);

        try {
            File output = new File(uniqueOutputFileName);
            ImageIO.write(image, "png", output);
            System.out.println("Изображение сохранено в файл: " + uniqueOutputFileName);
        } catch (IOException e) {
            System.err.println("Изображение не сохранено в файл: " + e.getMessage());
        }
    }


    private static int calculatePixelColor(int x, int y, int width, int height, double realPart, double imaginaryPart) {
        double zx = 1.5 * (x - (double) width / 2) / (0.5 * width);
        double zy = (y - (double) height / 2) / (0.5 * height);

        int maxIterations = 300;
        int iteration = 0;
        while (zx * zx + zy * zy < 4 && iteration < maxIterations) {
            double xTemp = zx * zx - zy * zy + realPart;
            zy = 2.0 * zx * zy + imaginaryPart;
            zx = xTemp;
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