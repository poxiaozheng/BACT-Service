package com.zzm.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class AppUtil {

    public static String createRandomStr(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            stringBuilder.append(str.charAt(number));
        }
        return stringBuilder.toString();
    }

    public static byte[] fileToByteArray(File image) {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //这样子写 try 可以自动关闭 byteArrayOutputStream
        try (byteArrayOutputStream) {
            BufferedImage bufferedImage = ImageIO.read(image);
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            System.out.println(bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void byteArrayToFile(byte[] byteArray, String filePath) throws IOException {
        //这样子写 try 可以自动关闭 byteArrayInputStream

            FileUtils.writeByteArrayToFile(new File(filePath), byteArray);
    }
}
