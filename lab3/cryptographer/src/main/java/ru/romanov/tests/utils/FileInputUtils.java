package ru.romanov.tests.utils;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileInputUtils {
    /**
     * Получаем json
     */
    public static String getJson(String path) {
        String json = null;
        try {
            json = String.join(" ",
                    Files.readAllLines(
                            Paths.get(path),
                            StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static String getFile(String path) {
        String inner = null;
        try {
            inner = String.join(" ",
                    Files.readAllLines(
                            Paths.get(path),
                            StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return inner;
    }
}
