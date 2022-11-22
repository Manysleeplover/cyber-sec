package ru.romanov.tests.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static ru.romanov.tests.utils.JsonUtils.getJson;


@Service
@Slf4j
public class CryptoService {

    private String pathToFile = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/src/users.json";

    public boolean encryptFile(String userKeys) {
        log.info("Пришёл ключ: " + userKeys);
        String json = getJson(pathToFile);
        byte[] arrJson = json.getBytes(StandardCharsets.UTF_8);
        byte[] arrUserKey = userKeys.getBytes(StandardCharsets.UTF_8);
        byte[] cipherJson = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


            String algorithm = "AES";
            SecretKeySpec key = new SecretKeySpec(arrUserKey, algorithm);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            log.info("Инициализируем шифровальщик");

            cipherJson = cipher.doFinal(arrJson);
            log.info("Зашифровали файл");


        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }

        /*"/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files"*/

        String FILE_PATH = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/src/cipherFile.txt";

        try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.WRITE).truncate(0).close();
            outputStream.write(cipherJson);
            log.info("Записали шифрованные данные во временный файл cipherFile.txt");
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean decryptFile(String userKeys) {
        log.info("Пришёл ключ: " + userKeys);
        /**
         *
         */
        byte[] arrUserKey = userKeys.getBytes(StandardCharsets.UTF_8);
        byte[] cipherJson = null;
        byte[] array = new byte[0];
        try {
            array = Files.readAllBytes(Paths.get("/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/src/cipherFile.txt"));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


            String algorithm = "AES";
            SecretKeySpec key = new SecretKeySpec(arrUserKey, algorithm);

            cipher.init(Cipher.DECRYPT_MODE, key);
            log.info("Инициализируем дешифровальщик");

            cipherJson = cipher.doFinal(array);
            log.info("Дешифровали файл");
        } catch (IOException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }




        /*"/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files"*/

        String FILE_PATH = "/home/ioromanov/ideaProjects/cyber-sec/cyber-sec/lab3/files/temp/users.json";

        try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.WRITE).truncate(0).close();
            outputStream.write(cipherJson);
            log.info("Записали дешифрованные данные во временный файл users.json");
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

}
