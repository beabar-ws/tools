package com.g5.sky.wxpay;

import com.g5.sky.wxpay.request.WxMiniPayRefundRequest;
import com.g5.sky.wxpay.request.WxMiniPayUnifiedorderRequest;
import com.g5.sky.wxpay.response.WxMiniPayRefundResponse;
import com.g5.sky.wxpay.response.WxMiniPayUnifiedorderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;
import java.util.TreeMap;

import static com.g5.sky.wxpay.constants.WxMiniPayConstants.WxMiniPay_refund;
import static com.g5.sky.wxpay.constants.WxMiniPayConstants.WxMiniPay_unifiedorder;
import static com.g5.sky.wxpay.utils.WxPayUtils.generateSignature;
import static com.g5.sky.wxpay.utils.WxRequestUtils.requestWithCert;
import static com.g5.sky.wxpay.utils.WxRequestUtils.requestWithoutCert;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 11:48 AM
 */
public class WxMiniPay {

    private static Logger logger = LoggerFactory.getLogger(WxMiniPay.class);

    /**
     * 小程序统一下单，客户端二次签名
     * @param appId 微信 appId
     * @param appKey 微信 appKey
     * @param timeStamp 时间戳
     * @param nonceStr 随即串
     * @param _package prepay_id=${prepay_id}
     * @param signType 签名算法
     * @return 签名
     */
    public static String unifiedorderClientSign(String appId, String appKey, String timeStamp, String nonceStr, String _package, String signType) {

        SortedMap<String, String> map = new TreeMap<>();
        map.put("appId", appId);
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", _package);
        map.put("signType", signType);

        return generateSignature(map, appKey);
    }

    /**
     * 统一下单
     * @param request 请求实体
     * @param key 签名 key
     * @return 下单返回实体
     */
    public static WxMiniPayUnifiedorderResponse unifiedorder(WxMiniPayUnifiedorderRequest request, String key) {

        String requXml = request.toXml(key);
        if (requXml == null) return null;

        String respXml = requestWithoutCert(WxMiniPay_unifiedorder, requXml);

        logger.info("===> 微信 小程序 统一下单，\nresponse:\n" + respXml);

        return new WxMiniPayUnifiedorderResponse(respXml);
    }

    /**
     * 申请退款
     * @param request 请求实体
     * @param key 签名 key
     * @param mchId 商户id
     * @param urlCertPkcs12 证书地址
     * @return 退款返回实体
     */
    public static WxMiniPayRefundResponse refund(WxMiniPayRefundRequest request, String key, String mchId, String urlCertPkcs12) {

        String requXml = request.toXml(key);
        if (requXml == null) return null;

        String respXml = requestWithCert(WxMiniPay_refund, requXml, mchId, urlCertPkcs12);

        logger.info("===> 微信 小程序 退款申请，\nresponse:\n" + respXml);

        return new WxMiniPayRefundResponse(respXml);
    }


}
