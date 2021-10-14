package com.zzm;

import com.zzm.entity.ImageEntity;
import com.zzm.service.BACTService;
import com.zzm.util.AppUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ClassUtils;

import java.io.*;

@EnableAsync
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
//        File file = new File("E:\\input.jpg");
//        byte[] result = AppUtil.fileToByteArray(file);
//
//        File f = new File("E:\\test1.txt"); //创建文件对象
//        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(f));
//        for (byte value : result) {
//            outputStream.write(value);//1.用OutputStream向文件写入字节流
//        }
//        outputStream.close();
//        File file = new File("D:\\waifu2x\\input.jpg");
//        byte[] pictureArray = AppUtil.fileToByteArray(file);
//        String originImagePath = "D:\\learnCode\\BACTService\\src\\main\\resources\\static\\input\\"
//                + System.currentTimeMillis() + "_input.jpg";
//        String processedImagePath = "D:\\learnCode\\BACTService\\target\\classes\\static\\output\\"
//                + System.currentTimeMillis() + "_output.png";
//        AppUtil.byteArrayToFile(pictureArray, originImagePath);
//        String cmd1 = "cd D:\\waifu2x";
//        callCmdToTransform(cmd1, originImagePath, processedImagePath, 2, 2);
    }

//    public static void callCmdToTransform(String cmd1, String inputImagePath, String outputImagePath, int noiseGrade, int scale) {
//        long time1 = System.currentTimeMillis();
//        Runtime runtime = Runtime.getRuntime();
//        Process process = null;
//        String cmd2 = "waifu2x-ncnn-vulkan.exe -i " + inputImagePath + " -o " + outputImagePath + " -n " + noiseGrade + " -s " + scale;
//        try {
//            // cmd /c是运行完后关闭窗口，两个cmd命令中间用&&连接起来
//            process = runtime.exec("cmd /c " + cmd1 + " && " + cmd2);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line + "\n");
//            }
//            System.out.println("cmd run finish!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (process != null) {
//                process.destroy();
//            }
//        }
//        long time2 = System.currentTimeMillis();
//        System.out.println("time:" + (time2 - time1));
//    }
}
