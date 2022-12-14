package com.mybatisplus.utils;

import catcode.Neko;
import com.mybatisplus.config.minio.config.service.impl.MinIOFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Component
public class Send_To_minio {
    @Autowired
    private MinIOFileStorageService fileStorageService;
//返回值是存储的url地址
    public String Send_To_minio_Picture(String url ){
        HttpURLConnection conn = null;
        String replace = UUID.randomUUID().toString().replace("-", "")+".jpg";
        InputStream in=null;
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection)url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            in =  conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileId = null;
        fileId = fileStorageService.uploadImgFile("", replace, in);
        return fileId;
    }

    public void Send_To_minio_Delete(String url ){
        fileStorageService.delete(url);
    }
    public void Send_To_minio_Delete_By_Cat(String cat ){
        String[] split = cat.split("file=");
        String substring = split[1].replace("]", "");
        fileStorageService.delete(substring);
    }


    public String Send_To_minio_Picture(Neko neko){
        String url = neko.get("url");
        HttpURLConnection conn = null;
        String replace = UUID.randomUUID().toString().replace("-", "")+".jpg";
        InputStream in=null;
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection)url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            in =  conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileId = null;
        fileId = fileStorageService.uploadImgFile("", replace, in);
        return fileId;
    }

}

