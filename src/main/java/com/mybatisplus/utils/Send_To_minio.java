package com.mybatisplus.utils;

import catcode.Neko;
import com.mybatisplus.config.minio.config.service.impl.MinIOFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.UUID;

@Component
public class Send_To_minio {
    @Autowired
    private MinIOFileStorageService fileStorageService;
//返回值是存储的url地址

    public String Send_ToMinio_Picture_new(String url1) throws IOException {
        URL url = new URL(url1);
        Random rand = new Random();
        int lowerBound = 1;
        int upperBound = 2000000;
        int randomInt = rand.nextInt(upperBound - lowerBound + 1) + lowerBound;

 String name =randomInt+"1.jpg";
        InputStream inputStream = url.openStream();
        FileOutputStream outputStream = new FileOutputStream(name);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();

        File file = new File(name);
        InputStream inputStream1 = new FileInputStream(file);


        String replace = UUID.randomUUID().toString().replace("-", "");
      //  String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = fileStorageService.uploadImgFile("", replace,  inputStream1 );
        System.out.println(fileId);

        inputStream1.close();
        file.delete();
        return  fileId;


    }


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



    public String Send_To_minio_Picture(String url ,String type){


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
        String replace = UUID.randomUUID().toString().replace("-", "")+"."+type.toLowerCase();


        String fileId = null;
        fileId = fileStorageService.uploadImgFile("", replace, is,type.toLowerCase());
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

