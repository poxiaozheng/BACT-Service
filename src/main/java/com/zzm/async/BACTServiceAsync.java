package com.zzm.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class BACTServiceAsync {

    public volatile static HashSet<String> hashSet = new HashSet<>();

    @Async("taskExecutor")
    public void callCmdToTransform(String cmd1, String inputImagePath, String outputImagePath, int noiseGrade, int scale,
                                   String id) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        String cmd2 = "waifu2x-ncnn-vulkan.exe -i " + inputImagePath + " -o " + outputImagePath + " -n " + noiseGrade + " -s " + scale;
        try {
            // cmd /c是运行完后关闭窗口，两个cmd命令中间用&&连接起来
            process = runtime.exec("cmd /c " + cmd1 + " && " + cmd2);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line + "\n");
            }
            System.out.println("cmd run finish!");
            hashSet.add(id);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}
