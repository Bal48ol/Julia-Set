package org.fubar;

import org.fubar.gen.FractalGenerator;
import org.fubar.params.FractalParametersParser;
import org.fubar.params.dto.FractalParameters;

public class Main {
    public static void main(String[] args) {
        FractalParametersParser parser = new FractalParametersParser();
        FractalParameters parameters = parser.parseArguments(args);
        FractalGenerator generator = new FractalGenerator();
        generator.generateJuliaFractal(parameters);
    }
}