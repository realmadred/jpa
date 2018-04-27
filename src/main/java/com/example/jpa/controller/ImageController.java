package com.example.jpa.controller;

import com.example.jpa.common.Constant;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/img")
public class ImageController {

    @RequestMapping(value = "/update")
    @ResponseBody
    public String imgUpdate(MultipartHttpServletRequest request) {
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        Set<Map.Entry<String, List<MultipartFile>>> entries = multiFileMap.entrySet();
        String result = "error";
        for (Map.Entry<String, List<MultipartFile>> entry : entries) {
            List<MultipartFile> value = entry.getValue();
            for (MultipartFile file : value) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                // 获取文件名
                String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("1.jpg");
                // 获取文件的后缀名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                // 这里我使用随机字符串来重新命名图片
                fileName = Calendar.getInstance().getTimeInMillis() + RandomStringUtils.randomAlphabetic(4) + suffixName;
                // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
                String realPath = ImageController.class.getResource("/").getPath();
                String filePath = realPath + Constant.STATIC_PATH+Constant.IMAGE_UPDATE_PATH;
                File dest = new File(filePath + fileName);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                    //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
                    result = "{\"state\": \"SUCCESS\"," +
                            "\"url\": \"" + Constant.BASE_URL + Constant.IMAGE_UPDATE_PATH + fileName + "\"," +
                            "\"title\": \"" + fileName + "\"," +
                            "\"original\": \"" + fileName + "\"}";
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
