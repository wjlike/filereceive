package com.hsbc.upload.fileupload.controller;

import com.hsbc.upload.fileupload.config.BussinessConfig;
import com.hsbc.upload.fileupload.util.FileUtil;
import com.hsbc.upload.fileupload.util.UZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/filereceive")
public class FileReceiveController {

    /**
     * 单个文件上传
     * @param file
     */
    @PostMapping("/upload")
    public String receiveFile(@RequestParam("file") MultipartFile file,int total){
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = file.getOriginalFilename();
        String filePath = BussinessConfig.getFilePath();
        File dest = new File(filePath + fileName);
        log.info("文件路径：{}",filePath + fileName);
        try {
            FileUtil.writeStringToFile(String.valueOf(total),filePath + "total-"+fileName);
            file.transferTo(dest);
            //unZip 解压
            unZip(dest,filePath);
            log.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return "上传失败！";

    }

    @PostMapping("/uploads")
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = BussinessConfig.getFilePath();
        int filetotal = files.size();
        FileUtil.writeStringToFile(String.valueOf(filetotal),filePath + "total-"+files.get(0).getOriginalFilename().split("/")[0]);
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "上传第" + (i++) + "个文件失败";
            }
            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                log.info("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                log.error(e.toString(), e);
                return "上传第" + (i++) + "个文件失败";
            }
        }

        return "上传成功";

    }

    /**
     * 解压
     */
    private static  void unZip(File dest,String filePath) throws IOException {
        UZipUtil.unZipFiles(dest,filePath);
    }
}
