package com.hsbc.upload.fileupload.controller;

import com.alibaba.fastjson.JSONObject;
import com.hsbc.upload.fileupload.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/key")
public class CommonControllor {
    @Value("${wechat.prikeyurl}")
    private String prikeyUrl;

    @GetMapping("/prikey")
    public String getPrikey() throws IOException {
        JSONObject jsonObject = HttpUtil.doGetJsonByUrl(prikeyUrl);
        String  prikey = (String)jsonObject.get("prikey");
        return prikey;
    }
}
