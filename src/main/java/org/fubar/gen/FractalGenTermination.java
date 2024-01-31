package org.fubar.gen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class FractalGenTermination {

    static void shutdownForkJoinPool(ForkJoinPool forkJoinPool) {
        if (forkJoinPool != null) {
            forkJoinPool.shutdown();
            try {
                if (!forkJoinPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    forkJoinPool.shutdownNow();
                    if (!forkJoinPool.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println("Не удалось корректно завершить ForkJoinPool");
                    }
                } else {
                    System.out.println("Потоки успешно завершились");
                }
            } catch (InterruptedException e) {
                forkJoinPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    static void shutdownExecutor(ExecutorService executor) {
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

    static void saveImageToFile(BufferedImage image, String fileName) {
        try {
            File output = new File(fileName);
            ImageIO.write(image, "png", output);
            System.out.println("Изображение сохранено в файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Изображение не сохранено в файл: " + e.getMessage());
        }
    }
}
