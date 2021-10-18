package com.zzm.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${waifu2x.executable}")
    private String waifu2x;

    public static ConcurrentHashMap<String,String> concurrentHashMap = new ConcurrentHashMap<>();

    @Async("taskExecutor")
    public void callCmdToTransform(String inputImagePath, String outputImagePath, int noiseGrade, int scale, String id) {

        ProcessBuilder pb = new ProcessBuilder();
        pb.command(waifu2x, "-i", inputImagePath, "-o", outputImagePath, "-n", String.valueOf(noiseGrade), "-s", String.valueOf(scale));
        
        Process process = null;
        try {
            process = pb.start();
            int exitCode = process.waitFor();
            System.out.println("cmd run finish! with code: " + exitCode);
            concurrentHashMap.put(id,id);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}
