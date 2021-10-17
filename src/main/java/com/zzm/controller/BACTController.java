package com.zzm.controller;

import com.zzm.async.BACTServiceAsync;
import com.zzm.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;

@Slf4j
@RestController
public class BACTController {

    //TODO 根据具体PAth更换
    String originImagePathPre = "D:\\Code\\back\\BACT-Service\\src\\main\\resources\\static\\input\\";
    String processedImagePathPre = "D:\\Code\\back\\BACT-Service\\target\\classes\\static\\output\\";
    //TODO 根据具体ip地址更换
    String saveImagePathPre = "http://192.168.88.194:8081/bact/output/";

    @Autowired
    private BACTServiceAsync bactServiceAsync;

    @RequestMapping("/hello")
    private String hello(){
        return "hello world！";
    }

    @PostMapping("/postOriginImage")
    @ResponseBody
    private synchronized HashMap<String, Object> postOriginImage(@RequestParam("scale") Integer scale, @RequestParam("noiseGrade")
            Integer noiseGrade, @RequestBody byte[] pictureArray) {
        if (scale == null && noiseGrade == null && pictureArray == null) {
            return postOriginImageErrorResponse();
        }
        System.out.println("scale:" + scale + ",noiseGrade:" + noiseGrade);

        long imagePre = System.currentTimeMillis();
        String originImageUrl = imagePre + "_input.jpg";
        String processImageUrl = imagePre + "_output.png";
        String originImagePath = originImagePathPre + originImageUrl;
        String processedImagePath = processedImagePathPre + processImageUrl;
        AppUtil.byteArrayToFile(pictureArray, originImagePath);
        String cmd1 = "cd D:\\PictureEnlarge\\waifu2x";
        bactServiceAsync.callCmdToTransform(cmd1, originImagePath, processedImagePath, noiseGrade, scale);
           String savePath = saveImagePathPre + processImageUrl;
           return postOriginImageSuccessResponse(savePath);
    }

    private HashMap<String, Object> postOriginImageSuccessResponse(String imageUrl) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("statusCode", 0);
        map.put("imageUrl", imageUrl);
        return map;
    }

    private HashMap<String, Object> postOriginImageErrorResponse() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("statusCode", -1);
        map.put("imageUrl", "");
        return map;
    }
}
