package org.fubar.params.dto;

public record FractalParameters (int width, int height, double realPart, double imaginaryPart, String outputFileName) {
}