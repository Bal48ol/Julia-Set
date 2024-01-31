package org.fubar;

import org.fubar.params.dto.FractalParameters;

import static org.fubar.gen.FractalGenerator.generateJuliaFractal;
import static org.fubar.params.FractalParametersParser.parseArguments;

public class Main {
    public static void main(String[] args) {
        FractalParameters parameters = parseArguments(args);
        generateJuliaFractal(parameters);
    }
}