package com.education.core.controller;

import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传接口
 *   
 *   

 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    protected static final Set<String> excelTypes = new HashSet<String>() {
        {
            add("application/x-xls");
            add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            add("application/vnd.ms-excel");
            add("text/xml");
        }
    };

    @Value("${file.uploadPath}")
    private String baseUploadPath;

    // 上传文件类型
    private static final int VIDEO_FILE = 1;
    private static final int IMAGE_FILE = 2;
    private static final int OTHER_FILE = 3;

    private static final Set<String> videoTypes = new HashSet<String>() {
        {
            add("video/mp4");
            add("video/x-ms-wmv");
            add("video/mpeg4");
            add("video/avi");
            add("video/mpeg");
            add("video/3gp");
        }
    };

    /**
     * 文件上传api 接口
     * @param file
     * @param uploadFileType
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "{uploadFileType}", method = {RequestMethod.GET, RequestMethod.POST})
    public Map uploadFile(@RequestParam MultipartFile file, @PathVariable int uploadFileType) throws IOException {
        String result = null;
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String suffix = "." + FilenameUtils.getExtension(fileName);
        String message = null;
        Map resultMap = new HashMap<>();
        switch (uploadFileType) {
            case VIDEO_FILE :
                if (file.getSize() > 500 * 1024 * 1024) {
                    resultMap.put("code", ResultCode.FAIL);
                    resultMap.put("message", "视频大小不能超过500MB");
                    return resultMap;
                }
                result = beforeUploadVideo(contentType, fileName);
                message = "视频";
                break;
            case IMAGE_FILE :
                result = beforeUploadImage(suffix);
                message = "图片";
                break;
            case OTHER_FILE :
                result = beforeUploadOtherFile(fileName);
                break;
        }

        if (ObjectUtils.isNotEmpty(result)) {
            try {
                String basePath = baseUploadPath + result;
                File filePath = new File(basePath);
                if (!filePath.getParentFile().exists()) {
                    filePath.getParentFile().mkdirs();
                }
                file.transferTo(filePath);
                resultMap.put("code", ResultCode.SUCCESS);
                resultMap.put("message", message + "上传成功");
                resultMap.put("url", result);
            } catch (Exception e) {
                resultMap.put("code", ResultCode.FAIL);
                resultMap.put("message", message + "文件上传失败");
                logger.error(message + "上传失败", e);
            }
        } else {
            resultMap.put("code", ResultCode.FAIL);
            resultMap.put("message", message + "文件格式错误,请更换文件");
        }
        return resultMap;
    }


    private String beforeUploadVideo(String contentType, String fileName) {
        if (videoTypes.contains(contentType)) {
            return "/videos/" + ObjectUtils.generateFileByTime() + ObjectUtils.generateUuId() + "/" + fileName;
        }
        return null;
    }

    private String beforeUploadImage(String suffix) {
        return "/images/" + ObjectUtils.generateFileByTime() + ObjectUtils.generateUuId() + suffix;
    }

    private String beforeUploadOtherFile(String fileName) {
        return "/others/" + ObjectUtils.generateFileByTime() + ObjectUtils.generateUuId() + "/" + fileName;
    }
}
