package com.zzm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@SpringBootApplication
public class BACTService {

    @PostMapping("/postOriginImage")
    @ResponseBody
    private Object postOriginImage(@RequestParam("multiple") Integer multiple, @RequestParam("noiseGrade")
            Integer noiseGrade, @RequestBody Byte[] pictureArray) {
        if (multiple == null && noiseGrade == null && pictureArray == null) {
            return postOriginImageErrorResponse();
        }
        log.debug("multiple:" + multiple + ",noiseGrade:" + noiseGrade + ",pictureArray:" +
                Arrays.toString(pictureArray));
        //TODO pictureArray解析成图片
        // 生成唯一id
        // 生成随机receipt
        // 调用命令行工具进行图片转换
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", 0);
        map.put("imageId", "2");
        map.put("receipt", "110");
        return map;
    }

    @RequestMapping("/queryProgress")
    @ResponseBody
    private Object queryProgress(@RequestParam("imageId") String imageId, @RequestParam("receipt") String receipt) {
        if (imageId == null && receipt == null) {
            return queryProgressErrorResponse();
        }
        log.debug("imageId:" + imageId + ",receipt:" + receipt);
        //TODO 校验imageId与receipt是否正确
        // 正确的话看图片是否转换成功
        // 是的话即可返回imageUrl
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", 0);
        map.put("imageUrl", "http://oppo.com");
        return map;
    }

    private Map<String, Object> postOriginImageErrorResponse() {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", -1);
        map.put("imageId", "");
        map.put("receipt", "");
        return map;
    }

    private Map<String, Object> queryProgressErrorResponse() {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", -1);
        map.put("imageUrl", "");
        return map;
    }

    public static void main(String[] args) {
        SpringApplication.run(BACTService.class, args);
    }
}
