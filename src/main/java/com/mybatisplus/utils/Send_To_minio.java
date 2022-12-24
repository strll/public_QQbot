package com.mybatisplus.utils;

import catcode.Neko;
import com.mybatisplus.config.minio.config.service.impl.MinIOFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@Component
public class Send_To_minio {
    @Autowired
    private MinIOFileStorageService fileStorageService;
//返回值是存储的url地址

    @Deprecated
    public String Send_To_minio_Picture(String url ){


        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        URLConnection con = null;
        try {
            con = url1.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is=null;

        try {
            is = con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        String replace = UUID.randomUUID().toString().replace("-", "")+".jpg";


        String fileId = null;
        fileId = fileStorageService.uploadImgFile("", replace, is,"jpg");
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


    public String Send_To_minio_Picture_By_Neko(Neko neko) throws IOException {
        String neko_url = neko.get("url");
        String imageType = neko.get("imageType");
        URL url = null;
        String fileId = null;
        try {
            url = new URL(neko_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            assert url != null;
            inputStream = url.openStream();
            assert  inputStream  != null;
            String replace = UUID.randomUUID().toString().replace("-", "")+"."+imageType.toLowerCase();
            fileId = fileStorageService.uploadImgFile("", replace, inputStream ,imageType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileId;
    }




}

