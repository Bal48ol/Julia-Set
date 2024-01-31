package org.fubar.params;

import org.fubar.params.dto.FractalParameters;

public class FractalParametersParser {
    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 512;
    private static final double DEFAULT_REAL_PART = -0.75;
    private static final double DEFAULT_IMAGINARY_PART = 0.11;
    private static final String DEFAULT_OUTPUT_FILENAME = "123.png";

    public static FractalParameters parseArguments(String[] args) {
        int defaultWidth = DEFAULT_WIDTH;
        int defaultHeight = DEFAULT_HEIGHT;
        double defaultRealPart = DEFAULT_REAL_PART;
        double defaultImaginaryPart = DEFAULT_IMAGINARY_PART;
        String defaultOutputFileName = DEFAULT_OUTPUT_FILENAME;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-d" -> {
                    String[] dimensions = args[i + 1].split(";");
                    defaultWidth = Integer.parseInt(dimensions[0]);
                    defaultHeight = Integer.parseInt(dimensions[1]);
                }
                case "-c" -> {
                    String[] parts = args[i + 1].split(";");
                    defaultRealPart = Double.parseDouble(parts[0]);
                    defaultImaginaryPart = Double.parseDouble(parts[1]);
                }
                case "-o" -> defaultOutputFileName = args[i + 1];
            }
        }

        return new FractalParameters(defaultWidth, defaultHeight, defaultRealPart, defaultImaginaryPart, defaultOutputFileName);
    }
}