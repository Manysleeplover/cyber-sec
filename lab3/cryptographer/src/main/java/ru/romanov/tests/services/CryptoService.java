package ru.romanov.tests.services;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import static ru.romanov.tests.utils.FileInputUtils.getFile;
import static ru.romanov.tests.utils.FileInputUtils.getJson;


@Service
@Slf4j
public class CryptoService {

    public boolean encryptFile(String userKeys) {
        log.info("Пришёл ключ: " + userKeys);
        String pathToFile = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/src/users.json";
        String json = getJson(pathToFile);


        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(json.getBytes());

        /*"/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files"*/

        String FILE_PATH = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/src/cipherFile.txt";

        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH))) {
            FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.WRITE).truncate(0).close();
            out.write(encoded);
            log.info("Записали шифрованные данные во временный файл cipherFile.txt");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean decryptFile(String userKeys) {
        log.info("Пришёл ключ: " + userKeys);
        String SRC_FILE_PATH = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/src/cipherFile.txt";

        String src = getFile(SRC_FILE_PATH);


        Base64.Decoder decoder = Base64.getDecoder();
        String decoded = new String(decoder.decode(src));
        JSONObject jsonObject = new JSONObject(decoded);

        /*"/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files"*/

        String FILE_PATH = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/temp/users.json";

        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH))) {
            FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.WRITE).truncate(0).close();
            out.write(jsonObject.toString(4));
            log.info("Записали дешифрованные данные во временный файл users.json");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExpectedPassword(String password) {
        return getFile("/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/password/pswrd.txt").equals(password);
    }


    public void writePasswordToFile(String key) {
        String FILE_PATH = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/password/pswrd.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH))) {
            FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.WRITE).truncate(0).close();
            out.write(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearTempFiles(){
        try {
            Files.delete(Path.of("/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/temp/users.json"));
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
