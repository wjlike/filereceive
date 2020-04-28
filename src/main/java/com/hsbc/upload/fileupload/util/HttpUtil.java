package com.hsbc.upload.fileupload.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


@Slf4j
public class HttpUtil {
    /**
     *  getRequest
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetJsonByUrl(String url) throws IOException {
        log.info("request url : {}",url);
        JSONObject jsonObject = null;
        HttpClient client = null;
        HttpGet httpGet = null;
        try {
            client = HttpClientBuilder.create().build();
            httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
            return jsonObject;
        }
    }

}
