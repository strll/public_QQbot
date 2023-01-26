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
                "    //https://zh.moegirl.org.cn/����");
        System.out.println(picture);

    }


    @Test
    public void test ()  {
       String url1="https://mini.s-shot.ru/1024x768/PNG/800/?https://leetcode.cn";



        try {
            //����һ��URL���󣬾����������ص�ͼƬ��URL��ַ
            URL url = new URL(url1);
            //������
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //��������ʽΪ"GET"
            conn.setRequestMethod("GET");
            //��ʱ��Ӧʱ��Ϊ10��
            conn.setConnectTimeout(10 * 1000);
            //ͨ����������ȡͼƬ����
            InputStream is = conn.getInputStream();
            //�õ�ͼƬ�Ķ��������ݣ��Զ����Ʒ�װ�õ����ݣ�����ͨ����
            byte[] data = readInputStream(is);
            //����һ���ļ�������������ͼƬ��Ĭ�ϱ��浱ǰ���̸�Ŀ¼��������Copy.jpg
            File imageFile = new File("Copy.jpg");
            //���������
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //д������
            outStream.write(data);
            //�ر���������ͷ���Դ
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //����һ��Buffer�ַ���
        byte[] buffer = new byte[6024];
        //ÿ�ζ�ȡ���ַ������ȣ����Ϊ-1������ȫ����ȡ���
        int len;
        //ʹ��һ����������buffer������ݶ�ȡ����
        while ((len = inStream.read(buffer)) != -1) {
            //���������buffer��д�����ݣ��м����������ĸ�λ�ÿ�ʼ����len�����ȡ�ĳ���
            outStream.write(buffer, 0, len);
        }
        //�ر�������
        inStream.close();
        //��outStream�������д���ڴ�
        return outStream.toByteArray();
    }

    }



