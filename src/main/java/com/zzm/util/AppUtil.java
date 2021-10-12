package com.zzm.util;

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

    public static void byteArrayToFile(byte[] byteArray, String filePath) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        //这样子写 try 可以自动关闭 byteArrayInputStream
        try (byteArrayInputStream) {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            File file = new File(filePath);
            ImageIO.write(bufferedImage, "jpg", file);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
