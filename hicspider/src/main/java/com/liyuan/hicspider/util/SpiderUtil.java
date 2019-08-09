package com.liyuan.hicspider.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by weiyuan on 2018/5/20/020.
 */
public class SpiderUtil {

    private  String url;
    private  String html;
    private  Document document;
    public  int statu_code;
    private Log logger = LogFactory.getLog(SpiderUtil.class);

    public SpiderUtil(){}
    public SpiderUtil(String url) {
        this.url = url;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
    /*
     * 爬取网页信息
     */
    public void pickData() {
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = null;
        try {
            sslcontext = createIgnoreVerifySSL();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);


        //创建自定义的httpclient对象
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager).build();
        try {
            HttpGet httpget = new HttpGet(this.url);

            httpget.addHeader("Accept", "text/html");
            httpget.addHeader("Accept-Charset", "utf-8");
            httpget.addHeader("Accept-Encoding", "gzip");
            httpget.addHeader("Accept-Language", "en-US,en");
            httpget.addHeader("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");

            CloseableHttpResponse response = httpclient.execute(httpget);
            //这个必须要有 //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(200000).setConnectTimeout(20000).build();
            httpget.setConfig(requestConfig);

            StatusLine statusLine= response.getStatusLine();
            this.statu_code = statusLine.getStatusCode();
            logger.info("访问状态码--"+this.statu_code);
            try {
                if (this.statu_code == 200) {
                    logger.info("访问成功");
                    // 获取响应实体
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        this.html = EntityUtils.toString(entity);
                        System.out.println(this.html);
                    }
                } else {
                    response.close();
                    logger.error("联接异常:状态码--"+this.statu_code+"异常URL--"+this.url);
                }

            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * 使用jsoup解析网页信息
     */
    public void  analyzeHTMLByString(){
        this.document = Jsoup.parse(this.html);
    }

    /**
     * 通过html 标签的class 获取elements
     * @param htmlClass
     * @return
     */
    public  Elements getElementsByClass(String htmlClass){

      Elements elements=  this.document.getElementsByClass(htmlClass);
      return elements;
    }

    public Elements getElementsByTag(String htmlTag){
        Elements elements = this.document.getElementsByTag(htmlTag);
        return elements;
    }

    public  Element getElementById(String htmlId){
        Element element = this.document.getElementById(htmlId);
        return element;
    }


    public void downImg(String storePath,String imgUrl){
        File file = new File(storePath);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        URL url = null;
        try {
            url = new URL(imgUrl);
            InputStream in = url.openStream();
            byte[] buff = new byte[1024];
            while(true){
                int readed = in.read(buff);//读取内容长度
                if(readed == -1){
                    break;
                }
                byte[] temp = new byte[readed];
                System.arraycopy(buff, 0, temp, 0, readed);//内容复制
                //写入到文件中
                os.write(temp);
            }
            in.close();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
