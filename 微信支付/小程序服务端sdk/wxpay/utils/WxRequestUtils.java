package com.g5.sky.wxpay.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;


/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 12:51 PM
 */
public class WxRequestUtils {

    private static Logger logger = LoggerFactory.getLogger(WxRequestUtils.class);

    private static final String USER_AGENT =
            " WxPay/1.0.0("
            + System.getProperty("os.arch")         // 操作系统的架构
            + " "
            + System.getProperty("os.name")         // 操作系统的名称
            + " "
            + System.getProperty("os.version")      // 操作系统的版本
            + ") Java/"
            + System.getProperty("java.version")    // Java 运行时环境版本
            + " HttpClient/"
            + HttpClient.class.getPackage().getImplementationVersion();     // HttpClient 版本

    /**
     * 不带证书请求
     * @param requestUrl    请求地址
     * @param dataXml   请求数据 xml
     * @return  返回 xml
     */
    public static String requestWithoutCert(String requestUrl, String dataXml) {
        return request(requestUrl, dataXml, false, null, null);
    }

    /**
     * 带证书请求
     * @param requestUrl    请求地址
     * @param dataXml   请求数据 xml
     * @param mchId     微信分配给商户的 mch_id，用作读取证书的密码
     * @param urlCertPkcs12 微信证书位置
     * @return
     */
    public static String requestWithCert(String requestUrl, String dataXml, String mchId, String urlCertPkcs12) {
        String respXml;

        // 获取证书
        File certFile = new File(urlCertPkcs12);
        if (certFile.exists()) {
            try {
                InputStream certStream = new FileInputStream(certFile);
                respXml = request(requestUrl, dataXml, true, mchId, certStream);
            } catch (Exception e) {
                respXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[获取证书失败]]></return_msg></xml>";
                logger.error("获取证书失败, urlCertPkcs12=" + urlCertPkcs12, e);
            }
        } else {
            respXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[证书未找到]]></return_msg></xml>";
            logger.error("证书未找到", new FileNotFoundException("证书未找到"));
        }

        return respXml;
    }

    /**
     * request 异常处理
     * @param requestUrl    请求地址
     * @param dataXml   访问微信的 xml 数据
     * @param useCert   是否使用证书
     * @param mchId     微信分配给商户的 mch_id，用作读取证书的密码
     * @param certStream     微信证书
     * @return
     */
    private static String request(String requestUrl, String dataXml, boolean useCert, String mchId, InputStream certStream) {
        String respXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[请求失败]]></return_msg></xml>";
        try {
            respXml = request(requestUrl, dataXml, 6*1000, 8*1000, useCert, mchId, certStream);
        } catch (UnknownHostException e) {
            logger.error("===> 向 微信 发出请求失败，未知的地址", e);
        } catch (ConnectTimeoutException e) {
            logger.error("===> 向 微信 发出请求失败，连接超时", e);
        } catch (SocketTimeoutException e) {
            logger.error("===> 向 微信 发出请求失败，socket 连接超时", e);
        } catch (Exception e) {
            logger.error("===> 向 微信 发出请求失败", e);
        }
        return respXml;
    }

    /**
     * 发起一次请求
     * @param requestUrl    请求地址
     * @param dataXml   访问微信的 xml 数据
     * @param connectTimeoutMs  请求超时时间
     * @param readTimeoutMs     socket超时时间
     * @param useCert   是否使用证书
     * @param mchId     微信分配给商户的 mch_id，用作读取证书的密码
     * @param certStream     微信证书
     * @return  POST 请求返回体
     * @throws Exception    异常
     */
    private static String request(String requestUrl, String dataXml, int connectTimeoutMs, int readTimeoutMs, boolean useCert, String mchId, InputStream certStream) throws Exception {

        // 使用 HttpClient 的简单连接管理
        BasicHttpClientConnectionManager connManager;

        if (useCert) {  // 使用证书

            // 加载证书
            char[] password = mchId.toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            // 实例化 BasicHttpClientConnectionManager
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        } else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        // 实例化 httpClient
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        // 准备 POST 请求
        HttpPost httpPost = new HttpPost(requestUrl);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(dataXml, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.setEntity(postEntity);

        // 发起请求
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String respEntity = EntityUtils.toString(httpEntity, "UTF-8");
        String requEntity = EntityUtils.toString(httpPost.getEntity(), "UTF-8");
        logger.info("===> 向 微信 成功发出请求 \nrequEntity:\n" + requEntity + "\nrespEntity:\n" + respEntity);
        return respEntity;
    }


}
