package com.zzm.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ImageEntity {

    private String originImagePath;
    private String imageId;
    private String receipt;
    private String processedImagePath;

    @Override
    public String toString() {
        return "ImageEntity [originImage=" + originImagePath + ",imageId=" + imageId + ",receipt=" +
                receipt + ",processedImage=" + processedImagePath+"]";
    }
}
