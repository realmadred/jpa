package com.example.jpa.controller;

import com.baidu.ueditor.ActionEnter;
import com.example.jpa.config.ImageConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/ueditor")
public class ImageController {

    @Autowired
    private ImageConfig imageConfig;

    @GetMapping("/base")
    public void base(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding( "utf-8" );
        response.setHeader("Content-Type" , "text/html");
        String path = imageConfig.getPath();
        String configPath = ImageController.class.getResource("/").getPath();
        try {
            String action = request.getParameter("action");
            if("config".equals(action)){
                path = configPath;
            }
            PrintWriter writer = response.getWriter();
            String exec = new ActionEnter(request, path, configPath + "config.json").exec();
            writer.write(exec.replaceAll(path,imageConfig.getBaseUrl()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/upload")
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
                fileName = System.currentTimeMillis() + RandomStringUtils.randomAlphabetic(4) + suffixName;
                // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
//                String realPath = ImageController.class.getResource("/").getPath();
//                String filePath = realPath + Constant.STATIC_PATH+Constant.IMAGE_UPDATE_PATH;
                File dest = new File(imageConfig.getPath() + fileName);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                    //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
                    result = "{\"state\": \"SUCCESS\"," +
                            "\"url\": \"" + imageConfig.getBaseUrl() + fileName + "\"," +
                            "\"title\": \"" + fileName + "\"," +
                            "\"original\": \"" + fileName + "\"}";
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
