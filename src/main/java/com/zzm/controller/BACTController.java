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
    private final List<ImageEntity> list = new ArrayList<>();

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
        System.out.println("scale:" + scale + ",noiseGrade:" + noiseGrade + ",pictureArray:" +
                Arrays.toString(pictureArray));
        String originImagePath = "D:\\learnCode\\BACTService\\src\\main\\resources\\static\\input\\"
                + System.currentTimeMillis() + "_input.jpg";
        String processImageUrl = System.currentTimeMillis() + "_output.png";
        String processedImagePath = "D:\\learnCode\\BACTService\\src\\main\\resources\\static\\output\\"
                + processImageUrl;
        AppUtil.byteArrayToFile(pictureArray, originImagePath);

        String cmd1 = "cd D:\\waifu2x";
        String id = String.valueOf(imageId++);
        String receipt = AppUtil.createRandomStr(20);
        bactServiceAsync.callCmdToTransform(cmd1, originImagePath, processedImagePath, noiseGrade, scale, id);

        String savePath = "127.0.0.1:8081/bact/output/" + processImageUrl;
        saveData(id, receipt, originImagePath, savePath);
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
        Iterator<ImageEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            ImageEntity imageEntity = iterator.next();
            if (imageEntity.getImageId().equals(imageId) && imageEntity.getReceipt().equals(receipt)) {
                if (bactServiceAsync.hashSet.contains(imageId)) {
                    iterator.remove();
                    bactServiceAsync.hashSet.remove(imageId);
                    return queryProgressSuccessResponse(imageEntity.getProcessedImagePath());
                }
            }
        }
        return queryProgressErrorResponse();
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
