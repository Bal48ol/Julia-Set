package org.fubar.gen;

import org.fubar.params.dto.FractalParameters;

import java.awt.image.BufferedImage;
import static org.fubar.gen.FractalImage.*;

public class FractalGenerator {

    public void generateJuliaFractal(FractalParameters parameters) {
        int width = parameters.width();
        int height = parameters.height();
        double realPart = parameters.realPart();
        double imaginaryPart = parameters.imaginaryPart();
        String outputFileName = parameters.outputFileName();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        generateJuliaFractalSingleThread(image, width, height, realPart, imaginaryPart);
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения для одного потока: " + (endTime - startTime) + " мс");

        startTime = System.currentTimeMillis();
        generateJuliaFractalFixedThreadPool(image, width, height, realPart, imaginaryPart);
        endTime = System.currentTimeMillis();
        System.out.println("Время выполнения для fixed пула: " + (endTime - startTime) + " мс");

        startTime = System.currentTimeMillis();
        generateJuliaFractalForkJoinPool(image, width, height, realPart, imaginaryPart);
        endTime = System.currentTimeMillis();
        System.out.println("Время выполнения для fork-join пула: " + (endTime - startTime) + " мс");

        String fileExtension = outputFileName.substring(outputFileName.lastIndexOf('.'));
        String uniqueOutputFileName = outputFileName.replace(fileExtension, "_" + "JuliaSet" + fileExtension);

        saveImageToFile(image, uniqueOutputFileName);
    }
}