package com.xiongyi.contentsales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongyi on 2019/1/5.
 */
@Controller
@RequestMapping(value = "/file")
public class FileController {

    private static final String IMAGE_PATH = "D:/contentsales/src/main/resources/images/";

    /**
     * 将图片保存在本地
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> upload(MultipartFile file) {
        Map<String, String> res = new HashMap<>();
        String path = IMAGE_PATH + file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                // 保存图片
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(path)));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                res.put("message", "upload error," + e.getMessage());
                return res;
            }
        }
        res.put("result",path);
        return res;
    }
}
