package com.g5.sky.wxpay.utils;

import com.alibaba.fastjson.JSON;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.*;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 12:53 PM
 */
public class WxPayUtils {

    private static Logger logger = LoggerFactory.getLogger(WxPayUtils.class);

    public static final String ALGORITHM = "AES/ECB/PKCS7Padding";

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    public static boolean initialized = false;

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    /**
     * AES-256-ECB解密
     * @param bytes 解密字节数组
     * @param key 秘钥字节数组
     * @return 解密结果
     * @throws Exception
     */
    public static String Aes256Decode(byte[] bytes, byte[] key)
            throws Exception {
        initialize();
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = cipher.doFinal(bytes);
            result = new String(decoded, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * 对微信退款通知结果 req_info 解密
     * @param key 秘钥， key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
     * @param reqInfo 加密信息
     * @return 解密后的 xml
     */
    public static String decryptRefundNotice(String key, String reqInfo) {

        try {
            // base64解码
            byte[] b = new BASE64Decoder().decodeBuffer(reqInfo);

            // 对key做md5
            String keyMd5 = MD5(key);

            // AES-256-ECB解密
            String result = Aes256Decode(b, keyMd5.getBytes());
            logger.info("对微信退款通知 req_info 解密 \nresult:\n" + result);

            return result;
        } catch (Exception e) {
            logger.error("对微信退款通知 req_info 解密失败", e);

            return null;
        }

    }

    /**
     * 字符串转整数
     * @param intStr 字符串
     * @param failNumber 转化失败赋值
     * @return 结果整数
     */
    public static int parseInt(String intStr, int failNumber) {

        int result;

        try {
            result = Integer.parseInt(intStr);
        } catch (Exception e) {
            logger.warn("字符串转整型失败");
            result = failNumber;
        }

        return result;
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * 生成签名. 使用 MD5 签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) {

        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();

        for (String k : keyArray) {
            if ("sign".equals(k)) {
                continue;
            }
            if (data.get(k) != null && data.get(k).trim().length() > 0)
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);

        try {
            return MD5(sb.toString()).toUpperCase();
        } catch (Exception e) {
            logger.error("生成签名 MD5 加密失败", e);
            return null;
        }
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String map2xml(Map<String, String> data) {

        try {
            Document document = newDocument();
            Element root = document.createElement("xml");
            document.appendChild(root);

            for (String key : data.keySet()) {
                String value = data.get(key);
                if (value == null) {
                    value = "";
                }
                value = value.trim();
                Element filed = document.createElement(key);
                filed.appendChild(document.createTextNode(value));
                root.appendChild(filed);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString();

            try {
                writer.close();
            } catch (Exception e) {
                logger.error("转化map为xml，关闭字符串流失败", e);
                return null;
            }

            return output;
        } catch (Exception e) {
            logger.error("map 转化为 xml 失败 \nmap:\n" + JSON.toJSONString(data), e);
            return null;
        }
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xml2map(String strXML) {
        Map<String, String> data = new HashMap<>();

        try {

            DocumentBuilder documentBuilder = newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }

            try {
                stream.close();
            } catch (Exception e) {
                logger.error("转化xml为map，关闭字节流失败", e);
                return null;
            }

        } catch (Exception e) {
            logger.error("无效的xml，无法转化为map \nxml:\n" + strXML, e);
            return null;
        }

        return data;

    }

    private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    public static Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }
}
