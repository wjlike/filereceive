package com.hsbc.upload.fileupload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BussinessConfig {
    private static String filePath;

    @Value("${wechat.filePath}")
    public  void setFilePath(String filePath) {
        BussinessConfig.filePath = filePath;
    }

    public static String getFilePath() {
        return filePath;
    }
}
