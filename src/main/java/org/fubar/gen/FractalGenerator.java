package org.fubar.gen;

import org.fubar.params.dto.FractalParameters;

import java.awt.image.BufferedImage;

import static org.fubar.gen.FractalGenMethods.*;
import static org.fubar.gen.FractalGenTermination.saveImageToFile;

public class FractalGenerator {

    public static void generateJuliaFractal(FractalParameters parameters) {
        int width = parameters.width();
        int height = parameters.height();
        double realPart = parameters.realPart();
        double imaginaryPart = parameters.imaginaryPart();
        String outputFileName = parameters.outputFileName();

        String uniqueOutputFileName = isValid(outputFileName);
        if (uniqueOutputFileName == null){
           System.exit(1);
        }

        if (width <= 0 || height <= 0) {
            System.err.println("\nНедопустимые параметры для генерации фрактала: Ширина и высота не могут быть <= 0");
            return;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        generateJuliaFractalSingleThread(image, width, height, realPart, imaginaryPart);
        long endTime = System.currentTimeMillis();
        System.out.println("\nВремя выполнения для одного потока: " + (endTime - startTime) + " мс");

        startTime = System.currentTimeMillis();
        generateJuliaFractalFixedThreadPool(image, width, height, realPart, imaginaryPart);
        endTime = System.currentTimeMillis();
        System.out.println("Время выполнения для fixed пула: " + (endTime - startTime) + " мс");

        startTime = System.currentTimeMillis();
        generateJuliaFractalForkJoinPool(image, width, height, realPart, imaginaryPart);
        endTime = System.currentTimeMillis();
        System.out.println("Время выполнения для fork-join пула: " + (endTime - startTime) + " мс");

        saveImageToFile(image, uniqueOutputFileName);
    }

    private static String isValid(String outputFileName) {
        String fileExtension;
        int dotIndex = outputFileName.lastIndexOf('.');
        if (dotIndex != -1) {
            fileExtension = outputFileName.substring(dotIndex);
        } else {
            System.err.println("\nНедопустимый путь для сохранения изображения (отсутствует расширение файла)");
            return null;
        }

        String uniqueOutputFileName = outputFileName.substring(0, dotIndex) + "_" + "JuliaSet" + fileExtension;
        if (!isValidImagePath(uniqueOutputFileName)) {
            System.err.println("\nНедопустимый формат для сохранения изображения (используйте .png)");
            return null;
        } else {
            return uniqueOutputFileName;
        }
    }

    public static boolean isValidImagePath(String path) {
        return path.matches(".*\\.png$");
    }
}