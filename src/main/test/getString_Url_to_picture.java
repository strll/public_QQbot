import com.mybatisplus.utils.Get_Url_in_Text_and_get_Picture_from_Url;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class getString_Url_to_picture {
    @Test
    public void pictureTes1t(){
        Get_Url_in_Text_and_get_Picture_from_Url a1 = new Get_Url_in_Text_and_get_Picture_from_Url();
        String picture = a1.getPicture("   //https://zh.moegirl.org.cn/index.php?title=Special:%E6%90%9C%E7%B4%A2&variant=Special%3ASearch&search=%E8%99%8E%E5%93%A5&searchToken=8ex0ytzqg9j9itv0blv6qmfuw\n" +
                "    //https://zh.moegirl.org.cn/度娘");
        System.out.println(picture);

    }


    @Test
    public void test ()  {
       String url1="https://mini.s-shot.ru/1024x768/PNG/800/?https://leetcode.cn";



        try {
            //定义一个URL对象，就是你想下载的图片的URL地址
            URL url = new URL(url1);
            //打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //通过输入流获取图片数据
            InputStream is = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(is);
            //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫Copy.jpg
            File imageFile = new File("Copy.jpg");
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            //关闭输出流，释放资源
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[6024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    }



