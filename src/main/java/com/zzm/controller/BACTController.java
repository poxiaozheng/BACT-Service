package com.zzm.controller;

import com.zzm.async.BACTServiceAsync;
import com.zzm.entity.ImageEntity;
import com.zzm.service.BACTService;
import com.zzm.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/bact")
public class BACTController {

//    //TODO 根据具体PAth更换
//    String originImagePathPre = "D:\\Code\\back\\BACT-Service\\src\\main\\resources\\static\\input\\";
//    String processedImagePathPre = "D:\\Code\\back\\BACT-Service\\target\\classes\\static\\output\\";
//    //TODO 根据具体ip地址更换
//    String saveImagePathPre = "http://192.168.88.194:8081/bact/output/";

    private volatile int imageId = 0;
    private final List<ImageEntity> list = new ArrayList<>();

    @Value("${waifu2x.input}")
    private File inputDir;

    @Value("${waifu2x.output}")
    private File outputDir;

    @Autowired
    private BACTServiceAsync bactServiceAsync;

    @GetMapping("/hello")
    private String hello() {
        return "hello world zzm!";
    }

    @GetMapping("/test")
    public ResponseEntity<byte[]> sendSampleImage() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(AppUtil.fileToByteArray(new File("sample.png")));
    }

    @PostMapping("/postOriginImage")
    @ResponseBody
    private synchronized HashMap<String, Object> postOriginImage
            (@RequestParam("scale") Integer scale,
             @RequestParam("noiseGrade")
                     Integer noiseGrade,
             @RequestBody byte[] pictureArray) {

        if (scale == null && noiseGrade == null && pictureArray == null) {
            return postOriginImageErrorResponse();
        }
        System.out.println("scale:" + scale + ",noiseGrade:" + noiseGrade);

        long imagePre = System.currentTimeMillis();

        String originImageUrl = imagePre + "_input.jpg";
        String processImageUrl = imagePre + "_output.png";
        File originImagePath = new File(inputDir, originImageUrl);
        File processedImagePath = new File(outputDir, processImageUrl);
        AppUtil.byteArrayToFile(pictureArray, originImagePath.getAbsolutePath());

        String id = String.valueOf(imageId++);
        String receipt = AppUtil.createRandomStr(20);
        bactServiceAsync.callCmdToTransform(
                originImagePath.getAbsolutePath(),
                processedImagePath.getAbsolutePath(),
                noiseGrade,
                scale,
                id);

        // 不再需要加前缀。
        String savePath = processImageUrl;
        saveData(id, receipt, originImagePath.getAbsolutePath(), savePath);
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
        System.out.println("imageId:" + imageId + ",receipt:" + receipt);
        Iterator<ImageEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            ImageEntity imageEntity = iterator.next();
            if (imageEntity.getImageId().equals(imageId) && imageEntity.getReceipt()
                                                                       .equals(receipt)) {
                System.out.println("bactServiceAsync.hashSet:" + BACTServiceAsync.hashSet);
                if (BACTServiceAsync.hashSet.contains(imageId)) {
                    iterator.remove();
                    BACTServiceAsync.hashSet.remove(imageId);
                    return queryProgressSuccessResponse(imageEntity.getProcessedImagePath());
                }
            }
        }
        return queryProgressErrorResponse();
    }

    @GetMapping("/output/{fileName}")
    public ResponseEntity<Object> getOutput(@PathVariable("fileName") String fileName) throws FileNotFoundException {
        try {
            return ResponseEntity
                    .ok()
                    .body(new FileInputStream(new File(outputDir, fileName)));
        } catch (FileNotFoundException fineNotFound) {
            return ResponseEntity.badRequest().body("File " + fileName + " not found!");
        }
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
