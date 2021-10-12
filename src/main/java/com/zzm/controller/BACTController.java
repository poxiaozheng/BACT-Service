package com.zzm.controller;

import com.zzm.async.BACTServiceAsync;
import com.zzm.entity.ImageEntity;
import com.zzm.service.BACTService;
import com.zzm.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
public class BACTController {

    private volatile int imageId = 0;
    private List<ImageEntity> list = new ArrayList<>();

    @Autowired
    private BACTServiceAsync bactServiceAsync;

    @RequestMapping("/hello")
    private String hello() {
        return "hello world zzm!";
    }

    @PostMapping("/postOriginImage")
    @ResponseBody
    private synchronized HashMap<String, Object> postOriginImage(@RequestParam("scale") Integer scale, @RequestParam("noiseGrade")
            Integer noiseGrade, @RequestBody byte[] pictureArray) {
        if (scale == null && noiseGrade == null && pictureArray == null) {
            return postOriginImageErrorResponse();
        }
        log.debug("scale:" + scale + ",noiseGrade:" + noiseGrade + ",pictureArray:" +
                Arrays.toString(pictureArray));

        String originImagePath = "D:\\waifu2x\\input\\" + System.currentTimeMillis() + "_input.jpg";
        AppUtil.byteArrayToFile(pictureArray, originImagePath);
        String cmd1 = "cd D:\\waifu2x";
        String processedImagePath = "D:\\waifu2x\\ouput\\" + System.currentTimeMillis() + "_output.png";
        String id = String.valueOf(imageId++);
        String receipt = AppUtil.createRandomStr(20);

        bactServiceAsync.callCmdToTransform(cmd1, originImagePath, processedImagePath, noiseGrade, scale, id);

        saveData(id, receipt, originImagePath, processedImagePath);
        return postOriginImageSuccessResponse(id, receipt);
    }

    private void saveData(String id, String receipt, String originImagePath, String processedImagePath) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageId(id);
        imageEntity.setReceipt(receipt);
        imageEntity.setOriginImagePath(originImagePath);
        imageEntity.setProcessedImagePath(processedImagePath);
        list.add(imageEntity);
    }

    @RequestMapping("/queryProgress")
    @ResponseBody
    private Object queryProgress(@RequestParam("imageId") String imageId, @RequestParam("receipt") String receipt) {
        if (imageId == null && receipt == null) {
            return queryProgressErrorResponse();
        }
        log.debug("imageId:" + imageId + ",receipt:" + receipt);
        //TODO
        // 校验imageId与receipt是否正确
        // 正确的话看图片是否转换成功
        // 是的话即可返回imageUrl



        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", 0);
        map.put("imageUrl", "http://oppo.com");
        return map;
    }

    private HashMap<String, Object> postOriginImageSuccessResponse(String imageId, String receipt) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("statusCode", 0);
        map.put("imageId", imageId);
        map.put("receipt", receipt);
        return map;
    }

    private HashMap<String, Object> postOriginImageErrorResponse() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("statusCode", -1);
        map.put("imageId", "");
        map.put("receipt", "");
        return map;
    }

    private Map<String, Object> queryProgressSuccessResponse(String imageUrl) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", 0);
        map.put("imageUrl", imageUrl);
        return map;
    }

    private Map<String, Object> queryProgressErrorResponse() {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", -1);
        map.put("imageUrl", "");
        return map;
    }
}
